package com.afkanerd.smswithoutborders_libsmsmms.services

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
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import androidx.work.ForegroundInfo
import com.afkanerd.lib_smsmms_android.R
import com.afkanerd.smswithoutborders_libsmsmms.data.ImageTransmissionProtocol
import com.afkanerd.smswithoutborders_libsmsmms.data.SmsWorkManager
import com.afkanerd.smswithoutborders_libsmsmms.data.data.models.SmsManager
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.getThreadId
import com.afkanerd.smswithoutborders_libsmsmms.receivers.NotificationActionImpl
import com.afkanerd.smswithoutborders_libsmsmms.ui.viewModels.ConversationsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class ImageTransmissionService : Service() {
    lateinit var dividedPayload: List<ByteArray>

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Implement binder")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent?.hasExtra(SmsWorkManager.ITP_STOP_SERVICE) == true) {
            stopSelf()
            return START_NOT_STICKY
        }

        val address  = intent?.getStringExtra(SmsWorkManager.ITP_TRANSMISSION_ADDRESS)
            ?: return START_NOT_STICKY

        val itpFailed  = intent.getBooleanExtra(SmsWorkManager.ITP_IS_FAILED,
            false)

        val itpRetry  = intent.getBooleanExtra(SmsWorkManager.ITP_IS_RETRY,
            false)

        val itpSuccess  = intent.getBooleanExtra(SmsWorkManager.ITP_IS_SUCCESS,
            false)

        val sessionId  = intent.getByteExtra(SmsWorkManager.ITP_SESSION_ID, -1)

        val icon = intent.getIntExtra(SmsWorkManager.ITP_SERVICE_ICON, -1)
        val subscriptionId = intent.getLongExtra(SmsWorkManager
            .ITP_TRANSMISSION_SUBSCRIPTION_ID, -1)

        if(!itpFailed) {
            CoroutineScope(Dispatchers.Default).launch {
                val payload = ImageTransmissionProtocol.getNextTransmission(
                    applicationContext,
                    sessionId,
                )
                val transmissionIndex = payload.first + 1

                SmsManager(ConversationsViewModel()).sendSms(
                    context = applicationContext,
                    text = TODO("This should be transmitted in b64"),
                    address = address,
                    subscriptionId = subscriptionId,
                    threadId = getThreadId(address),
                    bundle = Bundle()
                ) {
                    CoroutineScope(Dispatchers.Main).launch {
                        try {
                            startForeground(1,
                                createForegroundNotification(
                                    this@ImageTransmissionService,
                                    intent,
                                    icon = icon,
                                    progress = transmissionIndex,
                                    maxProgress = dividedPayload.size
                                ).notification
                            )
                        } catch(e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
        return START_STICKY
    }


    private fun createForegroundNotification(
        context: Context,
        intent: Intent,
        icon: Int,
        maxProgress: Int,
        progress: Int = 0,
    ) : ForegroundInfo {
        val pendingIntent = PendingIntent
            .getActivity(context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE)

        val title = getString(R.string.sending_images)
        val description = getString(R.string.of_sent, progress, maxProgress)

        val builder = NotificationCompat.Builder(context, "4")
            .setContentTitle(title)
            .setContentText(description)
            .setSmallIcon(icon)
            .setOngoing(true)
            .setRequestPromotedOngoing(true)
            .setContentIntent(pendingIntent)
            .setProgress(maxProgress, progress, false).apply {
                getActions(context).forEach {
                    this.addAction(it)
                }
            }

        val notification = builder.build()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(
                0, notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            ForegroundInfo(0, notification)
        }
    }

    private fun getActions(context: Context) : List<NotificationCompat.Action> {
        val stopLabel = getString(R.string.stop)
        val pauseLabel = getString(R.string.pause)

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

        return listOf(
            NotificationCompat.Action.Builder(
                null, // Icon for the reply button
                stopLabel, // Text for the reply button
                stopPendingIntent
            ).setSemanticAction(NotificationCompat.Action.SEMANTIC_ACTION_MUTE).build(),

            NotificationCompat.Action.Builder(
                null, // Icon for the reply button
                pauseLabel, // Text for the reply button
                pausePendingIntent
            ).setSemanticAction(NotificationCompat.Action.SEMANTIC_ACTION_MUTE).build(),
        )
    }

    override fun onTimeout(startId: Int, fgsType: Int) {
        super.onTimeout(startId, fgsType)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        TODO("update sending caches")
    }

}