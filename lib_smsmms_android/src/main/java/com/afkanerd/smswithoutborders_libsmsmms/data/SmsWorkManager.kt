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
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.afkanerd.lib_smsmms_android.R
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.isDefault
import com.afkanerd.smswithoutborders_libsmsmms.extensions.toShortLittleEndian
import com.afkanerd.smswithoutborders_libsmsmms.receivers.SmsTextReceivedReceiver
import com.afkanerd.smswithoutborders_libsmsmms.services.ImageTransmissionService
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.UUID
import kotlin.coroutines.resume

class SmsWorkManager(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams ) {
    override suspend fun doWork(): Result = suspendCancellableCoroutine { cont ->
        if(!applicationContext.isDefault()) {
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(applicationContext,
                    applicationContext.getString(R.string.not_default_sms_app),
                    Toast.LENGTH_LONG).show()
            }
            cont.resume( Result.failure(
                Data.Builder().putString("reason", "NOT DEFAULT SMS APP null")
                    .build())
            )
            return@suspendCancellableCoroutine
        }

        val itp = inputData.getByteArray(ITP_PAYLOAD)
        if(itp == null) {
            cont.resume( Result.failure(
                Data.Builder().putString("reason", "ITP_PAYLOAD null").build()))
            return@suspendCancellableCoroutine
        }

        val address = inputData.getString(ITP_TRANSMISSION_ADDRESS)
        if(address == null) {
            cont.resume(Result.failure(
                Data.Builder().putString("reason", "ITP_TRANSMISSION_ADDRESS null")
                    .build()))
            return@suspendCancellableCoroutine
        }

        val subscriptionId = inputData.getLong(ITP_TRANSMISSION_SUBSCRIPTION_ID,
            -1)

        val icon = inputData.getInt(ITP_SERVICE_ICON, -1).also {
            if(it == -1) {
                cont.resume(
                    Result.failure(
                        Data.Builder().putString("reason", "ITP_SERVICE_ICON null")
                            .build()
                    )
                )
                return@suspendCancellableCoroutine
            }
        }

        val version = inputData.getByte(ITP_VERSION, -1).also {
            if(it.toInt() == -1) {
                cont.resume(
                    Result.failure(
                        Data.Builder().putString("reason", "ITP_VERSION null").build()
                    )
                )
                return@suspendCancellableCoroutine
            }
        }

        val sessionId = inputData.getByte(ITP_SESSION_ID, -1).also {
            if(it.toInt() == -1) {
                cont.resume(
                    Result.failure(
                        Data.Builder().putString("reason", "ITP_SESSION_ID null")
                            .build()
                    )
                )
                return@suspendCancellableCoroutine
            }
        }

        val imageLength = inputData.getByteArray(ITP_IMAGE_LENGTH) .also {
            if(it == null) {
                cont.resume(
                    Result.failure(
                        Data.Builder().putString("reason", "ITP_IMAGE_LENGTH null")
                            .build()
                    )
                )
                return@suspendCancellableCoroutine
            }
        }

        val textLength = inputData.getByteArray(ITP_TEXT_LENGTH).also {
            if(it == null) {
                cont.resume(
                    Result.failure(
                        Data.Builder().putString("reason", "ITP_TEXT_LENGTH null")
                            .build()
                    )
                )
                return@suspendCancellableCoroutine
            }
        }

        registerReceivers(cont)

        startService(
            itp,
            sessionId,
            icon,
            address,
            version,
            imageLength!!,
            textLength = textLength!!,
            subscriptionId = subscriptionId,
        )
    }

    private lateinit var completedSendingBroadcast: BroadcastReceiver
    private lateinit var retrySendingBroadcast: BroadcastReceiver
    fun registerReceivers(
        cont: CancellableContinuation<Result>
    ) {
        val filter = IntentFilter(ITP_SERVICE_COMPLETION)
        completedSendingBroadcast = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                cont.resume(Result.success())
                applicationContext.unregisterReceiver(this)
            }
        }
        ContextCompat.registerReceiver(
            applicationContext,
            completedSendingBroadcast,
            filter,
            ContextCompat.RECEIVER_EXPORTED
        )

        val filterRetry = IntentFilter(ITP_RETRY_SERVICE)
        retrySendingBroadcast = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                try {
                    cont.resume(Result.retry())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        ContextCompat.registerReceiver(
            applicationContext,
            retrySendingBroadcast,
            filterRetry,
            ContextCompat.RECEIVER_EXPORTED
        )
    }

    fun startService(
        itp: ByteArray,
        sessionId: Byte,
        icon: Int,
        address: String,
        version: Byte,
        imageLength: ByteArray,
        textLength: ByteArray,
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
            putExtra(ITP_WORK_MANAGER_UUID, id.toString())
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
        const val ITP_WORK_MANAGER_UUID = "ITP_WORK_MANAGER_UUID"
        const val ITP_IMAGE_LENGTH = "ITP_IMAGE_LENGTH"
        const val ITP_TEXT_LENGTH = "ITP_TEXT_LENGTH"
        const val ITP_SERVICE_ICON = "ITP_SERVICE_ICON"
        const val ITP_STOP_SERVICE = "ITP_STOP_SERVICE"
        const val IMAGE_TRANSMISSION_WORK_MANAGER_TAG = "IMAGE_TRANSMISSION_WORK_MANAGER_TAG"
        const val ITP_SERVICE_COMPLETION = "ITP_IS_SUCCESS"
        const val ITP_RETRY_SERVICE = "ITP_RETRY_SERVICE"
        const val ITP_TRANSMISSION_REQUEST = "ITP_TRANSMISSION_REQUEST"
    }

}