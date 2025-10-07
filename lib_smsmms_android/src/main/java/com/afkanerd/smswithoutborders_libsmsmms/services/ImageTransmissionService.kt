package com.afkanerd.smswithoutborders_libsmsmms.services

import android.app.Activity
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import androidx.work.ForegroundInfo
import androidx.work.ListenableWorker
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.afkanerd.lib_smsmms_android.R
import com.afkanerd.smswithoutborders_libsmsmms.data.ImageTransmissionProtocol
import com.afkanerd.smswithoutborders_libsmsmms.data.SmsWorkManager
import com.afkanerd.smswithoutborders_libsmsmms.data.data.models.SmsManager
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.getThreadId
import com.afkanerd.smswithoutborders_libsmsmms.extensions.toByteArray
import com.afkanerd.smswithoutborders_libsmsmms.receivers.NotificationActionImpl
import com.afkanerd.smswithoutborders_libsmsmms.receivers.SmsTextReceivedReceiver
import com.afkanerd.smswithoutborders_libsmsmms.ui.viewModels.ConversationsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.nio.charset.StandardCharsets
import java.util.UUID
import kotlin.uuid.Uuid

class ImageTransmissionService : Service() {
    private lateinit var dividedMessages: MutableList<String>
    private lateinit var messageStateChangedBroadcast: BroadcastReceiver
    private var notificationId: Int = -1

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Implement binder")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        notificationId = getString(R.string.foreground_service_image_transmission_notification_id).toInt()

        if(intent?.hasExtra(SmsWorkManager.ITP_STOP_SERVICE) == true) {
            stopSelf()
            return START_NOT_STICKY
        }

        val payload  = intent?.getByteArrayExtra(SmsWorkManager.ITP_PAYLOAD)
            ?: return START_NOT_STICKY

        val address  = intent.getStringExtra(SmsWorkManager.ITP_TRANSMISSION_ADDRESS)
            ?: return START_NOT_STICKY

        val version  = intent.getByteExtra(SmsWorkManager.ITP_VERSION, -1)
            .also { if(it.toInt() == -1) return START_NOT_STICKY }

        val imageLength  = intent.getShortExtra(SmsWorkManager.ITP_IMAGE_LENGTH,
            -1).also { if(it.toInt() == -1) return START_NOT_STICKY }

        val textLength  = intent.getShortExtra(SmsWorkManager.ITP_TEXT_LENGTH,
            -1).also { if(it.toInt() == -1) return START_NOT_STICKY }

        val sessionId  = intent.getByteExtra(SmsWorkManager.ITP_SESSION_ID, -1)

        val icon = intent.getIntExtra(SmsWorkManager.ITP_SERVICE_ICON, -1)
        val subscriptionId = intent.getLongExtra(SmsWorkManager
            .ITP_TRANSMISSION_SUBSCRIPTION_ID, -1)

        val workId = intent.getStringExtra(SmsWorkManager.ITP_WORK_MANAGER_UUID)

        CoroutineScope(Dispatchers.Default).launch {
            WorkManager.getInstance(applicationContext)
                .getWorkInfoByIdFlow(UUID.fromString(workId))
                .collect { workInfo ->
                    if(workInfo?.state == WorkInfo.State.ENQUEUED) {
                        val notification = createForegroundNotification(
                            intent,
                            icon = icon,
                            progress = 0,
                            maxProgress = dividedMessages.size,
                            isQueue = true
                        ).notification

                        startForeground(notificationId, notification)
                    }

                    if(workInfo?.state == WorkInfo.State.CANCELLED) {
                        stopSelf()
                    }
                }
        }

        handleBroadcast(
            sessionId = sessionId,
            address = address,
            subscriptionId = subscriptionId,
            icon = icon
        )

        dividedMessages = divideImagePayload(
            payload,
            version,
            sessionId,
            imageLength,
            textLength
        ).apply {
            forEachIndexed { index, data ->
                val segNumSeg = ImageTransmissionProtocol
                    .getSegNumberNumberSegment(index, this.size)
                this[index] = data.replaceRange(2, 4,
                    segNumSeg.toHexString())
            }
        }

        val notification = createForegroundNotification(
            intent,
            icon = icon,
            progress = 0,
            maxProgress = dividedMessages.size,
        ).notification

