# Lib SMS/MMS Android

## About
Lib SMS/MMS is an Android based library which provides the defaults necessary to transform any app into a default SMS app for Android.
The [requirements](https://developer.android.com/guide/topics/permissions/default-handlers#follow-requirements-default-handlers) to be a default SMS app as put by Google's Playstore includes:
- Ability to send and receive SMS/MMS messages; Receiving MMS does not mean processing it - just the ability for the system to know your app is the receipien of the incoming broadcast.
- Ability to present users with a thread of their incoming/outgoing messages; this is written in the requirements of the playstore documents, but upon submission of your SMS app the Playstore review would send a request for this.
- 


## Dependencies
While `lib_sms_mms` provides the [default permissions](https://developer.android.com/guide/topics/permissions/default-handlers#follow-requirements-default-handlers) required to be a default SMS messaging app for Android, the developer would need to handle configuration of the following.

The library broadcast incoming messages (including delivery reports) using the - add this receiver in your app and handle the incoming broadcast
- `action android:name="com.afkanerd.deku.SMS_SENT_BROADCAST_INTENT_LIB"`
- `action android:name="com.afkanerd.deku.NOTIFICATION_REPLY_ACTION_INTENT_ACTION_REPLAY"`


```gradle
<receiver
    android:name=".receivers.SmsMmsNotificationReceiver"
    android:exported="false">
    <intent-filter>
        <action android:name="com.afkanerd.deku.SMS_SENT_BROADCAST_INTENT_LIB" />
        <action android:name="com.afkanerd.deku.NOTIFICATION_REPLY_ACTION_INTENT_ACTION_REPLAY" />
    </intent-filter>
</receiver>

...
```

## Parsing incoming messages
When the lib_sms_mms receives an incoming message, it processes the message and stores in Conversations Database. The incoming broadcast is rebroadcasted with the actions above which handled and provide notifications for the users.

The notification can be done with `context.notify(...)`.

The following is an example of what your `.receiver.SmsMmsNotificationReceiver` file or any other handling file could look like...

```kotlin

/**
id: Long  => conversation id of the incoming conversation.
self: Boolean => if this broadcast has been generated from within the app itself, such as replying with notifications.
type: String => Representing a  `NotificationTxType` as a String. Determines if text or data message is being sent.
**/

override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action) {
            SmsTextReceivedReceiver.SMS_SENT_BROADCAST_INTENT_LIB -> {
                val id = intent.getLongExtra("id", -1)
                val self = intent.getBooleanExtra("self", false)
                val type = intent.getStringExtra("type")
                CoroutineScope(Dispatchers.IO).launch {
                    context?.getDatabase()?.conversationsDao()?.getConversation(id)?.let { conversation ->
                    ...

                    if(type == NotificationTxType.DATA.name) {
                        // The incoming SMS is a data type
                        ...
                    }
                    else {
                        // The incoming SMS is a text type
                        ...
                    }

                }

                ....
```
