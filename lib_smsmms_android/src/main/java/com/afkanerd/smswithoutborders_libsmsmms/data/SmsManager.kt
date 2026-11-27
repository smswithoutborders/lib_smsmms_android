package com.afkanerd.smswithoutborders_libsmsmms.data

import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsManager
import android.text.TextUtils
import android.util.Base64
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.viewModelScope
import com.afkanerd.lib_smsmms_android.R
import com.afkanerd.smswithoutborders_libsmsmms.data.entities.Conversations
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.getBytesFromUri
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.getDatabase
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.getIdFromLocalDb
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.getThreadId
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.settingsGetGetDeliveryReports
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.settingsGetKeepMessagesArchived
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.settingsGetStoreTelephonyDb
import com.afkanerd.smswithoutborders_libsmsmms.receivers.MmsSentReceiverImpl
import com.afkanerd.smswithoutborders_libsmsmms.receivers.SmsTextReceivedReceiver
import com.klinker.android.send_message.Message
import com.klinker.android.send_message.Transaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.Collections

class SmsManager(private val context: Context) {
    fun sendSms(
        text: String,
        address: Addresses,
        threadId: Int,
        subscriptionId: Long,
        data: ByteArray? = null,
        bundle: Bundle? = null,
    ): Conversations? {
        if(text.isEmpty() && data == null) {
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(context,
                    context.getString(R.string.text_body_cannot_empty), Toast.LENGTH_LONG).show()
            }
            return null
        }

        val date = System.currentTimeMillis()

        var conversation: Conversations?

        try {
            conversation = Conversations(sms = SmsMmsNatives.Sms(
                thread_id = threadId,
                address = address.getFullE164Address,
                date = date,
                date_sent = date,
                read = 1,
                status = Telephony.Sms.STATUS_PENDING,
                type = Telephony.Sms.MESSAGE_TYPE_QUEUED,
                body = if(data == null) text else {
                    Base64.encodeToString(data, Base64.DEFAULT)
                },
                sub_id = subscriptionId,
            ), sms_data = data)

            insertSms(conversation)?.let { uri ->
                val pendingIntents = getSmsPendingIntents(uri, conversation, bundle)

                sendSms(
                    address = address.getFullE164Address,
                    conversation = conversation,
                    uri = uri,
                    sentPendingIntent = pendingIntents.first,
                    deliveredPendingIntent = if(context.settingsGetGetDeliveryReports)
                        pendingIntents.second else null,
                )
            }

        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }

