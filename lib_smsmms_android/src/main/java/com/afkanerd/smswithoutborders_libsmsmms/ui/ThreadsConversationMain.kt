package com.afkanerd.smswithoutborders_libsmsmms.ui

import android.provider.BlockedNumberContract
import android.provider.Telephony
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Unarchive
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState.Loading
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.afkanerd.lib_smsmms_android.R
import com.afkanerd.smswithoutborders_libsmsmms.data.data.models.DateTimeUtils
import com.afkanerd.smswithoutborders_libsmsmms.data.entities.Threads
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.blockContact
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.getNativesLoaded
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.isDefault
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.retrieveContactName
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.setNativesLoaded
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.settingsGetEnableSwipeBehaviour
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.unblockContact
import com.afkanerd.smswithoutborders_libsmsmms.extensions.isScrollingUp
import com.afkanerd.smswithoutborders_libsmsmms.ui.components.DeleteConfirmationAlert
import com.afkanerd.smswithoutborders_libsmsmms.ui.components.ModalDrawerSheetLayout
import com.afkanerd.smswithoutborders_libsmsmms.ui.components.SwipeToDeleteBackground
import com.afkanerd.smswithoutborders_libsmsmms.ui.components.ThreadConversationCard
import com.afkanerd.smswithoutborders_libsmsmms.ui.components.ThreadsNavMenuItems
import com.afkanerd.smswithoutborders_libsmsmms.ui.navigation.ComposeNewMessageScreenNav
import com.afkanerd.smswithoutborders_libsmsmms.ui.navigation.ConversationsScreenNav
import com.afkanerd.smswithoutborders_libsmsmms.ui.navigation.DeveloperModeScreen
import com.afkanerd.smswithoutborders_libsmsmms.ui.navigation.HomeScreenNav
import com.afkanerd.smswithoutborders_libsmsmms.ui.navigation.SearchScreenNav
import com.afkanerd.smswithoutborders_libsmsmms.ui.viewModels.ThreadsViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

