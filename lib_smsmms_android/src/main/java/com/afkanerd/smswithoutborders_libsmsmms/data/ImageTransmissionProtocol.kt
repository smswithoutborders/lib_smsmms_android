package com.afkanerd.smswithoutborders_libsmsmms.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Operation
import androidx.work.WorkManager
import androidx.work.WorkRequest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.Serializable
import java.util.UUID
import java.util.concurrent.TimeUnit

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "itp_sessions")

@Serializable
data class ImageTransmissionProtocol(
    val version: Byte,
    val sessionId: Byte,
    val segNumber: Int = -1, // nibble
    val numberSegments: Int = -1, // nibble
    val imageLength: Short, // only in first segment
    val textLength: Short, // only in first segment
    val image: ByteArray,
    val text: ByteArray // follows std platform formatting
) {
    fun getSegNumberNumberSegment(segmentNumber: Int): Byte {
        val hi = (segmentNumber and 0x0F) shl 4
        val low = (numberSegments and 0x0F)
        return (hi or low).toByte()
    }

    companion object {
        fun startWorkManager(
            context: Context,
            formattedPayload: ByteArray,
            logo: Int,
            version: Byte,
            sessionId: Byte,
            imageLength: ByteArray,
            textLength: ByteArray,
            address: String,
            subscriptionId: Long,
        ): Operation {
            val constraints : Constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

            val workManager = WorkManager.getInstance(context)

            fun generateUuidFromLong(input: Long): UUID {
                // Generate a UUID from the long by using the input directly
                // for the most significant bits and setting the least significant bits to 0.
                val mostSigBits = input
                val leastSigBits = 0L // You can modify this if you want to use more of the long

                return UUID(mostSigBits, leastSigBits)
            }

            val remoteListenersListenerWorker = OneTimeWorkRequestBuilder<SmsWorkManager>()
                .setConstraints(constraints)
                .setId(generateUuidFromLong(System.currentTimeMillis()))
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    WorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .setInputData(Data.Builder()
                    .putByteArray(SmsWorkManager.ITP_PAYLOAD, formattedPayload)
                    .putInt(SmsWorkManager.ITP_SERVICE_ICON, logo)
                    .putByte(SmsWorkManager.ITP_VERSION, version)
                    .putByte(SmsWorkManager.ITP_SESSION_ID, sessionId)
                    .putByteArray(SmsWorkManager.ITP_IMAGE_LENGTH, imageLength)
                    .putByteArray(SmsWorkManager.ITP_TEXT_LENGTH, textLength)
                    .putString(SmsWorkManager.ITP_TRANSMISSION_ADDRESS, address)
                    .putLong(SmsWorkManager.ITP_TRANSMISSION_SUBSCRIPTION_ID, subscriptionId)
                    .build()
                )
                .addTag(SmsWorkManager.IMAGE_TRANSMISSION_WORK_MANAGER_TAG)
                .build();

            return workManager.enqueueUniqueWork(
                "$SmsWorkManager.IMAGE_TRANSMISSION_WORK_MANAGER_TAG.${
                    System.currentTimeMillis()}",
                ExistingWorkPolicy.KEEP,
                remoteListenersListenerWorker
            )

        }

        suspend fun getNextTransmission(
            context: Context,
            sessionId: Byte,
        ) : Pair<Int, ByteArray?> {
            val index = getNextTransmissionIndex(context, sessionId) + 1
            val key = byteArrayPreferencesKey("session_data_$sessionId:$index")
            return Pair(index, context.dataStore.data.first()[key])
        }

        suspend fun hasTransmissionSession(
            context: Context,
            sessionId: Byte,
        ) : Boolean {
            val key = intPreferencesKey("session_index_$sessionId")
            return context.dataStore.data.firstOrNull()?.get(key) != null
        }

        suspend fun getNextTransmissionIndex(
            context: Context,
            sessionId: Byte,
        ) : Int {
            val key = intPreferencesKey("session_index_$sessionId")
            return context.dataStore.data.first()[key]!!
        }

        /**
         * The index store
         */
        suspend fun storeTransmissionSessionIndex(
            context: Context,
            sessionId: Byte,
            index: Int,
        ) {
            val key = intPreferencesKey("session_index_$sessionId")
            context.dataStore.edit { session ->
                session[key] = index
            }
        }

        suspend fun storeTransmissionSession(
            context: Context,
            sessionId: Byte,
            data: List<ByteArray>
        ) {
            data.forEachIndexed { index, segment ->
                val key = byteArrayPreferencesKey("session_data_$sessionId:$index")
                context.dataStore.edit { session ->
                    session[key] = segment
                }
            }
        }

        suspend fun getItpSession(context: Context) : Int {
            val sessionId = intPreferencesKey("session_id")
            context.dataStore.edit { session ->
                val currentSession = session[sessionId] ?: 0
                session[sessionId] = if(currentSession >=255) 0 else currentSession + 1
            }
            return context.dataStore.data.first()[sessionId]!!
        }

    }

}