        return conversation
    }


    fun sendMms(
        contentUri: Uri,
        text: String,
        addresses: Addresses,
        threadId: Int,
        subscriptionId: Long,
        filename: String,
        mimeType: String
    ): Conversations {
        val address = addresses.getFullE164Address

        val conversation = Conversations(
            sms = SmsMmsNatives.Sms(
                _id = System.currentTimeMillis(),
                thread_id = threadId,
                date = System.currentTimeMillis(),
                date_sent = 0,
                type = Telephony.Sms.MESSAGE_TYPE_QUEUED,
                status = Telephony.Sms.STATUS_PENDING,
                read = 1,
                sub_id = subscriptionId,
                address = address,
                body = null
            ),
            mms_text = text,
            mms_content_uri = contentUri.toString(),
            mms_mimetype = mimeType,
            mms_filename = filename,
        )

        try {
            insertMms(conversation)
            val sendSettings = MmsParser.getSendMessageSettings()
            sendSettings.subscriptionId = subscriptionId.toInt()

            val sendTransaction = Transaction(context, sendSettings)

            val intent = Intent(context, MmsSentReceiverImpl::class.java)
                .apply {
                    this.putExtra(
                        MmsSentReceiverImpl.EXTRA_ORIGINAL_RESENT_MESSAGE_ID,
                        conversation.id,
                    )
                }
            sendTransaction.setExplicitBroadcastForSentMms(intent)

            val mMessage = Message(text, address)
            mMessage.addMedia(
                context.getBytesFromUri(contentUri),
                mimeType,
                filename
            )

            try {
                sendTransaction.sendNewMessage(mMessage)
            } catch(e: Exception) {
                conversation.sms?.status = Telephony.Sms.STATUS_FAILED
                conversation.sms?.type = Telephony.Sms.MESSAGE_TYPE_FAILED
                updateMms(conversation)
                throw e
            }
            conversation.sms?.type = Telephony.Sms.MESSAGE_TYPE_OUTBOX
            updateMms(conversation)

        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
        return conversation
    }

    private fun sendSms(
        address: String,
        conversation: Conversations,
        uri: Uri,
        sentPendingIntent: PendingIntent?,
        deliveredPendingIntent: PendingIntent?,
    ) {
        val dataTransmissionPort: Short = 8200
        val smsManager = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            context.getSystemService(SmsManager::class.java)
                .createForSubscriptionId(conversation.sms?.sub_id!!.toInt())
        else SmsManager.getSmsManagerForSubscriptionId(conversation.sms?.sub_id!!.toInt())

        try {
            if(conversation.sms_data != null) {
                smsManager.sendDataMessage(
                    address,
                    null,
                    dataTransmissionPort,
                    conversation.sms_data,
                    sentPendingIntent,
                    deliveredPendingIntent
                )
            } else {
                val dividedMessage = smsManager.divideMessage(conversation.sms?.body)
                if (dividedMessage.size < 2) smsManager.sendTextMessage(
                    address,
                    null,
                    conversation.sms?.body,
                    sentPendingIntent,
                    deliveredPendingIntent
                )
                else {
                    val sentPendingIntents = ArrayList<PendingIntent?>()
                    val deliveredPendingIntents = ArrayList<PendingIntent?>()

                    for (i in 0 until dividedMessage.size - 1) {
                        sentPendingIntents.add(null)
                        deliveredPendingIntents.add(null)
                    }

                    sentPendingIntents.add(sentPendingIntent)
                    deliveredPendingIntents.add(deliveredPendingIntent)

                    smsManager.sendMultipartTextMessage(
                        address,
                        null,
                        dividedMessage,
                        sentPendingIntents,
                        deliveredPendingIntents
                    )
                }
            }

        } catch(e: Exception) {
            conversation.sms?.status = Telephony.Sms.STATUS_FAILED
            conversation.sms?.type = Telephony.Sms.MESSAGE_TYPE_FAILED
            updateSms(uri, conversation)
            throw e
        }
        conversation.sms?.type = Telephony.Sms.MESSAGE_TYPE_OUTBOX
        updateSms(uri, conversation)
    }

    private fun getSmsPendingIntents(
        uri: Uri?,
        conversation: Conversations,
        bundle: Bundle? = null
    ): Pair<PendingIntent, PendingIntent> {
        val sentPendingIntent = PendingIntent.getBroadcast(
            context,
            conversation.id.toInt(),
            Intent().apply {
                setPackage(context.packageName)
                action = if(conversation.sms_data == null)
                    SmsTextReceivedReceiver.SMS_SENT_BROADCAST_INTENT else
                    SmsTextReceivedReceiver.DATA_SENT_BROADCAST_INTENT

                this.putExtra("id", conversation.id)
                this.putExtra("address", conversation.sms?.address)
                this.putExtra("thread_id", conversation.sms?.thread_id)
                this.putExtra("sub_id", conversation.sms?.sub_id)
                this.putExtra("uri", uri?.toString())
                bundle?.let { this.putExtras(it) }
            },
            PendingIntent.FLAG_IMMUTABLE
        )

        val deliveredPendingIntent = PendingIntent.getBroadcast(
            context,
            conversation.id.toInt(),
            Intent().apply {
                setPackage(context.packageName)
                action = if(conversation.sms_data == null)
                    SmsTextReceivedReceiver.SMS_DELIVERED_BROADCAST_INTENT else
                    SmsTextReceivedReceiver.DATA_DELIVERED_BROADCAST_INTENT

                this.putExtra("id", conversation.id)
                this.putExtra("address", conversation.sms?.address)
                this.putExtra("thread_id", conversation.sms?.thread_id)
                this.putExtra("sub_id", conversation.sms?.sub_id)
                this.putExtra("uri", uri?.toString())
            },
            PendingIntent.FLAG_IMMUTABLE
        )

        return Pair(sentPendingIntent, deliveredPendingIntent)
    }


    fun registerIncomingSms(
        intent: Intent,
        data: Boolean = false,
    ): Conversations {
        val bundle = intent.extras
        val subscriptionId = bundle!!.getInt("subscription", -1)
        var address: String? = ""
        val bodyBuffer = StringBuilder()
        val dataBuffer = ByteArrayOutputStream()
        var dateSent: Long = 0
        val date = System.currentTimeMillis()
        var status = -1

        for (currentSMS in Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
            address = currentSMS.displayOriginatingAddress
            bodyBuffer.append(currentSMS.displayMessageBody)
            dataBuffer.write(currentSMS.userData)
            dateSent = currentSMS.timestampMillis
            status = currentSMS.status
        }
        val body = bodyBuffer.toString()

        val conversation = Conversations(
            sms = SmsMmsNatives.Sms(
                body = body,
                sub_id = subscriptionId.toLong(),
                date = date,
                date_sent = dateSent,
                address = address!!,
                type = Telephony.Sms.MESSAGE_TYPE_INBOX,
                status = status,
                thread_id = context.getThreadId(address),
                read = 0,
            ),
            sms_data = if(data) dataBuffer.toByteArray() else null
        )

        insertSms(conversation)
        return conversation
    }

    fun insertMms(conversation: Conversations) {
        try{
            context.getDatabase().conversationsDao()
                ?.insert(conversation, context.settingsGetKeepMessagesArchived)
                ?.let { id -> conversation.id = id }

        } catch (e: Exception) {
            throw e
        }
    }

    fun insertSms(conversation: Conversations): Uri? {
        var uri: Uri? = null
        if(context.settingsGetStoreTelephonyDb) {
            try {
                uri = insertSmsTelephony(
                    conversation.sms?.body,
                    conversation.sms?.sub_id!!,
                    conversation.sms?.address!!,
                    conversation.sms?.date!!,
                    conversation.sms?.type!!,
                    conversation.sms?.read!!
                )
            } catch(e: Exception) {
                throw e
            }
        }

        try{
            conversation.sms?._id = if(uri != null)
                context.getIdFromLocalDb(uri) else System.currentTimeMillis()

            context.getDatabase().conversationsDao()
                ?.insert(conversation, context.settingsGetKeepMessagesArchived)
                ?.let { id -> conversation.id = id }

        } catch (e: Exception) {
            throw e
        }
        return uri
    }

    fun deleteSmsThreads(threadIds: Array<String>): Int {
        return context.contentResolver.delete(
            Telephony.Sms.CONTENT_URI,
            Telephony.TextBasedSmsColumns.THREAD_ID + " in (" +
                    TextUtils.join(",",
                        Collections.nCopies(threadIds.size, "?")) + ")",
            threadIds
        )
    }

    private fun updateSmsToLocalDb(
        uri: Uri,
        conversations: Conversations
    ) {
        val contentValues = ContentValues().apply {
            put(Telephony.TextBasedSmsColumns.TYPE, conversations.sms?.type)
            put(Telephony.TextBasedSmsColumns.STATUS, conversations.sms?.status)
            put(Telephony.TextBasedSmsColumns.ERROR_CODE, conversations.sms?.error_code)
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                context.contentResolver.update(uri, contentValues, null)
            } else {
                context.contentResolver.update(uri, contentValues, null, null)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    private fun insertSmsTelephony(
        text: String?,
        sub_id: Long,
        address: String,
        date: Long,
        type: Int,
        read: Int,
    ): Uri? {
        val contentValues = ContentValues().apply {
            put(Telephony.TextBasedSmsColumns.BODY, text)
            put(Telephony.TextBasedSmsColumns.DATE, date)
            put(Telephony.TextBasedSmsColumns.TYPE, type)
            put(Telephony.TextBasedSmsColumns.ADDRESS, address)
            put(Telephony.TextBasedSmsColumns.SUBSCRIPTION_ID, sub_id)
            put(Telephony.TextBasedSmsColumns.READ, read)
            put(Telephony.TextBasedSmsColumns.STATUS, Telephony.TextBasedSmsColumns.STATUS_NONE)
        }

        try {
            return context.contentResolver.insert( Telephony.Sms.CONTENT_URI, contentValues)
        } catch (e: Exception) {
            throw e
        }
    }

    private fun updateMms(conversation: Conversations) {
        try {
            context.getDatabase().conversationsDao()?.update(conversation)
        } catch(e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    fun updateSms(uri: Uri, conversation: Conversations) {
        try {
            if(context.settingsGetStoreTelephonyDb)
                updateSmsToLocalDb(uri,conversation)
            context.getDatabase().conversationsDao()?.update(conversation)
        } catch(e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}