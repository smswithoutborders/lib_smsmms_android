package com.afkanerd.smswithoutborders_libsmsmms.ui

import android.Manifest
import androidx.compose.foundation.Image
import android.content.Context
import android.net.Uri
import android.provider.Telephony
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.ImageLoader
import coil3.compose.rememberAsyncImagePainter
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import coil3.video.VideoFrameDecoder
import com.afkanerd.lib_smsmms_android.R
import com.afkanerd.smswithoutborders_libsmsmms.data.data.models.DateTimeUtils
import com.afkanerd.smswithoutborders_libsmsmms.data.data.models.SmsManager
import com.afkanerd.smswithoutborders_libsmsmms.data.entities.Conversations
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.blockContact
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.call
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.cancelNotification
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.getDefaultSimSubscription
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.getFileNameFromUri
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.getMimeTypeFromUri
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.getSubscriptionName
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.getThreadId
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.getUriForDrawable
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.isDualSim
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.isShortCode
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.makeE16PhoneNumber
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.retrieveContactName
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.unblockContact
import com.afkanerd.smswithoutborders_libsmsmms.ui.components.ChatCompose
import com.afkanerd.smswithoutborders_libsmsmms.ui.components.ConvenientMethods.deriveMetaDate
import com.afkanerd.smswithoutborders_libsmsmms.ui.components.ConversationCrudBottomBar
import com.afkanerd.smswithoutborders_libsmsmms.ui.components.ConversationPositionTypes
import com.afkanerd.smswithoutborders_libsmsmms.ui.components.ConversationsCard
import com.afkanerd.smswithoutborders_libsmsmms.ui.components.ConversationsMainDropDownMenu
import com.afkanerd.smswithoutborders_libsmsmms.ui.components.DeleteConfirmationAlert
import com.afkanerd.smswithoutborders_libsmsmms.ui.components.FailedMessageOptionsModal
import com.afkanerd.smswithoutborders_libsmsmms.ui.components.MessageInfoAlert
import com.afkanerd.smswithoutborders_libsmsmms.ui.components.SearchCounterCompose
import com.afkanerd.smswithoutborders_libsmsmms.ui.components.SearchTopAppBarText
import com.afkanerd.smswithoutborders_libsmsmms.ui.components.ShortCodeAlert
import com.afkanerd.smswithoutborders_libsmsmms.ui.components.SimChooser
import com.afkanerd.smswithoutborders_libsmsmms.ui.components.getConversationType
import com.afkanerd.smswithoutborders_libsmsmms.ui.navigation.ContactDetailsScreenNav
import com.afkanerd.smswithoutborders_libsmsmms.ui.navigation.HomeScreenNav
import com.afkanerd.smswithoutborders_libsmsmms.ui.navigation.ImageViewScreenNav
import com.afkanerd.smswithoutborders_libsmsmms.ui.navigation.SearchScreenNav
import com.afkanerd.smswithoutborders_libsmsmms.ui.viewModels.ConversationsViewModel
import com.afkanerd.smswithoutborders_libsmsmms.ui.viewModels.CustomsConversationsViewModel
import com.afkanerd.smswithoutborders_libsmsmms.ui.viewModels.ThreadsViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import sh.calvin.autolinktext.rememberAutoLinkText
import kotlin.let

fun backHandler(
    context: Context,
    text: String,
    mmsUri: Uri?,
    address: String,
    subId: Long,
    viewModel: ConversationsViewModel,
    navController: NavController,
    threadId: Int,
) {
    if(text.isNotBlank()) {
        ConversationsViewModel().addDraft(
            context,
            body = text,
            mmsUri = mmsUri,
            address = address,
            subId = subId,
            threadId = threadId,
        ) {}
    }

    if(viewModel.getSelectedItemCount() > 0)
        viewModel.removeAllSelectedItems()
    else if(!navController.popBackStack()) {
        navController.navigate(HomeScreenNav()) {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = true
            }
        }
    }
}