        startForeground(notificationId, notification)

        sendMessage(
            address = address,
            subscriptionId = subscriptionId,
            transmissionIndex = 0
        )

        return START_STICKY
    }

    fun sendMessage(
        address: String,
        subscriptionId: Long,
        transmissionIndex: Int,
    ) {
        SmsManager(ConversationsViewModel()).sendSms(
            context = applicationContext,
            text = dividedMessages[transmissionIndex],
            address = address,
            subscriptionId = subscriptionId,
            threadId = getThreadId(address),
            bundle = Bundle().apply {
                putBoolean(SmsWorkManager.ITP_TRANSMISSION_REQUEST, true)
            }
        ) { }
    }

    private fun createForegroundNotification(
        intent: Intent,
        icon: Int,
        maxProgress: Int,
        progress: Int = 0,
        isRetry: Boolean = false,
        isQueue: Boolean = false
    ) : ForegroundInfo {
        val progress = progress + 1
        val pendingIntent = PendingIntent
            .getActivity(applicationContext,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE)

        val title = when {
            isRetry -> getString(R.string.sending_stop)
            isQueue -> getString(R.string.queued_for_sending)
            else -> getString(R.string.sending_images)
        }

        val description = when {
            isRetry -> getString(R.string.waiting_to_send_of, progress,
                maxProgress)
            isQueue -> getString(R.string.will_resume_sending_shortly)
            else -> getString(R.string.of_sent, progress, maxProgress)
        }

        val builder = NotificationCompat.Builder(applicationContext,
            getString(R.string.foreground_service_image_transmission_channel_id))
            .setContentTitle(title)
            .setContentText(description)
            .setSmallIcon(icon)
            .setOngoing(true)
            .setRequestPromotedOngoing(true)
            .setContentIntent(pendingIntent)
            .setProgress(maxProgress, progress, isQueue).apply {
                if(!isQueue) {
                    getActions(applicationContext, isRetry).forEach {
                        this.addAction(it)
                    }
                }
            }

        val notification = builder.build()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(
                notificationId,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            ForegroundInfo(
                notificationId,
                notification
            )
        }
    }

    private fun getActions(
        context: Context,
        isRetry: Boolean
    ) : List<NotificationCompat.Action> {
        val stopLabel = getString(R.string.stop)
        val pauseLabel = getString(R.string.pause)
        val retryLabel = getString(R.string.retry)

        val notifications = mutableListOf<NotificationCompat.Action>()

        if(isRetry) {
            val retryPendingIntent: PendingIntent = PendingIntent.getBroadcast(
                context,
                2, // Or a unique request code
                Intent(
                    this,
                    NotificationActionImpl::class.java
                ).apply {
                    action = NotificationActionImpl.NOTIFICATION_RETRY_ACTION_INTENT_ACTION
                },
                PendingIntent.FLAG_MUTABLE // Flags for the PendingIntent
            )
            notifications.add(
                NotificationCompat.Action.Builder(
                    null, // Icon for the reply button
                    retryLabel, // Text for the reply button
                    retryPendingIntent
                ).build(),
            )
        } else {
            val stopPendingIntent: PendingIntent = PendingIntent.getBroadcast(
                context,
                0, // Or a unique request code
                Intent(
                    this,
                    NotificationActionImpl::class.java
                ).apply {
                    action = NotificationActionImpl.NOTIFICATION_STOP_ACTION_INTENT_ACTION
                },
                PendingIntent.FLAG_MUTABLE // Flags for the PendingIntent
            )

            val pausePendingIntent: PendingIntent = PendingIntent.getBroadcast(
                context,
                1, // Or a unique request code
                Intent(
                    this,
                    NotificationActionImpl::class.java
                ).apply {
                    action = NotificationActionImpl.NOTIFICATION_PAUSE_ACTION_INTENT_ACTION
                },
                PendingIntent.FLAG_MUTABLE // Flags for the PendingIntent
            )
            notifications.add(
                NotificationCompat.Action.Builder(
                    null, // Icon for the reply button
                    stopLabel, // Text for the reply button
                    stopPendingIntent
                ).build()
            )

            notifications.add(
                NotificationCompat.Action.Builder(
                    null, // Icon for the reply button
                    pauseLabel, // Text for the reply button
                    pausePendingIntent
                ).build(),
            )
        }
        return notifications
    }

    override fun onTimeout(startId: Int, fgsType: Int) {
        super.onTimeout(startId, fgsType)
        stopSelf()
    }

    private fun handleBroadcast(
        sessionId: Byte,
        icon: Int,
        address: String,
        subscriptionId: Long,
    ) {
        val intentFilter = IntentFilter()
        intentFilter.addAction(SmsTextReceivedReceiver.SMS_SENT_BROADCAST_INTENT)
        messageStateChangedBroadcast = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action != null &&
                    intentFilter.hasAction(intent.action) &&
                    intent.hasExtra(SmsWorkManager.ITP_TRANSMISSION_REQUEST)
                ) {
                    if (resultCode == Activity.RESULT_OK) {
                        CoroutineScope(Dispatchers.Default).launch {
                            var transmissionIndex = ImageTransmissionProtocol
                                .getTransmissionIndex(applicationContext, sessionId) ?: return@launch

                            transmissionIndex += 1
                            if(transmissionIndex >= dividedMessages.size) {
                                stopSelf()
                                return@launch
                            }

                            ImageTransmissionProtocol.storeTransmissionSessionIndex(
                                context = applicationContext,
                                sessionId = sessionId,
                                index = transmissionIndex + 1
                            )
                            Thread.sleep(5000)

                            sendMessage(
                                address = address,
                                subscriptionId = subscriptionId,
                                transmissionIndex = transmissionIndex,
                            )
                        }
                    } else {
                        // TODO: could depend on the type of failure
                        CoroutineScope(Dispatchers.Default).launch {
                            val transmissionIndex = ImageTransmissionProtocol
                                .getTransmissionIndex(applicationContext, sessionId) ?: return@launch
                            val notification = createForegroundNotification(
                                intent,
                                icon = icon,
                                progress = transmissionIndex,
                                maxProgress = dividedMessages.size,
                                isRetry = true
                            ).notification

                            try {
                                startForeground( notificationId, notification, )
                            } catch(e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }
        }
        ContextCompat.registerReceiver(
            this,
            messageStateChangedBroadcast,
            intentFilter,
            ContextCompat.RECEIVER_EXPORTED
        )
    }

    /**
     * Message type
     * 	Character limit per message	Bytes for text	Bytes for UDH
     * Single SMS	160	140 bytes	0 bytes
     * Concatenated SMS	153	134 bytes	6 bytes
     */
    @Throws
    private fun divideImagePayload(
        payload: ByteArray,
        version: Byte,
        sessionId: Byte,
        imageLength: Short,
        textLength: Short,
    ): MutableList<String> {
        var encodedPayload = payload
        val standardSegmentSize = 153
        val dividedImage = mutableListOf<String>()

        var segmentNumber = 0
        val segNumberNumberOfSegments: Byte = 0
        do {
            var metaData = version.toHexString() +
                    sessionId.toHexString() +
                    segNumberNumberOfSegments.toHexString()

            if(segmentNumber == 0) {
                metaData += imageLength.toHexString() + textLength.toHexString()
            }

            val size = (standardSegmentSize - metaData.length)
                .coerceAtMost(encodedPayload.size)

            val buffer = metaData + String(encodedPayload.take(size).toByteArray(),
                StandardCharsets.UTF_8)
            if(buffer.length > standardSegmentSize) {
                throw Exception("Buffer size > $standardSegmentSize")
            }
            encodedPayload = encodedPayload.drop(size).toByteArray()

            segmentNumber += 1
            if(segmentNumber >= 256 / 2) {
                throw Exception("Segment number > ${256 /2 }")
            }

            dividedImage.add(buffer)
        } while(encodedPayload.isNotEmpty())

        return dividedImage
    }

    override fun onDestroy() {
        super.onDestroy()
        sendBroadcast(Intent(SmsWorkManager.ITP_SERVICE_COMPLETION))
        if(::messageStateChangedBroadcast.isInitialized) {
            try {
                unregisterReceiver(messageStateChangedBroadcast)
            } catch(e: IllegalArgumentException) {
                e.printStackTrace()
            }
        }
    }

}