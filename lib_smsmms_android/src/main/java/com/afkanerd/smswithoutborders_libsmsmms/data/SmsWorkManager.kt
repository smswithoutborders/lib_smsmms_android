package com.afkanerd.smswithoutborders_libsmsmms.data

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.afkanerd.smswithoutborders_libsmsmms.extensions.toByteArray
import com.afkanerd.smswithoutborders_libsmsmms.extensions.toShortLittleEndian
import com.afkanerd.smswithoutborders_libsmsmms.services.ImageTransmissionService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class SmsWorkManager(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams ) {
    val workValue = MutableStateFlow<Result?>(null)

    override suspend fun doWork(): Result = suspendCancellableCoroutine { cont ->
        val itp = inputData.getByteArray(ITP_PAYLOAD)
        if(itp == null) {
            cont.resume( Result.failure(
                Data.Builder().putString("reason", "ITP_PAYLOAD null").build()))
        }

        val address = inputData.getString(ITP_TRANSMISSION_ADDRESS)
        if(address == null) {
            cont.resume(Result.failure(
                Data.Builder().putString("reason", "ITP_TRANSMISSION_ADDRESS null")
                    .build()))
        }

        val subscriptionId = inputData.getLong(ITP_TRANSMISSION_SUBSCRIPTION_ID,
            -1)

        val icon = inputData.getInt(ITP_SERVICE_ICON, -1).also {
            if(it == -1)
                cont.resume( Result.failure(
                        Data.Builder().putString("reason", "ITP_SERVICE_ICON null")
                            .build()))
        }

        val version = inputData.getByte(ITP_VERSION, -1).also {
            if(it.toInt() == -1)
                cont.resume(
                    Result.failure(
                        Data.Builder().putString("reason", "ITP_VERSION null").build()))
        }

        val sessionId = inputData.getByte(ITP_SESSION_ID, -1).also {
            if(it.toInt() == -1)
                cont.resume(
                    Result.failure(
                        Data.Builder().putString("reason", "ITP_SESSION_ID null")
                            .build()))
        }

        val imageLength = inputData.getByteArray(ITP_IMAGE_LENGTH).also {
            if(it == null)
                cont.resume( Result.failure(
                        Data.Builder().putString("reason", "ITP_IMAGE_LENGTH null")
                            .build()) )
        }

        val textLength = inputData.getByteArray(ITP_TEXT_LENGTH).also {
            if(it == null)
                cont.resume(
                    Result.failure(
                        Data.Builder().putString("reason", "ITP_TEXT_LENGTH null")
                            .build()) )
        }

        val filter = IntentFilter(ITP_SERVICE_COMPLETION)

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                // Service says it's finished
                applicationContext.unregisterReceiver(this)
                cont.resume(Result.success())
            }
        }

        ContextCompat.registerReceiver(
            applicationContext,
            receiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )

        startService(
            itp!!,
            sessionId,
            icon,
            address!!,
            version,
            imageLength!!.toShortLittleEndian(),
            textLength = textLength!!.toShortLittleEndian(),
            subscriptionId = subscriptionId
        )
    }

    fun startService(
        itp: ByteArray,
        sessionId: Byte,
        icon: Int,
        address: String,
        version: Byte,
        imageLength: Short,
        textLength: Short,
        subscriptionId: Long = -1,
    ) {
        val intent = Intent(
            applicationContext,
            ImageTransmissionService::class.java
        ).apply {
            putExtra(ITP_PAYLOAD, itp)
            putExtra(ITP_SESSION_ID, sessionId)
            putExtra(ITP_SERVICE_ICON, icon)
            putExtra(ITP_VERSION, version)
            putExtra(ITP_IMAGE_LENGTH, imageLength)
            putExtra(ITP_TEXT_LENGTH, textLength)
            putExtra(ITP_TRANSMISSION_ADDRESS, address)
            putExtra(ITP_TRANSMISSION_SUBSCRIPTION_ID, subscriptionId)
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                applicationContext.startForegroundService(intent)
            } else {
                applicationContext.startService(intent)
            }
        } catch(e: Exception) {
            e.printStackTrace()
        }
    }


    companion object {
        const val ITP_PAYLOAD = "ITP_PAYLOAD"
        const val ITP_VERSION = "ITP_VERSION"
        const val ITP_SESSION_ID = "ITP_SESSION_ID"
        const val ITP_TRANSMISSION_ADDRESS = "ITP_TRANSMISSION_ADDRESS"
        const val ITP_TRANSMISSION_SUBSCRIPTION_ID = "ITP_TRANSMISSION_SUBSCRIPTION_ID"
        const val ITP_IMAGE_LENGTH = "ITP_IMAGE_LENGTH"
        const val ITP_TEXT_LENGTH = "ITP_TEXT_LENGTH"
        const val ITP_SERVICE_ICON = "ITP_SERVICE_ICON"
        const val ITP_STOP_SERVICE = "ITP_STOP_SERVICE"
        const val IMAGE_TRANSMISSION_WORK_MANAGER_TAG = "IMAGE_TRANSMISSION_WORK_MANAGER_TAG"
        const val ITP_SERVICE_COMPLETION = "ITP_IS_SUCCESS"
    }

}