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

class SmsWorkManager(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams ) {
    private lateinit var messageStateChangedBroadcast: BroadcastReceiver
    val workValue = MutableStateFlow<Result?>(null)

    override suspend fun doWork(): Result {
        val itp = inputData.getByteArray(ITP_PAYLOAD)
            ?:
            return Result.failure(
                Data.Builder().putString("reason", "ITP_PAYLOAD null").build())

        val address = inputData.getString(ITP_TRANSMISSION_ADDRESS)
            ?: return Result.failure(
                Data.Builder().putString("reason", "ITP_TRANSMISSION_ADDRESS null")
                    .build())

        val subscriptionId = inputData.getLong(ITP_TRANSMISSION_SUBSCRIPTION_ID,
            -1)

        val icon = inputData.getInt(ITP_SERVICE_ICON, -1).also {
            if(it == -1)
                return Result.failure(
                    Data.Builder().putString("reason", "ITP_SERVICE_ICON null").build())
        }

        val version = inputData.getByte(ITP_VERSION, -1).also {
            if(it.toInt() == -1)
                return Result.failure(
                    Data.Builder().putString("reason", "ITP_VERSION null").build())
        }

        val sessionId = inputData.getByte(ITP_SESSION_ID, -1).also {
            if(it.toInt() == -1)
                return Result.failure(
                    Data.Builder().putString("reason", "ITP_SESSION_ID null").build())
        }

        val imageLength = inputData.getByteArray(ITP_IMAGE_LENGTH).also {
            if(it == null)
                return Result.failure(
                    Data.Builder().putString("reason", "ITP_IMAGE_LENGTH null").build())
        }

        val textLength = inputData.getByteArray(ITP_TEXT_LENGTH).also {
            if(it == null)
                return Result.failure(
                    Data.Builder().putString("reason", "ITP_TEXT_LENGTH null").build())
        }

        handleBroadcast(
            sessionId,
            icon = icon,
            address = address,
            subscriptionId = subscriptionId,
        )

        storeSession(
            itp = itp,
            sessionId = sessionId,
            version = version,
            imageLength = imageLength!!.toShortLittleEndian(),
            textLength = textLength!!.toShortLittleEndian()
        )
        startService(sessionId, icon, address, subscriptionId)

        workValue.first { it != null }
        return workValue.value!!
    }

    fun startService(
        sessionId: Byte,
        icon: Int,
        address: String,
        subscriptionId: Long = -1,
        isRetry: Boolean = false,
        isFailed: Boolean = false,
        isSucceeded: Boolean = false
    ) {
        val intent = Intent(
            applicationContext,
            ImageTransmissionService::class.java
        ).apply {
            putExtra(ITP_SESSION_ID, sessionId)
            putExtra(ITP_SERVICE_ICON, icon)
            putExtra(ITP_TRANSMISSION_ADDRESS, address)
            putExtra(ITP_TRANSMISSION_SUBSCRIPTION_ID, subscriptionId)
            putExtra(ITP_IS_FAILED, isFailed)
            putExtra(ITP_IS_RETRY, isRetry)
            putExtra(ITP_IS_SUCCESS, isSucceeded)
            putExtra(ITP_IS_SUCCESS, isSucceeded)
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

        const val ITP_IS_FAILED = "ITP_IS_FAILED"
        const val ITP_IS_RETRY = "ITP_IS_RETRY"
        const val ITP_IS_SUCCESS = "ITP_IS_SUCCESS"
    }

    /**
     *  Multiple incoming broadcast are expected at this point.
     *  Next message should only begin going out if previous one sends.
     */
    private fun handleBroadcast(
        sessionId: Byte,
        icon: Int,
        address: String,
        subscriptionId: Long,
    ) {
        val action = "com.afkanerd.deku.SMS_SENT_BROADCAST_INTENT"
        val intentFilter = IntentFilter()
        intentFilter.addAction(action)
        messageStateChangedBroadcast = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action != null && intentFilter.hasAction(intent.action)) {
                    if (resultCode == Activity.RESULT_OK) {
                        CoroutineScope(Dispatchers.Default).launch {
                            val itp = ImageTransmissionProtocol
                                .getNextTransmission(context, sessionId)
                            if(itp.second == null) {
                                workValue.value = Result.success()
                            } else {
                                ImageTransmissionProtocol
                                    .storeTransmissionSessionIndex(
                                        context = applicationContext,
                                        sessionId = sessionId,
                                        index = itp.first + 1
                                    )
                                Thread.sleep(5000)
                                startService(
                                    sessionId,
                                    icon = icon,
                                    address = address,
                                    subscriptionId = subscriptionId
                                )
                            }
                        }
                    } else {
//                        workValue.value = Result.retry()
                        workValue.value = Result.failure()
                        startService(
                            sessionId,
                            icon = icon,
                            address = address,
                            subscriptionId = subscriptionId,
                            isFailed = true
                        )
                        CoroutineScope(Dispatchers.Main).launch {
                            Toast
                                .makeText(context, "Failed sms: $resultCode",
                                    Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
        ContextCompat.registerReceiver(
            applicationContext,
            messageStateChangedBroadcast,
            intentFilter,
            ContextCompat.RECEIVER_EXPORTED
        )
    }

    suspend fun storeSession(
        itp: ByteArray,
        sessionId: Byte,
        version: Byte,
        imageLength: Short,
        textLength: Short,
    ) {
        if(!ImageTransmissionProtocol.hasTransmissionSession(applicationContext, sessionId)) {
            TODO("update segment number of segments - currently set at 0")
            val dividedPayload = divideImagePayload(
                itp,
                version,
                sessionId,
                imageLength,
                textLength
            )

            ImageTransmissionProtocol
                .storeTransmissionSession(applicationContext, sessionId, dividedPayload)
        }
    }

    @Throws
    private fun divideImagePayload(
        payload: ByteArray,
        version: Byte,
        sessionId: Byte,
        imageLength: Short,
        textLength: Short,
    ): MutableList<ByteArray> {
        val segmentSize: Int = 3
        var encodedPayload = payload
        val standardSegmentSize = 150 * segmentSize
        val dividedImage = mutableListOf<ByteArray>()

        var segmentNumber = 0
        do {
            var metaData = byteArrayOf(version, sessionId, 0,)
            if(segmentNumber == 0) { metaData +=
                imageLength.toByteArray() + textLength.toByteArray() }

            val size = (standardSegmentSize - metaData.size)
                .coerceAtMost(encodedPayload.size)
            val buffer = metaData +  encodedPayload.take(size).toByteArray()
            if(buffer.size > standardSegmentSize) {
                throw Exception("Buffer size > $standardSegmentSize")
            }
            encodedPayload = encodedPayload.drop(buffer.size).toByteArray()

            segmentNumber += 1
            if(segmentNumber >= 256 / 2) {
                throw Exception("Segment number > ${256 /2 }")
            }

            dividedImage.add(buffer)
        } while(encodedPayload.isNotEmpty())

        return dividedImage
    }


}