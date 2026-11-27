package com.afkanerd.smswithoutborders_libsmsmms.extensions.context

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsManager
import android.text.TextUtils
import android.util.Base64
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.database.getIntOrNull
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import androidx.core.net.toUri
import com.afkanerd.lib_smsmms_android.R
import com.afkanerd.smswithoutborders_libsmsmms.data.MmsParser
import com.afkanerd.smswithoutborders_libsmsmms.data.SmsMmsNatives
import com.afkanerd.smswithoutborders_libsmsmms.data.entities.Conversations
import com.afkanerd.smswithoutborders_libsmsmms.receivers.MmsSentReceiverImpl
import com.afkanerd.smswithoutborders_libsmsmms.receivers.SmsTextReceivedReceiver
import com.google.gson.GsonBuilder
import com.klinker.android.send_message.Message
import com.klinker.android.send_message.Transaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.Collections

@SuppressLint("Range")
fun Context.getIdFromLocalDb(uri: Uri): Long? {
    contentResolver.query(
        uri,
        null,
        null,
        null,
        null
    )?.let { cursor ->
        if(cursor.moveToFirst()) {
            val id = cursor.getLong(cursor
                .getColumnIndex(Telephony.Sms._ID))
            return id
        }
        cursor.close()
    }
    return null
}

@RequiresApi(Build.VERSION_CODES.O)
@Throws
fun Context.loadRawThreads() : List<Pair<String, Boolean>>{
    val threadIds = mutableListOf<Pair<String, Boolean>>()

    fun processCursor(cursor: Cursor, isMms: Boolean) {
        do {
            val threadId = cursor.getString(0)
            val value = Pair(threadId, isMms)
            if(!threadIds.contains(value))
                threadIds.add(value)
        } while(cursor.moveToNext())
    }

    try {
        val bundle = Bundle()
        bundle.putString(ContentResolver.QUERY_ARG_SQL_GROUP_BY, "thread_id")
        bundle.putString(ContentResolver.QUERY_ARG_SQL_SORT_ORDER, "date DESC");

        contentResolver.query(
            Telephony.Sms.CONTENT_URI,
            arrayOf("thread_id"),
            bundle,
            null
        )?.let { cursor ->
            if(cursor.moveToFirst()) { processCursor(cursor, false) }
            cursor.close()
        }

        contentResolver.query(
            Telephony.Mms.CONTENT_URI,
            arrayOf("thread_id"),
            bundle,
            null
        )?.let { cursor ->
            if(cursor.moveToFirst()) { processCursor(cursor, true) }
            cursor.close()
        }
    } catch(e: Exception) {
        e.printStackTrace()
    }

    return threadIds
}

@Throws
fun Context.loadRawSmsMmsDb(threadId: String? = null, isMms: Boolean) : List<Conversations>{
    val conversationsList = arrayListOf<Conversations>()

    val selection = if(threadId != null) "thread_id =?" else threadId
    val selectionArgs = if(threadId != null) arrayOf(threadId) else null

    try {
        if(!isMms) {
            // SMS
            contentResolver.query(
                Telephony.Sms.CONTENT_URI,
                null,
                selection,
                selectionArgs,
                "date desc"
            )?.let { cursor ->
                if (cursor.moveToFirst()) {
                    do {
                        parseRawSmsContents(cursor)?.let { it ->
                            conversationsList.add(Conversations(sms = it.apply {
                                this.address = findRCSPhoneNumbers(this.address!!)?.run {
                                    this[0]
                                } ?: this.address!!.trimEnd()
                                this.thread_id = getThreadId(this.address!!)
                            }))
                        }
                    } while (cursor.moveToNext())
                }
                cursor.close()
            }
        } else {
            contentResolver.query(
                Telephony.Mms.CONTENT_URI,
                null,
                selection,
                selectionArgs,
                "date desc"
            )?.let { cursor ->
                if(cursor.moveToFirst()) {
                    do {
                        val conversation = MmsParser.parse(this, cursor)
                        conversation?.sms?.let {
                            conversationsList.add(conversation)
                        }
                    } while(cursor.moveToNext())
                    cursor.close()
                }
            }
        }
    } catch (e: Exception) {
        throw e
    }

    return conversationsList
}