const val requiredSendSMSPermission = Manifest.permission.SEND_SMS
const val requiredReceiveSMSPermission = Manifest.permission.READ_SMS
const val requiredReadPhoneStatePermissions = Manifest.permission.READ_PHONE_STATE

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalPermissionsApi::class
)
@Composable
fun ConversationsMainLayout(
    address: String,
    navController: NavController,
    searchQuery: String? = null,
    text: String = "",
    foldOpen: Boolean = false,
    threadId: Int? = null,
    customComposable: (@Composable (CustomsConversationsViewModel?) -> Unit)? = null,
    customMenuItems: (@Composable ((Boolean) -> Unit) -> Unit)? = null,
    customsConversationsViewModel: CustomsConversationsViewModel? = null,
    _items: List<Conversations>? = null
) {
    var text = text
    val readPhoneStatePermission = rememberPermissionState(requiredReadPhoneStatePermissions)
    if(!readPhoneStatePermission.status.isGranted) {
        navController.navigate(HomeScreenNav())
        return
    }

    val viewModel: ConversationsViewModel = viewModel()
    val context = LocalContext.current
    val inPreviewMode = LocalInspectionMode.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    val scope = rememberCoroutineScope()
    val coroutineScope = remember { CoroutineScope(Dispatchers.Default) }
    val listState = rememberLazyListState()

    val address = context.makeE16PhoneNumber(address)

    val dualSim = if(inPreviewMode) true else context.isDualSim()

    var typingText by remember{ mutableStateOf(text) }
    var typingMmsImage by remember{ mutableStateOf<Uri?>(null) }
    var subscriptionId by remember{ mutableStateOf( context.getDefaultSimSubscription()) }
    var highlightedMessage by remember{ mutableStateOf<Conversations?>(null) }

    var isBlocked by remember { mutableStateOf(viewModel.contactIsBlocked(context, address))}
    var contactName by remember{ mutableStateOf( context.retrieveContactName(address)
        ?: address )}
    var isMute by remember { mutableStateOf(false) }
    var isArchived by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf(searchQuery) }
    var searchIndex by remember { mutableIntStateOf(0) }

    var threadId by remember { mutableIntStateOf(
        if(inPreviewMode) 0 else { threadId ?: context.getThreadId(address) })
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if(event == Lifecycle.Event.ON_DESTROY) {
                if(typingText.isNotBlank()) {
                    ConversationsViewModel().addDraft(
                        context,
                        body = typingText,
                        mmsUri = typingMmsImage,
                        address = address,
                        subId = subscriptionId!!,
                        threadId = threadId,
                    ) {}
                }
            }
            if(event == Lifecycle.Event.ON_RESUME) {
                if(text.isBlank()) {
                    viewModel.fetchDraft(context, threadId) {
                        typingText = it?.sms?.body!!
                        viewModel.clearDraft(context, it)
                        if(searchQuery.isNullOrEmpty()) {
                            scope.launch{
                                listState.animateScrollToItem(0)
                            }
                        }
                    }
                }

                val threadsViewModel = ThreadsViewModel()
                threadsViewModel.get(context, threadId) {
                    it?.let { thread ->
                        threadsViewModel.update(context, listOf(thread.apply {
                            this.unread = false
                        }))
                    }
                }

            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val messages = viewModel.getConversations(context, threadId)

    var showFailedRetryModal by rememberSaveable { mutableStateOf(false) }
    var rememberMenuExpanded by remember{ mutableStateOf(false) }
    var openSimCardChooser by remember { mutableStateOf(inPreviewMode) }
    var searchIndexes by remember { mutableStateOf(emptyList<Int>())}

    val inboxMessagesItems = messages.collectAsLazyPagingItems()

    val selectedItems by viewModel.selectedItems.collectAsState()


    val scrollBehaviour = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    var openAlertDialog by remember { mutableStateOf(false)}

    val isShortCode = if(inPreviewMode) false else isShortCode(address)

    var rememberDeleteAlert by remember { mutableStateOf(false) }

    var openInfoAlert by remember { mutableStateOf(false) }

    val smsManager = SmsManager(customsConversationsViewModel ?: viewModel)

    LaunchedEffect(Unit){
        if(!searchQuery.isNullOrEmpty()) {
            viewModel.search(context, searchQuery!!, threadId=threadId) { indexes ->
                searchIndexes = indexes
            }
        }

        ThreadsViewModel().isMuted(context, threadId) {
            isMute = it
        }

        ThreadsViewModel().isMuted(context, threadId) {
            isArchived = it
        }
    }

    LaunchedEffect(inboxMessagesItems.itemCount) {
        if (inboxMessagesItems.itemCount > 0) {
            listState.animateScrollToItem(0)

            val threadsViewModel = ThreadsViewModel()
            threadsViewModel.get(context, threadId) {
                it?.let { thread ->
                    threadsViewModel.update(context, listOf(thread.apply {
                        this.unread = false
                    }))
                }
            }
        }
    }

    LaunchedEffect(inboxMessagesItems.loadState, searchIndexes) {
        if(inboxMessagesItems.loadState.isIdle) {
            if(searchIndexes.isNotEmpty() && searchIndex == 0) {
                if(inboxMessagesItems.itemCount > searchIndexes.first()) {
                    inboxMessagesItems[searchIndexes.first()]
                    scope.launch {
                        listState.animateScrollToItem(searchIndexes.first(), )
                    }
                }
                else {
                    inboxMessagesItems.refresh()
                }
            }

            context.cancelNotification(threadId)
        }

    }

    BackHandler {
        if(!searchQuery.isNullOrEmpty()) searchQuery = ""
        else backHandler(
            context = context,
            text = typingText,
            mmsUri = typingMmsImage,
            address = address,
            subId = subscriptionId!!,
            viewModel = viewModel,
            navController = navController,
            threadId = threadId,
        )
    }

    ConversationsMainDropDownMenu(
        rememberMenuExpanded,
        isMute = isMute,
        isBlocked = isBlocked,
        isArchived = isArchived,
        searchCallback = {
            navController.navigate(SearchScreenNav(address = address))
        },
        blockCallback = {
            if(isBlocked) {
                ThreadsViewModel().setIsBlocked(context, listOf(address), false) {
                    context.unblockContact(listOf(address))
                    viewModel.removeAllSelectedItems()
                }
                isBlocked = false
            }
            else {
                ThreadsViewModel().setIsBlocked(context, listOf(address), true) {
                    context.blockContact(listOf(address))
                    viewModel.removeAllSelectedItems()
                }
                isBlocked = true
            }
        },
        deleteCallback = {
            rememberDeleteAlert = true
        },
        archiveCallback = {
            if(isArchived) {
                viewModel.unArchive(context, threadId) {}
            }
            else {
                viewModel.archive(context, threadId) {}
            }
            backHandler(
                context,
                typingText,
                typingMmsImage,
                address,
                subscriptionId!!,
                viewModel = viewModel,
                navController=navController,
                threadId = threadId,
            )
        },
        muteCallback = {
            if(isMute) {
                viewModel.unMute(context, threadId) {}
                isMute = false
            } else {
                viewModel.mute(context, threadId) {}
                isMute = true
            }
        },
        customMenuCallbacks = customMenuItems,
    ) {
        rememberMenuExpanded = false
    }

    Scaffold (
        modifier = Modifier
            .safeDrawingPadding()
            .nestedScroll(scrollBehaviour.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    if(searchQuery.isNullOrEmpty()) {
                        TextButton(onClick = {
                            navController.navigate(ContactDetailsScreenNav(address))
                        }) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    if(LocalInspectionMode.current) "Template" else contactName,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                if(isMute || inPreviewMode) {
                                    Spacer(Modifier.padding(4.dp))
                                    Icon(
                                        Icons.Filled.NotificationsOff,
                                        "muted",
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                    else {
                        SearchTopAppBarText(
                            searchQuery!!,
                            cancelCallback = { searchQuery = "" }
                        ) {
                            searchIndexes = emptyList()
                            searchQuery = it
                        }
                    }
                },
                navigationIcon = {
                    if(!foldOpen) {
                        IconButton(onClick = {
                            if(!searchQuery.isNullOrEmpty()) searchQuery = ""
                            else backHandler(
                                context = context,
                                text = typingText,
                                mmsUri = typingMmsImage,
                                address = address,
                                subId = subscriptionId!!,
                                viewModel = viewModel,
                                navController = navController,
                                threadId = threadId,
                            )
                            viewModel.removeAllSelectedItems()
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack,
                                stringResource(R.string.go_back))
                        }
                    }
                },
                actions = {
                    if(searchQuery.isNullOrEmpty()) {
                        if(!isShortCode) {
                            IconButton(onClick = { context.call(address) }) {
                                Icon(
                                    imageVector = Icons.Filled.Call,
                                    contentDescription = stringResource(R.string.call)
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

                    }
                },
                scrollBehavior = scrollBehaviour
            )
        },
        bottomBar = {
            if(selectedItems.isNotEmpty()) {
                ConversationCrudBottomBar(
                    viewModel,
                    navController,
                    onInfoRequested = {
                        openInfoAlert = true
                        highlightedMessage = it
                    },
                    onCompleted = { viewModel.removeAllSelectedItems() }
                ) {
                    viewModel.removeAllSelectedItems()
                }
            }
            else if(!searchQuery.isNullOrEmpty()) {
                SearchCounterCompose(
                    index = (searchIndex + 1).toString(),
                    total=searchIndexes.size.toString(),
                    forwardClick = {
                        if(searchIndex + 1 >= searchIndexes.size)
                            searchIndex = 0
                        else searchIndex += 1
                        scope.launch {
                            if(searchIndexes.size >= searchIndex)
                                listState.animateScrollToItem(
                                    searchIndexes[searchIndex],
                                    0
                                )
                        }
                    },
                    backwardClick = {
                        if(searchIndex - 1 < 0)
                            searchIndex = searchIndexes.size - 1
                        else searchIndex -= 1
                        scope.launch {
                            listState.animateScrollToItem(
                                searchIndexes[searchIndex],
                                0
                            )
                        }
                    }
                )
            }
            else if(isShortCode) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(R.string.conversation_shortcode_description),
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )
                    TextButton(onClick = {
                        openAlertDialog = true
                    }) {
                        Text(
                            stringResource(R.string.conversation_shortcode_action_button),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
            else {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ChatCompose(
                        value = typingText,
                        subscriptionId = subscriptionId!!,
                        simCardChooserCallback = if(dualSim) {
                            { openSimCardChooser = true}
                        } else null,
                        valueChanged = {
                            typingText = it
                        },
                        mmsValueChanged = {
                            typingMmsImage = it
                        },
                        mmsCancelledCallback = {
                            typingMmsImage = null
                        },
                        sendMmsCallback = {
                            viewModel.sendMms(
                                context,
                                it,
                                text = typingText,
                                address = address,
                                subscriptionId = subscriptionId!!,
                                threadId = threadId,
                                filename = context.getFileNameFromUri(it)
                                    ?: System.currentTimeMillis().toString(),
                                mimeType = context.getMimeTypeFromUri(it) ?: "image/jpeg"
                            ){}
                            typingMmsImage = null
                            typingText = ""
                        },
                        smsSendCallback = {
                            smsManager.sendSms(
                                context,
                                text = typingText,
                                address = address,
                                subscriptionId = subscriptionId!!,
                                threadId = threadId,
                                data = null
                            ){}
                            typingText = ""
                        }
                    )

                    if(openSimCardChooser) {
                        SimChooser(
                            expanded = openSimCardChooser,
                            onClickCallback = {
                                subscriptionId = it
                            }
                        ) {
                            openSimCardChooser = false
                        }
                    }
                }
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState,
                reverseLayout = true,
            ) {
                items(
                    count = if(inPreviewMode) _items!!.size else inboxMessagesItems.itemCount,
                    key =  if(inPreviewMode) { index -> _items!![index].id }
                    else inboxMessagesItems.itemKey{ it.id }
                ) { index ->
                    (
                            if(inPreviewMode) _items!![index]
                            else inboxMessagesItems[index]
                    )?.let { conversation ->
                        var showDate = index == 0

                        var timestamp by remember { mutableStateOf(
                            if(inPreviewMode) "1234567"
                            else {
                                DateTimeUtils
                                    .formatDateExtended(context,
                                        conversation.sms?.date!!.toLong())
                            })
                        }

                        var date by remember { mutableStateOf(
                            if(inPreviewMode) "1234567"
                            else { deriveMetaDate(conversation) +
                                    if(dualSim && !inPreviewMode) {
                                        " • " +
                                                context
                                                    .getSubscriptionName(
                                                        subscriptionId!!)
                                    } else ""
                            })
                        }

                        val position = if(!conversation.mms_content_uri.isNullOrEmpty()) {
                            ConversationPositionTypes.END
                        } else getConversationType(
                            index,
                            conversation,
                            inboxMessagesItems.itemSnapshotList.items
                        )

                        var text = if(LocalInspectionMode.current)
                            AnnotatedString(conversation.sms?.body ?: "")
                        else AnnotatedString.rememberAutoLinkText(
                            conversation.sms?.body ?: "",
                            defaultLinkStyles = TextLinkStyles(
                                SpanStyle( textDecoration = TextDecoration.Underline )
                            )
                        )

                        if(!searchQuery.isNullOrEmpty()) {
                            text = buildAnnotatedString {
                                val startIndex = text
                                    .indexOf(searchQuery!!, ignoreCase = true)
                                val endIndex = startIndex + searchQuery!!.length

                                append(text)
                                if (startIndex >= 0) {
                                    addStyle(
                                        style = SpanStyle(
                                            background = Color.Yellow,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black
                                        ),
                                        start = startIndex,
                                        end = endIndex
                                    )
                                }
                            }
                        }

                        ConversationsCard(
                            text= text,
                            timestamp = timestamp,
                            type= conversation.sms?.type!!,
                            status = conversation.sms?.status!!,
                            position = position,
                            date = date,
                            showDate = showDate,
                            mmsContentUri = typingMmsImage,
                            mmsMimeType = conversation.mms_mimetype,
                            mmsFilename = conversation.mms_filename,
                            onClickCallback = {
                                if (selectedItems.isNotEmpty()) {
                                    if (selectedItems.contains(conversation))
                                        viewModel.setSelectedItems(
                                            selectedItems.toMutableList().apply {
                                                this.remove(conversation)
                                            }
                                        )
                                    else
                                        viewModel.setSelectedItems(
                                            selectedItems.toMutableList().apply {
                                                this.add(conversation)
                                            }
                                        )
                                }
                                else if(conversation.sms?.type ==
                                    Telephony.Sms.MESSAGE_TYPE_FAILED) {
                                    highlightedMessage = conversation
                                    showFailedRetryModal = true
                                }
                                else {
                                    if(typingMmsImage != null) {
                                        navController.navigate(ImageViewScreenNav(
                                            contentUri = typingMmsImage.toString(),
                                            address = contactName,
                                            date = date,
                                            filename = conversation.mms_filename
                                                ?: System.currentTimeMillis().toString(),
                                            mimeType = conversation.mms_mimetype ?: "image/jpeg",
                                        ))
                                    } else {
                                        showDate = !showDate
                                    }
                                }
                            },
                            onLongClickCallback = {
                                if (selectedItems.contains(conversation))
                                    viewModel.setSelectedItems(
                                        selectedItems.toMutableList().apply {
                                            this.remove(conversation)
                                        }
                                    )
                                else
                                    viewModel.setSelectedItems(
                                        selectedItems.toMutableList().apply {
                                            this.add(conversation)
                                        }
                                    )
                            },
                            isSelected = selectedItems.contains(conversation),
                        )
                    }
                }
            }

            val showScrollBottom by remember {
                derivedStateOf {
                    listState.firstVisibleItemIndex > 0
                }
            }

            if(showScrollBottom) {
                Button(
                    onClick = {
                        searchQuery = null
                        searchIndexes = emptyList()
                        searchIndex = 0

                        scope.launch { listState.animateScrollToItem(0) }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.outline
                    ),
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomCenter)
                        .clip(CircleShape)
                        .size(50.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(50.dp),
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = stringResource(R.string.down_to_latest_content),
                        tint= MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }

            if(openAlertDialog) {
                ShortCodeAlert {
                    openAlertDialog = false
                }
            }

            if(openInfoAlert && highlightedMessage != null) {
                MessageInfoAlert( highlightedMessage!!) {
                    highlightedMessage = null
                    openInfoAlert = false
                }
            }
        }

        if(showFailedRetryModal) {
            FailedMessageOptionsModal(
                retryCallback = {
                    highlightedMessage?.let { conversation ->
                        viewModel.delete(context, listOf(conversation)) {
                            smsManager.sendSms(
                                context,
                                text = typingText,
                                address = address,
                                subscriptionId = subscriptionId!!,
                                threadId = threadId,
                                data = null
                            ){
                                highlightedMessage = null
                            }
                        }
                    }
                },
                deleteCallback = {
                    highlightedMessage?.let { conversation ->
                        viewModel.delete( context, listOf(conversation)) {
                            highlightedMessage = null
                        }
                    }
                },
            ){
                showFailedRetryModal = false
            }
        }

        if(rememberDeleteAlert) {
            DeleteConfirmationAlert(
                confirmCallback = {
                    coroutineScope.launch {
                        viewModel.deleteThread(context, threadId) {
                            rememberDeleteAlert = false
                            TODO("Navigate back to home")
                        }
                    }
                }
            ) {
                rememberDeleteAlert = false
                viewModel.removeAllSelectedItems()
            }
        }

        customComposable?.invoke(customsConversationsViewModel?.apply {
            setConversationAddress(address)
            setConversationThreadId(threadId)
            setConversationSubscriptionId(subscriptionId)
        })
    }

}

@Preview
@Composable
fun ConversationsMainPreview() {
    ConversationsMainLayout(
        navController = rememberNavController(),
        address = "+1234567",
        _items = emptyList()
    )
}
