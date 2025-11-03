package com.afkanerd.smswithoutborders_libsmsmms.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Telephony
import android.telephony.SmsManager
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.SimCard
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import coil3.video.VideoFrameDecoder
import com.afkanerd.lib_smsmms_android.R
import com.afkanerd.smswithoutborders_libsmsmms.data.entities.Conversations
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.copyItemToClipboard
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.getSimCardInformation
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.getSubscriptionBitmap
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.getUriForDrawable
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.shareItem
import com.afkanerd.smswithoutborders_libsmsmms.ui.navigation.ComposeNewMessageScreenNav
import com.afkanerd.smswithoutborders_libsmsmms.ui.viewModels.ConversationsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun SearchCounterCompose(
    index: String = "0",
    total: String = "10",
    forwardClick: (() -> Unit)? = null,
    backwardClick: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "$index/$total ${stringResource(R.string.results_found)}",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(Modifier.weight(4f))
            IconButton(onClick = {
                forwardClick?.let{ it() }
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBackIos,
                    contentDescription = stringResource(R.string.move_search_backwards),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            IconButton(onClick = {
                backwardClick?.let{ it() }
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowForwardIos,
                    contentDescription = stringResource(R.string.move_search_forwards),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopAppBarText(
    searchQuery: String = "",
    cancelCallback: (() -> Unit)? = null,
    searchCallback: ((String) -> Unit)? = null,
) {
    var searchQuery by remember { mutableStateOf(searchQuery) }
    val interactionsSource = remember { MutableInteractionSource() }

    BasicTextField(
        value = searchQuery,
        onValueChange = {
            searchQuery = it
            if(it.isEmpty())
                cancelCallback?.let{ it() }
            else searchCallback?.let{ it(searchQuery)}
        },
        maxLines = 7,
        singleLine = false,
        textStyle = TextStyle(color= MaterialTheme.colorScheme.onBackground),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
        modifier = Modifier.fillMaxWidth()
    ) {
        TextFieldDefaults.DecorationBox(
            value = searchQuery,
            visualTransformation = VisualTransformation.None,
            innerTextField = it,
            singleLine = false,
            enabled = true,
            interactionSource = interactionsSource,
            trailingIcon = {
                IconButton(onClick = {
                    searchQuery = ""
                    cancelCallback?.let{ it() }
                }) {
                    Icon(Icons.Default.Close, stringResource(R.string.cancel_search))
                }
            },
            placeholder = {
                Text(
                    text= stringResource(R.string.text_message),
                    color = MaterialTheme.colorScheme.outline
                )
            },
            shape = RoundedCornerShape(24.dp, 24.dp, 24.dp, 24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
            ),
        )
    }
}


@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ChatCompose(
    value: String,
    valueChanged: ((String) -> Unit)? = null,
    mmsValueChanged: ((Uri) -> Unit)? = null,
    mmsCancelledCallback: (() -> Unit)? = null,
    subscriptionId: Long = -1,
    sendMmsCallback: (Uri) -> Unit,
    simCardChooserCallback: (() -> Unit)? = null,
    smsSendCallback: () -> Unit
) {
    val context = LocalContext.current
    val inPreviewMode = LocalInspectionMode.current
    val interactionsSource = remember { MutableInteractionSource() }

    var imageUri: Uri? by remember { mutableStateOf(null) }
    val imagePicker = mmsImagePicker { uri ->
        val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
        context.contentResolver.takePersistableUriPermission(uri, flag)
        mmsValueChanged?.invoke(uri)
        imageUri = uri
    }

    var messagingType by remember { mutableStateOf("SMS") }

    LaunchedEffect(imageUri) {
        messagingType = if(imageUri != null) "MMS" else "SMS"
    }

    Column(
        modifier = Modifier
            .imePadding()
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .padding(start = 8.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        Row( Modifier.fillMaxWidth()) {
            Row(Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(
                    24.dp,
                    24.dp,
                    24.dp,
                    24.dp)
                )
                .background(MaterialTheme.colorScheme.outlineVariant),
            ) {
                Column(
                    modifier=Modifier
                        .padding(8.dp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(onClick = {
//                    imagePicker.launch(arrayOf("*/*"))
                        imagePicker.launch(arrayOf("image/png", "image/jpg", "image/jpeg"))
                    }) {
                        Icon(
                            Icons.Outlined.AddCircleOutline,
                            stringResource(R.string.send_mms_photo),
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                Column(
                    modifier=Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                ) {
                    if(imageUri != null || inPreviewMode) {
                        ComposeMmsImage(imageUri) {
                            imageUri = null
                            mmsCancelledCallback?.invoke()
                        }
                    }

                    BasicTextField(
                        value = value,
                        onValueChange = {
                            valueChanged?.invoke(it)
                        },
                        maxLines = 7,
                        singleLine = false,
                        textStyle = TextStyle(
                            color= MaterialTheme.colorScheme.onBackground,
                            fontSize = 16.sp
                        ),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
                    ) {
                        TextFieldDefaults.DecorationBox(
                            value = value,
                            visualTransformation = VisualTransformation.None,
                            innerTextField = it,
                            singleLine = false,
                            enabled = true,
                            interactionSource = interactionsSource,
                            placeholder = {
                                Text(
                                    text= stringResource(R.string.text_message),
                                    color = MaterialTheme.colorScheme.outline
                                )
                            },
                            shape = RoundedCornerShape(24.dp, 24.dp, 24.dp, 24.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                            ),
                        )
                    }

                    if(value.isNotBlank() || inPreviewMode) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            val length = if(inPreviewMode) "10/140"
                            else getSMSCount(
                                context,
                                value,
                                subscriptionId,
                            )
                            Text(
                                length,
                                color= MaterialTheme.colorScheme.secondary,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxHeight(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.End
                ) {
                    if(simCardChooserCallback != null || inPreviewMode) {
                        IconButton(
                            onClick = { simCardChooserCallback!!() },
                        ) {
                            if(LocalInspectionMode.current) {
                                Icon(
                                    Icons.Outlined.SimCard,
                                    stringResource(R.string.send_message),
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.size(25.dp)
                                )
                            } else {
                                if(subscriptionId > -1) {
                                    context.getSubscriptionBitmap(
                                        subscriptionId.toInt())
                                        ?.asImageBitmap()?.let { image ->
                                            Image(
                                                image,
                                                stringResource(R.string.choose_sim_card),
                                                modifier = Modifier.size(25.dp)
                                            )
                                        }
                                }
                            }
                        }

                    }
                }
            }

            if(value.isNotBlank() || imageUri != null || LocalInspectionMode.current) {
                Column(
                    Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = {
                            if(imageUri != null) {
                                sendMmsCallback(imageUri!!)
                            } else {
                                smsSendCallback.invoke()
                            }
                            imageUri = null
                        },
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.outlineVariant),
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Spacer(Modifier.weight(1f))
                            Icon(
                                Icons.AutoMirrored.Default.Send,
                                stringResource(R.string.send_message),
                                tint = MaterialTheme.colorScheme.onBackground,
                            )
                            Text(
                                messagingType,
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 8.sp
                            )
                        }
                    }
                }
            }
        }

    }
}

fun getSMSCount(
    context: Context,
    text: String?,
    subscriptionId: Long,
): String {
    val smsManager = if(Build.VERSION.SDK_INT > Build.VERSION_CODES.Q)
        context.getSystemService(SmsManager::class.java)
    else SmsManager.getSmsManagerForSubscriptionId(subscriptionId.toInt())

    if(text.isNullOrBlank()) return ""
    val messages = smsManager.divideMessage(text)
    val segmentCount = messages[messages.size - 1].length
    return segmentCount.toString() + "/" + messages.size
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FailedMessageOptionsModal(
    retryCallback: (() -> Unit)? = null,
    deleteCallback: (() -> Unit)? = null,
    dismissCallback: (() -> Unit)? = null
) {
    val scope = rememberCoroutineScope()
    val state = rememberStandardBottomSheetState(
        initialValue = if(LocalInspectionMode.current) SheetValue.Expanded else SheetValue.Hidden,
        skipHiddenState = false
    )

    ModalBottomSheet(
        onDismissRequest = { dismissCallback?.invoke() },
        sheetState = state,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            TextButton(
                onClick = {
                    retryCallback?.invoke()
                    scope
                        .launch{
                            state.hide()
                        }
                        .invokeOnCompletion {
                            dismissCallback?.invoke()
                        }
                },
                modifier = Modifier.align(Alignment.Start),
            ) {
                Icon(
                    Icons.AutoMirrored.Default.Send,
                    stringResource(R.string.resend_message),
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(end=8.dp),
                )

                Text(stringResource(R.string.resend_message))
            }

            TextButton(
                onClick = {
                    deleteCallback?.invoke()
                    scope
                        .launch{
                            state.hide()
                        }
                        .invokeOnCompletion {
                            dismissCallback?.invoke()
                        }
                },
                modifier = Modifier.align(Alignment.Start),
            ) {
                Icon(
                    Icons.Filled.Delete,
                    stringResource(R.string.delete_message),
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(end=8.dp),
                )

                Text(stringResource(R.string.delete_message1))
            }
        }
    }
}

@Composable
fun ShortCodeAlert(
    dismissCallback: (() -> Unit)? = null
) {
    val context = LocalContext.current
    AlertDialog(
        backgroundColor = MaterialTheme.colorScheme.primary,
        text = {
            Text(
                context.getString(R.string.conversation_shortcode_learn_more_text),
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        onDismissRequest = { dismissCallback?.invoke() },
        confirmButton = {},
        dismissButton = {
            TextButton(
                onClick = { dismissCallback?.invoke() }
            ) {
                Text(
                    context.getString(R.string.conversation_shortcode_learn_more_ok),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    )
}

@Composable
fun MessageInfoAlert(
    conversation: Conversations,
    dismissCallback: (() -> Unit)? = null,
) {
    val type = stringResource(R.string.text_message_1)
    val priority = stringResource(R.string.normal)
    AlertDialog(
        backgroundColor = MaterialTheme.colorScheme.primary,
        title = {
            Text(
                stringResource(R.string.message_details),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        text = {
            Column {
                Text(
                    stringResource(R.string.type, type),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    stringResource(R.string.priority, priority),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                if(conversation.sms?.type == Telephony.Sms.MESSAGE_TYPE_INBOX)
                    Text(
                        stringResource(R.string.from, conversation.sms?.address ?: ""),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                Text(
                    stringResource(
                        R.string.to, if (conversation.sms?.type == Telephony.Sms.MESSAGE_TYPE_OUTBOX)
                            conversation.sms?.address ?: ""
                        else "N/A"
                    ),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    stringResource(
                        R.string.sent, if (conversation.sms?.type == Telephony.Sms.MESSAGE_TYPE_OUTBOX)
                            formatDate(conversation.sms?.date?.toLong() ?: 0L)
                        else formatDate(conversation.sms?.date_sent?.toLong() ?: 0L)
                    ),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                if(conversation.sms?.type == Telephony.Sms.MESSAGE_TYPE_INBOX)
                    Text(
                        stringResource(
                            R.string.received,
                            formatDate(conversation.sms?.date?.toLong() ?: 0L)
                        ),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
            }
        },
        onDismissRequest = { dismissCallback?.invoke() },
        confirmButton = {},
        dismissButton = {
            TextButton(
                onClick = { dismissCallback?.invoke() }
            ) {
                Text(
                    stringResource(R.string.close),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    )
}

fun formatDate(timestamp: Long): String {
    val date = Date(timestamp)
    val format = SimpleDateFormat("yyyy-MM-dd HH:ss", Locale.getDefault())
    return format.format(date)
}


@Composable
fun SimChooser(
    expanded: Boolean = LocalInspectionMode.current,
    onClickCallback: ((Long) -> Unit)? = null,
    dismissCallback: (() -> Unit)? = null
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.BottomCenter)
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { dismissCallback?.invoke() },
        ) {
            if(!LocalInspectionMode.current)
            context.getSimCardInformation()?.forEach {
                DropdownMenuItem(
                    leadingIcon = {
                        if(LocalInspectionMode.current) {
                            Icon(
                                Icons.Outlined.SimCard,
                                stringResource(R.string.send_message),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        } else {
                            context.getSubscriptionBitmap(it.subscriptionId)
                                ?.asImageBitmap()?.let { image ->
                                    Image(image, stringResource(R.string.choose_sim_card))
                                }
                        }
                    },
                    text = {
                        Text(
                            text = it.carrierName.toString(),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    onClick = {
                        onClickCallback?.invoke(it.subscriptionId.toLong())
                        dismissCallback?.invoke()
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun ConversationCrudBottomBar(
    viewModel: ConversationsViewModel = ConversationsViewModel(),
    navController: NavController? = null,
    onInfoRequested: (Conversations) -> Unit = {},
    onCompleted: (() -> Unit)? = null,
    onCancel: (() -> Unit)? = null,
) {
    val context = LocalContext.current
    val selectedItems by viewModel.selectedItems.collectAsState()

    BottomAppBar (
        actions = {
            Row {
                IconButton(onClick = {
                    CoroutineScope(Dispatchers.Default).launch {
                        onCancel?.let { it() }
                    }
                }) {
                    Icon(Icons.Default.Close,
                        stringResource(R.string.cancel_selected_messages))
                }

                Text(
                    selectedItems.size.toString(),
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )

                Spacer(Modifier.weight(1f))

                if(selectedItems.size < 2) {
                    IconButton(onClick = {
                        val conversations = viewModel.selectedItems.value.first()
                        onInfoRequested(conversations)
                    }) {
                        Icon(Icons.Filled.Info,
                            stringResource(R.string.message_information))
                    }

                    IconButton(onClick = {
                        val conversation = viewModel.selectedItems.value.first()
                        context.copyItemToClipboard(conversation.sms?.body!!)
                        onCompleted?.invoke()
                    }) {
                        Icon(Icons.Filled.ContentCopy,
                            stringResource(R.string.copy_message))
                    }

                    IconButton(onClick = {
                        selectedItems.firstOrNull()?.let { conversation ->
                            navController?.navigate(ComposeNewMessageScreenNav(
                                text = conversation.sms?.body,
                                subscriptionId = conversation.sms?.sub_id
                            ))
                        }
                    }) {
                        Icon(painter= painterResource(id= R.drawable.rounded_forward_24),
                            stringResource(R.string.forward_message)
                        )
                    }

                    IconButton(onClick = {
                        val conversation = viewModel.selectedItems.value.first()
                        context.shareItem(conversation.sms?.body!!)
                        onCompleted?.invoke()
                    }) {
                        Icon(Icons.Filled.Share,
                            stringResource(R.string.share_message))
                    }
                }

                IconButton(onClick = {
                    val conversation = viewModel.selectedItems.value.first()
                    viewModel.delete(context, listOf(conversation)) {
                        onCompleted?.invoke()
                    }
                }) {
                    Icon(Icons.Filled.Delete, stringResource(R.string.delete_message))
                }
            }

        }
    )
}



@Preview(showBackground = true)
@Composable
fun SearchCounterComposePreview() {
    SearchCounterCompose(
        index = "5",
        total = "20",
        forwardClick = {},
        backwardClick = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun SearchTopAppBarTextPreview() {
    SearchTopAppBarText(
        searchQuery = "Sample search",
        cancelCallback = {},
        searchCallback = {}
    )
}

@RequiresExtension(extension = Build.VERSION_CODES.UPSIDE_DOWN_CAKE, version = 15)
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Preview(showBackground = true)
@Composable
fun ChatComposePreview() {
    ChatCompose(
        value = "Hello there!",
        sendMmsCallback = {}
    ) {

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Failed Message Modal Light")
@Preview(showBackground = true, name = "Failed Message Modal Dark", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FailedMessageOptionsModalPreview() {
    FailedMessageOptionsModal(
        retryCallback = {},
        deleteCallback = {},
        dismissCallback = {}
    )
}

@Preview(showBackground = true, name = "Short Code Alert Light")
@Preview(showBackground = true, name = "Short Code Alert Dark", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ShortCodeAlertPreview() {
    ShortCodeAlert(dismissCallback = {})
}

@Preview(showBackground = true, name = "SIM Chooser Light")
@Preview(showBackground = true, name = "SIM Chooser Dark", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SimChooserPreview() {
    SimChooser(
        expanded = true, // Make it visible in preview
        onClickCallback = {},
        dismissCallback = {}
    )
}

//@Preview(showBackground = true, name = "SIM Chooser Light")
//@Preview(showBackground = true, name = "SIM Chooser Dark", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun MessageInfoAlert_Preview() {
//    AppTheme {
//        val conversation = Conversation()
//        conversation.sms?.address = "+2371234567"
//        conversation.date = System.currentTimeMillis().toString()
//        conversation.date_sent = System.currentTimeMillis().toString()
//        conversation.sms?.type = Telephony.Sms.MESSAGE_TYPE_INBOX
//        MessageInfoAlert(
//            conversation
//        ) {}
//    }
//}

@Composable
fun mmsImagePicker(
    callback: (Uri) -> Unit
): ManagedActivityResultLauncher<Array<String>, Uri?> {
    // Registers a photo picker activity launcher in single-select mode.
    return rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            callback(uri)
        } else {
        }
    }
}

@Composable
fun ComposeMmsImage(
    uri: Uri?,
    onCloseCallback: () -> Unit,
) {
    val size = 90.dp
    val padding = 12.dp
    val shape = RoundedCornerShape(24.dp, 24.dp,
        24.dp, 24.dp)

    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier.padding(8.dp)
    ) {
        Row {
            Column {
                if(LocalInspectionMode.current) {
                    Image(
                        painter = painterResource(R.drawable.github_mark),
                        contentDescription = stringResource(R.string.mms_selected_image),
                        modifier = Modifier
                            .padding(padding)
                            .size(size)
                            .clip(shape),
                        contentScale = ContentScale.Crop,
                    )
                }
                else {
                    AsyncImage(
                        model = uri,
                        contentDescription = stringResource(R.string.mms_selected_image),
                        modifier = Modifier
                            .padding(padding)
                            .size(size)
                            .clip(shape),
                        contentScale = ContentScale.Crop,
                    )
                }
            }
            Column {
                IconButton(onClick = {
                    onCloseCallback()
                }) {
                    Icon(Icons.Filled.Close, stringResource(R.string.delete_message))
                }
            }
        }
    }
}

@Composable
fun MmsContentView(
    contentUri: Uri,
    mimeType: String,
    filename: String?,
    isSelected: Boolean,
    type: Int,
    isSending: Boolean = false,
    onClickCallback: (() -> Unit)?,
    onLongClickCallback: (() -> Unit)?,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = if(isSending) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize(if(isSending)
                    Alignment.CenterEnd else Alignment.CenterStart),
        ) {
            when {
                mimeType.contains("image") -> {
                    AsyncImage(
                        model = contentUri,
                        contentDescription = stringResource(R.string.mms_image),
                        contentScale = ContentScale.Crop,
                        modifier = if(LocalInspectionMode.current) Modifier else Modifier
                            .combinedClickable(
                                onClick = {
                                    onClickCallback?.let { it() }
                                },
                                onLongClick ={
                                    onLongClickCallback?.let { it() }
                                }
                            )
                            .size(200.dp)
                            .aspectRatio(1f)  // This ensures a square aspect ratio
                            .clip(RoundedCornerShape(10.dp))
                    )
                }
                mimeType.contains("video") -> {
                    val imageLoader = ImageLoader.Builder(LocalContext.current)
                        .components {
                            add(VideoFrameDecoder.Factory())
                        }
                        .build()

                    val painter = rememberAsyncImagePainter(
                        model = contentUri,
                        imageLoader = imageLoader,
                    )

                    Image(
                        painter = painter,
                        contentDescription = stringResource(R.string.mms_video),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(200.dp)
                            .aspectRatio(1f)  // This ensures a square aspect ratio
                            .clip(RoundedCornerShape(10.dp)),
                    )
                }
                else -> {
                    val inPreview = LocalInspectionMode.current
                    val filename by remember{
                        mutableStateOf(if(inPreview) "filename.txt" else filename)
                    }
                    Card {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // TODO:
//                            Icon(painterResource(R.drawable.ic_alert), "")
//                            filename?.let {
//                                Text(
//                                    it,
//                                    modifier = Modifier.padding(start=16.dp)
//                                )
//                            }
                        }
                    }
                }
            }

            if(isSelected) {
                Surface(
                    color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f),
                    modifier = Modifier
                        .matchParentSize()
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    // Optional: Add content on top of the overlay if needed
                    // Text("Overlay Text", color = Color.White)
                }
            }
        }

        if(LocalInspectionMode.current || type == Telephony.Sms.MESSAGE_TYPE_FAILED) {
            Column(modifier = Modifier
                .align(Alignment.CenterVertically)) {
                IconButton(onClick = {}) {
                    Icon(
                        Icons.Default.Info,
                        "Message failed icon",
//                        tint= colorResource(R.color.design_default_color_error)
                        tint= MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewMmsImage_Image() {
    val context = LocalContext.current
    Column {
        MmsContentView(
            context.getUriForDrawable(R.drawable.github_mark)!!,
            "image/jpeg",
            "demo.txt",
            true,
            Telephony.Mms.MESSAGE_BOX_SENT,
            isSending = true,
            onClickCallback = {}) {
        }
    }
}

@Preview
@Composable
fun PreviewMmsImage_filepath() {
    Column {
        MmsContentView(
            "content://file/path".toUri(),
            "text/v-card",
            "demo.txt",
            false,
            Telephony.Mms.MESSAGE_BOX_SENT,
            onClickCallback = {}) {
        }
    }
}