fun Context.exportRawWithColumnGuesses(): String {
    val mmsContents = arrayListOf<SmsMmsNatives.Mms>()
    val mmsAddrContents = arrayListOf<SmsMmsNatives.MmsAddr>()
    val mmsPartsContents = arrayListOf<SmsMmsNatives.MmsPart>()
    val smsContents = arrayListOf<SmsMmsNatives.Sms>()

    val mmsIds = mutableSetOf<Long>()

    // MMS
    contentResolver.query(
        Telephony.Mms.CONTENT_URI,
        null,
        null,
        null,
        null
    )?.let { cursor ->
        if(cursor.moveToFirst()) {
            do {
                mmsContents.add(parseRawMmsContents(cursor).apply {
                    mmsIds.add(this._id)
                })
            } while(cursor.moveToNext())
        }
        cursor.close()
    }

    // MMSAddr
    mmsIds.forEach {
        contentResolver.query(
            "content://mms/${it}/addr".toUri(),
            null,
            null,
            null,
            null
        )?.let { cursor ->
            if(cursor.moveToFirst()) {
                do {
                    mmsAddrContents.add(parseRawMmsAddrContentsParts(cursor))
                } while(cursor.moveToNext())
            }
            cursor.close()
        }
    }

    // MMS/Parts
    contentResolver.query(
        "content://mms/part".toUri(),
        null,
        null,
        null,
        null
    )?.let { cursor ->
        if(cursor.moveToFirst()) {
            do {
                mmsPartsContents.add(parseRawMmsContentsParts(cursor))
            } while(cursor.moveToNext())
        }
    }


    // SMS
    contentResolver.query(
        Telephony.Sms.CONTENT_URI,
        null,
        null,
        null,
        null
    )?.let { cursor ->
        if(cursor.moveToFirst()) {
            do {
                parseRawSmsContents(cursor)?.let { smsContents.add(it) }
            } while(cursor.moveToNext())
        }
        cursor.close()
    }

    val smsMmsContents = SmsMmsNatives.SmsMmsContents(
        mapOf(
            Pair(
                Telephony.Mms.CONTENT_URI.toString(),
                mmsContents
            )
        ),

        mapOf(Pair("content://mms/{_id}/addr", mmsAddrContents)),
        mapOf(Pair("content://mms/part/{_id}", mmsPartsContents)),

        mapOf(
            Pair(
                Telephony.Sms.CONTENT_URI.toString(),
                smsContents
            )
        ),
    )

    val gson = GsonBuilder()
        .serializeNulls()
        .setPrettyPrinting()
        .create()
    return gson.toJson(smsMmsContents)
}

@SuppressLint("Range")
private fun parseRawMmsAddrContentsParts(cursor: Cursor): SmsMmsNatives.MmsAddr {
    val _id: Int = cursor.getInt(cursor
        .getColumnIndex(Telephony.Mms.Addr._ID))
    val msg_id : String? = cursor.getStringOrNull(cursor
        .getColumnIndex(Telephony.Mms.Addr.MSG_ID))
    val contact_id: String? = cursor.getStringOrNull(cursor
        .getColumnIndex(Telephony.Mms.Addr.CONTACT_ID))
    val address: String? = cursor.getStringOrNull(cursor
        .getColumnIndex(Telephony.Mms.Addr.ADDRESS))
    val type: String? = cursor.getStringOrNull(cursor
        .getColumnIndex(Telephony.Mms.Addr.TYPE))
    val charset: String? = cursor.getStringOrNull(cursor
        .getColumnIndex(Telephony.Mms.Addr.CHARSET))
    val sub_id: Long? = cursor.getLongOrNull(cursor
        .getColumnIndex("sub_id"))

    return SmsMmsNatives.MmsAddr(
        _id = _id,
        msg_id = msg_id,
        contact_id = contact_id,
        address = address,
        type = type,
        charset = charset,
        sub_id = sub_id
    )
}

