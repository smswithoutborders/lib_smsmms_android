package com.afkanerd.smswithoutborders_libsmsmms.receivers

import android.content.Context
import android.net.Uri
import com.afkanerd.smswithoutborders_libsmsmms.data.MmsParser
import com.afkanerd.smswithoutborders_libsmsmms.data.SmsManager
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.NotificationTxType
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.getDatabase
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.sendNotificationBroadcast
import com.klinker.android.send_message.MmsReceivedReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MmsReceivedReceiverImpl: MmsReceivedReceiver() {
    override fun onMessageReceived(context: Context?, contentUri: Uri?) {
        context?.let { context ->
            processIncomingMms(context, contentUri)
        }
    }

    fun processIncomingMms(
        context: Context,
        contentUri: Uri?,
    ) {
        contentUri?.let {
            context.contentResolver?.query(
                contentUri,
                null,
                null,
                null,
                null,
            )?.let { cursor ->
                if(cursor.moveToFirst()) {
                    MmsParser.parse(context, cursor)?.let{ conversation ->
                        CoroutineScope(Dispatchers.IO).launch {
                            SmsManager(context).insertSms(conversation)
                            context.getDatabase().threadsDao()?.get(conversation.sms?.thread_id!!)
                                ?.let {
                                    if(!it.isMute)
                                        context.sendNotificationBroadcast(
                                            conversation,
                                            type = NotificationTxType.MMS
                                        )
                                }
                        }
                    }
                }
            }
        }
    }

    override fun onError(p0: Context?, p1: String?) {
        TODO("Not yet implemented")
    }
}