data class ThreadsConversationParameters(
    var searchQuery: String? = null,
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class, ExperimentalPermissionsApi::class
)
@Composable
fun ThreadConversationLayout(
    threadsViewModel: ThreadsViewModel,
    navController: NavController,
    foldOpen: Boolean = false,
    threadsMainMenuItems: (@Composable ((Boolean) -> Unit) -> Unit)? = null,
    modalNavigationModalItems:
    (@Composable ((ThreadsViewModel.InboxType) -> () -> Unit) -> Unit)? = null,
    customBottomBar: @Composable (() -> Unit)? = null,
    customThreadsView: @Composable (() -> Unit)? = null,
    showTopBar: Boolean = true,
    appName: String = stringResource(R.string.lib_app_name),
) {
    val inPreviewMode = LocalInspectionMode.current
    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current

    val readPhoneStatePermission =
        rememberPermissionState(requiredReadPhoneStatePermissions)

    var isDefault by remember{ mutableStateOf(inPreviewMode || context.isDefault()) }

    val messagesAreLoading = threadsViewModel.messagesLoading
    val secondaryMessagesAreLoading = threadsViewModel.secondaryMessagesLoading

    var inboxType by remember { mutableStateOf(ThreadsViewModel.InboxType.INBOX )}
    DisposableEffect(lifeCycleOwner) {
        val observer = Observer<ThreadsViewModel.InboxType> { newInboxType ->
            inboxType = newInboxType
        }
        threadsViewModel.selectedInbox.observe(lifeCycleOwner, observer)

        onDispose {
            threadsViewModel.selectedInbox.removeObserver(observer)
        }
    }

    val selectedItems by threadsViewModel.selectedItems.collectAsState()

    val inboxMessagesPagers = threadsViewModel.getThreads(context)
    val archivedMessagesPagers = threadsViewModel.getArchives(context)
    val draftMessagesPagers = threadsViewModel.getDrafts(context)
    val mutedMessagesPagers = threadsViewModel.getIsMute(context)
    val blockedMessagesPager = threadsViewModel.getIsBlocked(context)

    val inboxMessagesItems = inboxMessagesPagers.collectAsLazyPagingItems()
    val archivedMessagesItems = archivedMessagesPagers.collectAsLazyPagingItems()
    val draftMessagesItems = draftMessagesPagers.collectAsLazyPagingItems()
    val mutedMessagesItems = mutedMessagesPagers.collectAsLazyPagingItems()
    val blockedMessagesItems = blockedMessagesPager.collectAsLazyPagingItems()

    val listState = rememberLazyListState()
    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val drawerState = threadsViewModel.drawerState.collectAsState()

    val selectedIconColors = MaterialTheme.colorScheme.primary

    var rememberDeleteMenu by remember { mutableStateOf( false)}

    val scope = rememberCoroutineScope()

    BackHandler(
        inboxType != ThreadsViewModel.InboxType.INBOX ||
                !selectedItems.isEmpty()
    ) {
        if(inboxType != ThreadsViewModel.InboxType.INBOX) {
            threadsViewModel.setInboxType(ThreadsViewModel.InboxType.INBOX)
        }
        else if(!selectedItems.isEmpty()) {
            threadsViewModel.removeAllSelectedItems()
        }
    }

    LaunchedEffect(isDefault) {
        if(!context.getNativesLoaded() && isDefault) {
            threadsViewModel.loadNativesAsync(context) {
                context.setNativesLoaded(true)
            }
        }
    }

    var rememberMenuExpanded by remember { mutableStateOf( false)}

    val displayedInbox = when(inboxType) {
        ThreadsViewModel.InboxType.ARCHIVED -> archivedMessagesItems
        ThreadsViewModel.InboxType.BLOCKED -> blockedMessagesItems
        ThreadsViewModel.InboxType.DRAFTS -> draftMessagesItems
        ThreadsViewModel.InboxType.MUTED -> mutedMessagesItems
        else -> inboxMessagesItems
    }

    ThreadsNavMenuItems(
        navController = navController,
        expanded=rememberMenuExpanded,
        threadsViewModel = threadsViewModel,
        threadMenuItems = threadsMainMenuItems,
    ) {
        rememberMenuExpanded = it
    }

    ModalNavigationDrawer(
        drawerState = drawerState.value,
        drawerContent = {
            ModalDrawerSheetLayout(
                callback = { type ->
                    if(type == ThreadsViewModel.InboxType.DEVELOPER) {
                        navController.navigate(DeveloperModeScreen)
                    } else {
                        threadsViewModel.setInboxType(type)
                    }
                    threadsViewModel.toggleDrawerValue()
                },
                selectedItemIndex = inboxType,
                customComposable = modalNavigationModalItems,
            )
        },
    ) {
        if(customThreadsView != null) {
            customThreadsView()
        }
        else {
            Scaffold (
                modifier = Modifier.nestedScroll(scrollBehaviour.nestedScrollConnection),
                topBar = {
                    if(showTopBar) {

                        if(selectedItems.isEmpty() && inboxType == ThreadsViewModel.InboxType.INBOX) {
                            CenterAlignedTopAppBar(
                                title = {
                                    Text(
                                        text = appName,
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        threadsViewModel.toggleDrawerValue()
                                    }) {
                                        Icon(
                                            imageVector = Icons.Filled.Menu,
                                            contentDescription = stringResource(R.string.open_side_menu)
                                        )
                                    }
                                },
                                actions = {
                                    if(isDefault || inPreviewMode) {
                                        IconButton(onClick = {
                                            navController.navigate(SearchScreenNav())
                                        }) {
                                            Icon(
                                                imageVector = Icons.Filled.Search,
                                                contentDescription = stringResource(R.string.search_messages)
                                            )
                                        }
                                    }
                                    IconButton(onClick = {
                                        rememberMenuExpanded = !rememberMenuExpanded
                                    }) {
                                        Icon(
                                            imageVector = Icons.Filled.MoreVert,
                                            contentDescription = stringResource(R.string.open_menu)
                                        )
                                    }
                                },
                                scrollBehavior = scrollBehaviour
                            )
                        }
                        else if(selectedItems.isNotEmpty()) {
                            TopAppBar(
                                title = {
                                    Text(
                                        text= "${selectedItems.size} ${stringResource(R.string.selected)}",
                                        maxLines =1,
                                        color = selectedIconColors,
                                        overflow = TextOverflow.Ellipsis)
                                },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        threadsViewModel.removeAllSelectedItems()
                                    }) {
                                        Icon(
                                            imageVector = Icons.Filled.Close,
                                            tint = selectedIconColors,
                                            contentDescription = stringResource(R.string.cancel_selection)
                                        )
                                    }
                                },
                                actions = {
                                    IconButton(onClick = {
                                        if(inboxType == ThreadsViewModel.InboxType.ARCHIVED) {
                                            threadsViewModel.update(
                                                context, selectedItems.apply {
                                                    forEach { it.isArchive = false }
                                                })
                                            threadsViewModel.removeAllSelectedItems()
                                        } else {
                                            threadsViewModel.update(context, selectedItems.apply {
                                                forEach { it.isArchive = true }
                                            })
                                            threadsViewModel.removeAllSelectedItems()
                                        }
                                    }) {
                                        if(inboxType == ThreadsViewModel.InboxType.ARCHIVED) {
                                            Icon(
                                                imageVector = Icons.Filled.Unarchive,
                                                tint = selectedIconColors,
                                                contentDescription =
                                                    stringResource(R.string.unarchive_messages)
                                            )
                                        } else {
                                            Icon(
                                                imageVector = Icons.Filled.Archive,
                                                tint = selectedIconColors,
                                                contentDescription =
                                                    stringResource(R.string.messages_threads_menu_archive)
                                            )
                                        }
                                    }

                                    IconButton(onClick = {
                                        rememberDeleteMenu = true
                                    }) {
                                        Icon(
                                            imageVector = Icons.Rounded.Delete,
                                            tint = selectedIconColors,
                                            contentDescription =
                                                stringResource(R.string.message_threads_menu_delete)
                                        )
                                    }

                                    if(selectedItems.size == 1) {
                                        IconButton(onClick = {
                                            val state = inboxType == ThreadsViewModel.InboxType.MUTED
                                                    || selectedItems.first().isMute
                                            threadsViewModel.update(context, selectedItems.apply {
                                                forEach { it.isMute = !state }
                                            }) { threadsViewModel.removeAllSelectedItems() }
                                        }) {
                                            Icon(
                                                imageVector = if(selectedItems.first().isMute)
                                                    Icons.Default.Notifications
                                                else Icons.Default.NotificationsOff,
                                                tint = selectedIconColors,
                                                contentDescription = stringResource(R.string.thread_muted)
                                            )
                                        }
                                    }

                                    IconButton(onClick = {
                                        val state = inboxType == ThreadsViewModel.InboxType.BLOCKED
                                        threadsViewModel.setIsBlocked(
                                            context,
                                            selectedItems.map{ it.address },
                                            !state
                                        ) {
                                            CoroutineScope(Dispatchers.IO).launch {
                                                try {
                                                    if(state) {
                                                        context.unblockContact(selectedItems.map {
                                                            it.address })
                                                    } else {
                                                        context.blockContact(selectedItems.map {
                                                            it.address })
                                                    }
                                                } catch(e: Exception) {
                                                    e.printStackTrace()
                                                    Toast.makeText(
                                                        context,
                                                        "${e.message}",
                                                        Toast.LENGTH_LONG).show()
                                                }
                                                threadsViewModel.removeAllSelectedItems()
                                            }
                                        }
                                    }) {
                                        Icon(
                                            imageVector = if(inboxType != ThreadsViewModel.InboxType.BLOCKED)
                                                Icons.Outlined.Block else Icons.Outlined.Remove,
                                            tint = selectedIconColors,
                                            contentDescription = stringResource(R.string.block_contact)
                                        )
                                    }
                                },
                                scrollBehavior = scrollBehaviour
                            )
                        }
                        else {
                            TopAppBar(
                                title = {
                                    Text(
                                        text= when(inboxType) {
                                            ThreadsViewModel.InboxType.ARCHIVED ->
                                                stringResource(R.string
                                                    .conversations_navigation_view_archived)
                                            ThreadsViewModel.InboxType.BLOCKED ->
                                                stringResource(R.string
                                                    .conversations_navigation_view_blocked)
                                            ThreadsViewModel.InboxType.MUTED ->
                                                stringResource(R.string
                                                    .conversation_menu_muted_label)
                                            ThreadsViewModel.InboxType.DRAFTS ->
                                                stringResource(R.string
                                                    .conversations_navigation_view_drafts)
                                            else -> ""
                                        },
                                        maxLines =1,
                                        overflow = TextOverflow.Ellipsis)
                                },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        if(inboxMessagesItems.loadState.isIdle)
                                            threadsViewModel.toggleDrawerValue()
                                    }) {
                                        Icon(
                                            imageVector = Icons.Filled.Menu,
                                            contentDescription = stringResource(R.string.open_side_menu)
                                        )
                                    }
                                },
                                actions = {},
                                scrollBehavior = scrollBehaviour
                            )
                        }

                    }
                },
                bottomBar = customBottomBar ?: {},
                floatingActionButton = {
                    when(inboxType) {
                        ThreadsViewModel.InboxType.INBOX -> {
                            if((isDefault && !messagesAreLoading) || inPreviewMode) {
                                ExtendedFloatingActionButton(
                                    onClick = {
                                        navController.navigate(
                                            ComposeNewMessageScreenNav())
                                    },
                                    icon = { Icon( Icons.Default.Edit,
                                        stringResource(R.string.compose_new_message)) },
                                    text = { Text(
                                        stringResource(R.string.compose),
                                        fontWeight = FontWeight.SemiBold
                                    ) },
                                    expanded = listState.isScrollingUp(),
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                        else -> {}
                    }
                }
            ) { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding)) {
                    if(secondaryMessagesAreLoading || inPreviewMode)
                        LinearProgressIndicator(Modifier.fillMaxWidth())

                    if(!isDefault || !readPhoneStatePermission.status.isGranted) {
                        DefaultCheckMain { isDefault = context.isDefault() }
                    }
                    if(inPreviewMode || messagesAreLoading) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            LinearProgressIndicator()
                            Text(
                                stringResource(R.string.give_it_a_minute),
                                modifier = Modifier.padding(top=8.dp),
                                fontSize = 12.sp
                            )
                        }
                    }
                    else {
                        Box(
                            modifier = Modifier .fillMaxSize()
                        ) {
                            when(inboxType) {
                                ThreadsViewModel.InboxType.ARCHIVED -> {
                                    if(archivedMessagesItems.loadState.refresh != Loading &&
                                        archivedMessagesItems.itemCount < 1)
                                        Column(
                                            modifier = Modifier.fillMaxSize(),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                stringResource(R.string.homepage_archive_no_message),
                                                fontSize = 24.sp
                                            )
                                        }
                                }
                                else -> {
                                    if(inboxMessagesItems.loadState.refresh != Loading &&
                                        inboxMessagesItems.itemCount < 1)
                                        Column(
                                            modifier = Modifier.fillMaxSize(),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                stringResource(R.string.homepage_no_message),
                                                fontSize = 24.sp
                                            )
                                        }
                                }
                            }

                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                state = listState
                            )  {
                                items(
                                    count = displayedInbox.itemCount,
                                    key = displayedInbox.itemKey { it.threadId }
                                ) { index ->
                                    val thread = displayedInbox[index]

                                    val address = thread!!.address
                                    val isBlocked = if(isDefault)
                                        BlockedNumberContract.isBlocked(context, address)
                                    else false

                                    val contactName = if(isDefault)
                                        context.retrieveContactName(address)
                                    else address

                                    var firstName by remember { mutableStateOf(address) }
                                    var lastName by remember { mutableStateOf("") }

                                    if (!contactName.isNullOrEmpty()) {
                                        contactName.split(" ").let {
                                            firstName = it[0]
                                            if (it.size > 1)
                                                lastName = it[1]
                                        }
                                    }

                                    val date = if(!inPreviewMode) DateTimeUtils.formatDate(
                                        context,
                                        thread.date
                                    ) ?: "" else "Tues"

                                    val offsetX = remember { Animatable(0f) }
                                    val threshold = 300f

                                    Box {
                                        Box(
                                            modifier = Modifier
                                                .matchParentSize()
                                                .background(
                                                    MaterialTheme.colorScheme.background
                                                ),
                                            contentAlignment = Alignment.CenterEnd
                                        ) {
                                            SwipeToDeleteBackground(
                                                inboxType ==
                                                        ThreadsViewModel.InboxType.ARCHIVED,
                                                onArchiveCallback = {
                                                    threadsViewModel.update(
                                                        context,
                                                        listOf(thread.apply {
                                                            this.isArchive = true
                                                        })
                                                    )
                                                    threadsViewModel.removeAllSelectedItems()
                                                },
                                                onDeleteCallback = {
                                                    threadsViewModel.setSelectedItems(listOf(thread))
                                                    rememberDeleteMenu = true
                                                },
                                            )
                                        }

                                        Box(
                                            modifier = Modifier
                                                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                                                .fillMaxSize()
                                                .background(Color.White)
                                                .apply {
                                                    if(context.settingsGetEnableSwipeBehaviour) {
                                                        this.draggable(
                                                            orientation = Orientation.Horizontal,
                                                            state = rememberDraggableState { delta ->
                                                                scope.launch {
                                                                    offsetX.snapTo(offsetX.value + delta)
                                                                }
                                                            },
                                                            onDragStopped = {
                                                                scope.launch {
                                                                    val target = when {
                                                                        offsetX.value < -threshold -> -threshold
                                                                        offsetX.value > threshold -> threshold
                                                                        else -> 0f
                                                                    }
                                                                    offsetX.animateTo(
                                                                        target,
                                                                        animationSpec =
                                                                            spring(dampingRatio =
                                                                                Spring.DampingRatioMediumBouncy)
                                                                    )
                                                                }
                                                            }
                                                        )
                                                    }
                                                }
                                        ) {
                                            ThreadConversationCard(
                                                id = thread.threadId,
                                                firstName = firstName,
                                                lastName = lastName,
                                                phoneNumber = address,
                                                content = thread.snippet,
                                                date = date,
                                                isRead = !thread.unread,
                                                isContact = isDefault && !contactName.isNullOrBlank(),
                                                isBlocked = isBlocked,
                                                modifier = Modifier.combinedClickable(
                                                    onClick = {
                                                        if(selectedItems.isEmpty()) {
                                                            if(!foldOpen) {
                                                                navController.navigate(
                                                                    ConversationsScreenNav(
                                                                        address,
                                                                        threadId = thread.threadId
                                                                    ))
                                                            }
                                                            else {
                                                                navController
                                                                    .navigate(
                                                                        HomeScreenNav( address))
                                                            }
                                                        } else {
                                                            threadsViewModel.setSelectedItems(
                                                                selectedItems.toMutableList().apply {
                                                                    if(selectedItems.contains(thread))
                                                                        remove(thread)
                                                                    else add(thread)
                                                                }
                                                            )
                                                        }
                                                    },
                                                    onLongClick = {
                                                        threadsViewModel.setSelectedItems(
                                                            selectedItems.toMutableList().apply {
                                                                if(selectedItems.contains(thread))
                                                                    remove(thread)
                                                                else add(thread)
                                                            }
                                                        )
                                                    }
                                                ),
                                                isSelected = selectedItems.contains(thread),
                                                isMuted = thread.isMute,
                                                type = thread.type,
                                                unreadCount = thread.unreadCount,
                                                mms = thread.isMms
                                            )
                                        }
                                    }
                                }
                            }

                            if(rememberDeleteMenu) {
                                DeleteConfirmationAlert(
                                    confirmCallback = {
                                        threadsViewModel.deleteThreads(
                                            context,
                                            selectedItems
                                        )
                                        threadsViewModel.removeAllSelectedItems()
                                        rememberDeleteMenu = false
                                    }
                                ) {
                                    rememberDeleteMenu = false
                                    threadsViewModel.removeAllSelectedItems()
                                }
                            }
                        }
                    }
               }
            }

        }
    }

}

@Preview
@Composable
fun PreviewMessageCard() {
    Surface(Modifier.safeDrawingPadding()) {
        val messages: MutableList<Threads> =
            remember { mutableListOf( ) }
        var mms = true
        for(i in 0..10) {
            val thread = Threads(
                threadId = i.toInt(),
                address = "$i",
                snippet = "Hello world: $i",
                date = System.currentTimeMillis(),
                unread = true,
                isMute = true,
                type = Telephony.Sms.MESSAGE_TYPE_SENT,
                conversationId = i.toLong(),
                isArchive = false,
                isMms = mms.apply {
                    mms = !mms
                }
            )
            messages.add(thread)
        }
        val threadsViewModel: ThreadsViewModel = viewModel()
        ThreadConversationLayout(
            threadsViewModel = threadsViewModel,
            navController = rememberNavController(),
        )
    }
}

@Preview
@Composable
fun PreviewMessageCardRemoteListeners_Preview() {
    Surface(Modifier.safeDrawingPadding()) {
        val threadsViewModel: ThreadsViewModel = viewModel()
        ThreadConversationLayout(
            threadsViewModel = threadsViewModel,
            navController = rememberNavController(),
        )
    }
}