@SuppressLint("Range")
private fun parseRawMmsContentsParts(cursor: Cursor): SmsMmsNatives.MmsPart {
    val _id: Int = cursor.getInt(cursor
        .getColumnIndex(Telephony.Mms.Part._ID))
    val mid: Long = cursor.getLong(cursor
        .getColumnIndex(Telephony.Mms.Part.MSG_ID))
    val seq: Int = cursor.getInt(cursor
        .getColumnIndex(Telephony.Mms.Part.SEQ))
    val ct: String? = cursor.getStringOrNull(cursor
        .getColumnIndex(Telephony.Mms.Part.CONTENT_TYPE))
    val name: String? = cursor.getStringOrNull(cursor
        .getColumnIndex(Telephony.Mms.Part.NAME))
    val cid: String? = cursor.getStringOrNull(cursor
        .getColumnIndex(Telephony.Mms.Part.CONTENT_ID))
    val cl: String? = cursor.getStringOrNull(cursor
        .getColumnIndex(Telephony.Mms.Part.CONTENT_ID))
    val text: String? = cursor.getStringOrNull(cursor
        .getColumnIndex(Telephony.Mms.Part.TEXT))
    val sub_id: Long? = cursor.getLongOrNull(cursor
        .getColumnIndex("sub_id"))
    val _data: String? = cursor.getStringOrNull(cursor
        .getColumnIndex(Telephony.Mms.Part._DATA))
    val chset: Int? = cursor.getIntOrNull(cursor
        .getColumnIndex(Telephony.Mms.Part.CHARSET))

    return SmsMmsNatives.MmsPart(
        _id = _id,
        mid = mid,
        seq = seq,
        ct = ct,
        name = name,
        cid = cid,
        cl = cl,
        text = text,
        sub_id = sub_id,
        _data = _data,
        chset = chset,
    )
}

@SuppressLint("Range")
fun parseRawMmsContents(cursor: Cursor): SmsMmsNatives.Mms {
    val _id: Long = cursor.getLong(cursor
        .getColumnIndex(Telephony.Mms._ID))
    val thread_id: Int = cursor.getInt(cursor
        .getColumnIndex(Telephony.Mms.THREAD_ID))
    val date: Long = cursor.getLong(cursor
        .getColumnIndex(Telephony.Mms.DATE))
    val date_sent: Long = cursor.getLong(cursor
        .getColumnIndex(Telephony.Mms.DATE_SENT))
    val msg_box: Int = cursor.getInt(cursor
        .getColumnIndex(Telephony.Mms.MESSAGE_BOX))
    val read: Int = cursor.getInt(cursor
        .getColumnIndex(Telephony.Mms.READ))
    val m_id: String? = cursor.getStringOrNull(cursor
        .getColumnIndex(Telephony.Mms.MESSAGE_ID))
    val sub: String? = cursor.getStringOrNull(cursor
        .getColumnIndex(Telephony.Mms.SUBJECT))
    val sub_cs: Int = cursor.getInt(cursor
        .getColumnIndex(Telephony.Mms.SUBJECT_CHARSET))
    val ct_t: String? = cursor.getStringOrNull(cursor
        .getColumnIndex(Telephony.Mms.CONTENT_TYPE))
    val ct_l: String? = cursor.getStringOrNull(cursor
        .getColumnIndex(Telephony.Mms.CONTENT_LOCATION))
    val m_cls: String? = cursor.getStringOrNull(cursor
        .getColumnIndex(Telephony.Mms.MESSAGE_CLASS))
    val m_type: Int = cursor.getInt(cursor
        .getColumnIndex(Telephony.Mms.MESSAGE_TYPE))
    val v: Int = cursor.getInt(cursor
        .getColumnIndex(Telephony.Mms.MMS_VERSION))
    val m_size: Int = cursor.getInt(cursor
        .getColumnIndex(Telephony.Mms.MESSAGE_SIZE))
    val pri: Int = cursor.getInt(cursor
        .getColumnIndex(Telephony.Mms.PRIORITY))
    val rr: Int = cursor.getInt(cursor
        .getColumnIndex(Telephony.Mms.READ_REPORT))
    val d_rpt: Int = cursor.getInt(cursor
        .getColumnIndex(Telephony.Mms.DELIVERY_REPORT))
    val locked: Int = cursor.getInt(cursor
        .getColumnIndex(Telephony.Mms.LOCKED))
    val sub_id: Long = cursor.getLong(cursor
        .getColumnIndex(Telephony.Mms.SUBSCRIPTION_ID))
    val seen: Int = cursor.getInt(cursor
        .getColumnIndex(Telephony.Mms.SEEN))
    val creator: String? = cursor.getStringOrNull(cursor
        .getColumnIndex(Telephony.Mms.CREATOR))
    val text_only: Int = cursor.getInt(cursor
        .getColumnIndex(Telephony.Mms.TEXT_ONLY))

    return SmsMmsNatives.Mms(
        _id = _id,
        thread_id = thread_id,
        date = date,
        date_sent = date_sent,
        msg_box = msg_box,
        read = read,
        m_id = m_id,
        sub = sub,
        sub_cs = sub_cs,
        ct_t = ct_t,
        ct_l = ct_l,
        m_cls = m_cls,
        m_type = m_type,
        v = v,
        m_size = m_size,
        pri = pri,
        rr = rr,
        d_rpt = d_rpt,
        locked = locked,
        sub_id = sub_id,
        seen = seen,
        creator = creator,
        text_only = text_only
    )
}

