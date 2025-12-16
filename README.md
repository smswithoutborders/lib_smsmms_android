# Lib SMS/MMS Android

## About
Lib SMS/MMS is an Android based library which provides the defaults necessary to transform any app into a default SMS app for Android.
The [requirements](https://developer.android.com/guide/topics/permissions/default-handlers#follow-requirements-default-handlers) to be a default SMS app as put by Google's Playstore includes:
- Ability to send and receive SMS/MMS messages; Receiving MMS does not mean processing it - just the ability for the system to know your app is the receipien of the incoming broadcast.
- Ability to present users with a thread of their incoming/outgoing messages; this is written in the requirements of the playstore documents, but upon submission of your SMS app the Playstore review would send a request for this.

## Features
- Jetpack Compose Navigation Graph (extendable to include custom routes)
  
- Make as default Composable

- Messaging threads View: \
  - Includes search, CRUD, Navigation modals and toolbar dropdowns for settings (extenable), muting etc...

- Search threads View
  
- Conversation Views: \
  - Message composition (typing) view with multi-sim and MMS support \
  - Includes search, CRUD, Contact details view, muting etc..


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

## Implementing within your app
Within your app, you may need to implement some default Composable functionalities of `lib_sms_mms_android`. 

### Navigation
In your main activity handling your navigation, you can utilize the `NavHostControllerInstance(...)` to provide navigation from within your app and the library.

```kotlin
@Composable
fun NavHostControllerInstance(
    newLayoutInfo: WindowLayoutInfo,
    navController: NavHostController,
    threadsViewModel: ThreadsViewModel,
    searchViewModel: SearchViewModel,
    threadsMainMenuItems: (@Composable ((Boolean) -> Unit) -> Unit)? = null,
    customMenuItems: (@Composable ((Boolean) -> Unit) -> Unit)? = null,
    conversationsCustomComposable: (@Composable (CustomsConversationsViewModel?) -> Unit)? = null,
    conversationsCustomViewModel: CustomsConversationsViewModel? = null,
    conversationsCustomDataView: (@Composable (Conversations) -> Unit)? = null,
    modalNavigationModalItems:
    (@Composable ((ThreadsViewModel.InboxType) -> () -> Unit) -> Unit)? = null,
    startDestination: Any = HomeScreenNav(),
    customBottomBar: @Composable (() -> Unit)? = null,
    customThreadsView: @Composable (() -> Unit)? = null,
    showThreadsTopBar: Boolean = true,
    appName: String = stringResource(R.string.lib_app_name),
    builder: NavGraphBuilder.() -> Unit,
) {
    ...
}

// Usage in your main activity

private lateinit var navController: NavHostController

private val threadsViewModel: ThreadsViewModel by viewModels()
private lateinit var searchViewModel: SearchViewModel

AppTheme {
      Surface(Modifier.fillMaxSize()) {
          NavHostControllerInstance(
              newLayoutInfo = newLayoutInfo,
              navController = navController,
              threadsViewModel = threadsViewModel,
              searchViewModel = searchViewModel,
              threadsMainMenuItems = {
                  DropdownMenuItem(
                      text = {
                          Text(
                              text= stringResource(R.string.homepage_menu_routed),
                              color = MaterialTheme.colorScheme.onBackground
                          )
                      },
                      onClick = {
                          navController.navigate(RemoteForwardingScreen)
                          it(false)
                      }
                  ),

                  ...
              },
              customMenuItems = {
                  DropdownMenuItem(
                      text = {
                          Text(
                              text= stringResource(com.afkanerd.deku.DefaultSMS.R.string.secure),
                              color = MaterialTheme.colorScheme.onBackground
                          )
                      },
                      onClick = {
                          secureViewModel.setModal(true)
                      }
                  )
              },
              conversationsCustomViewModel = secureViewModel,
              conversationsCustomComposable = { vm ->
                  ...
              },
              conversationsCustomDataView = { 
                  ...
              }
          ) {
              composable<Screen> {
                  ...
              }
              ...
          }
```