@SuppressLint("Range")
private fun parseRawSmsContents(cursor: Cursor): SmsMmsNatives.Sms? {
    val body: String = cursor.getStringOrNull(cursor
        .getColumnIndex(Telephony.Sms.BODY)) ?: return null

    val _id: Long = cursor.getLong(cursor
        .getColumnIndex(Telephony.Sms._ID))
    val thread_id: Int = cursor.getInt(cursor
        .getColumnIndex(Telephony.Sms.THREAD_ID))
    val address: String? = cursor.getString(cursor
        .getColumnIndex(Telephony.Sms.ADDRESS))
    val date: Long = cursor.getLong(cursor
        .getColumnIndex(Telephony.Sms.DATE))
    val date_sent: Long = cursor.getLong(cursor
        .getColumnIndex(Telephony.Sms.DATE_SENT))
    val read: Int = cursor.getInt(cursor
        .getColumnIndex(Telephony.Sms.READ))
    val status: Int = cursor.getInt(cursor
        .getColumnIndex(Telephony.Sms.STATUS))
    val type: Int = cursor.getInt(cursor
        .getColumnIndex(Telephony.Sms.TYPE))
    val locked: Int = cursor.getInt(cursor
        .getColumnIndex(Telephony.Sms.LOCKED))
    val sub_id: Long = cursor.getLong(cursor
        .getColumnIndex(Telephony.Sms.SUBSCRIPTION_ID))
    val error_code: Int = cursor.getInt(cursor
        .getColumnIndex(Telephony.Sms.ERROR_CODE))
    val creator: String = cursor.getString(cursor
        .getColumnIndex(Telephony.Sms.CREATOR))
    val seen: Int = cursor.getInt(cursor
        .getColumnIndex(Telephony.Sms.SEEN))

    return SmsMmsNatives.Sms(
        _id = _id,
        thread_id = thread_id,
        address = address,
        date = date,
        date_sent = date_sent,
        read = read,
        status = status,
        type = type,
        body = body,
        locked = locked,
        sub_id = sub_id,
        error_code = error_code,
        creator = creator,
        seen = seen
    )
}
