package com.afkanerd.smswithoutborders_libsmsmms.ui.components

import android.net.Uri
import android.provider.Telephony
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.afkanerd.lib_smsmms_android.R
import com.afkanerd.smswithoutborders_libsmsmms.data.data.models.DateTimeUtils
import com.afkanerd.smswithoutborders_libsmsmms.data.data.models.SmsMmsNatives
import com.afkanerd.smswithoutborders_libsmsmms.data.entities.Conversations

enum class ConversationType(val value: Int) {
    NORMAL(0),
    START(1),
    MIDDLE(2),
    END(3),
    START_TIMESTAMP(4),
    NORMAL_TIMESTAMP(5),
}

@Preview(showBackground = true)
@Composable
private fun ConversationIsKey(isReceived: Boolean = false) {
    Column(
        modifier = Modifier
        .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(isReceived) {
            Text(
                text=stringResource(R.string.secure_communications_request_received),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(24.dp),
//                color = colorResource(R.color.md_theme_secondary)
            )
        } else {
            Text(
                text=stringResource(R.string.secure_communication_requested),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(24.dp),
//                color = colorResource(R.color.md_theme_secondary)
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ConversationReceived(
    text: AnnotatedString,
    date: String,
    position: ConversationType = ConversationType.START_TIMESTAMP,
    isSelected: Boolean = false,
    onClickCallback: (() -> Unit)? = null,
    onLongClickCallback: (() -> Unit)? = null,
//    color: Color = colorResource(R.color.md_theme_onBackground)
) {
    val receivedShape = RoundedCornerShape(18.dp, 18.dp, 18.dp, 18.dp)
    val receivedStartShape = RoundedCornerShape(28.dp, 28.dp, 28.dp, 1.dp)
    val receivedMiddleShape = RoundedCornerShape(1.dp, 28.dp, 28.dp, 1.dp)
    val receivedEndShape = RoundedCornerShape(1.dp, 28.dp, 28.dp, 28.dp)

    val shape = when(position) {
        ConversationType.NORMAL, ConversationType.NORMAL_TIMESTAMP ->
            receivedShape
        ConversationType.START, ConversationType.START_TIMESTAMP ->
            receivedStartShape
        ConversationType.MIDDLE -> receivedMiddleShape
        ConversationType.END -> receivedEndShape
    }

    val modifier = when(position) {
        ConversationType.NORMAL, ConversationType.NORMAL_TIMESTAMP ->
            Modifier.padding(end=32.dp, top=16.dp, bottom=8.dp)
        ConversationType.START, ConversationType.START_TIMESTAMP ->
            Modifier.padding(end=32.dp, top=16.dp)
        ConversationType.MIDDLE -> Modifier.padding(end=32.dp, top=1.dp)
        ConversationType.END -> Modifier.padding(end=32.dp, top=1.dp, bottom=8.dp)
    }

    Row(modifier = modifier
        .fillMaxWidth()
    ) {
        Column {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if(isSelected) MaterialTheme.colorScheme.tertiaryContainer
                    else MaterialTheme.colorScheme.outlineVariant
                ),
                modifier = Modifier
                    .clip(shape = shape)
                    .then(
                        if(!LocalInspectionMode.current) {
                            Modifier.combinedClickable(
                                onClick = { onClickCallback?.invoke() },
                                onLongClick = { onLongClickCallback?.invoke() }
                            )
                        } else Modifier
                    )
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp),
//                    color = color
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ConversationSent(
    text: AnnotatedString,
    position: ConversationType = ConversationType.START_TIMESTAMP,
    type: Int,
    isSelected: Boolean = false,
    onClickCallback: (() -> Unit)? = null,
    onLongClickCallback: (() -> Unit)? = null,
    color: Color = MaterialTheme.colorScheme.onPrimary
) {
    val sentShape = RoundedCornerShape(18.dp, 18.dp, 18.dp, 18.dp)
    val sentStartShape = RoundedCornerShape(28.dp, 28.dp, 1.dp, 28.dp)
    val sentMiddleShape = RoundedCornerShape(28.dp, 1.dp, 1.dp, 28.dp)
    val sentEndShape = RoundedCornerShape(28.dp, 1.dp, 28.dp, 28.dp)

    val shape = when(position) {
        ConversationType.NORMAL, ConversationType.NORMAL_TIMESTAMP -> sentShape
        ConversationType.START, ConversationType.START_TIMESTAMP -> sentStartShape
        ConversationType.MIDDLE -> sentMiddleShape
        ConversationType.END -> sentEndShape
    }

    val modifier = when(position) {
        ConversationType.NORMAL, ConversationType.NORMAL_TIMESTAMP ->
            Modifier.padding(start=32.dp, top=16.dp, bottom=8.dp)
        ConversationType.START, ConversationType.START_TIMESTAMP ->
            Modifier.padding(start=32.dp, top=16.dp)
        ConversationType.MIDDLE -> Modifier.padding(start=32.dp, top=1.dp)
        ConversationType.END -> Modifier.padding(start=32.dp, top=1.dp, bottom=8.dp)
    }

    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Column {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if(isSelected) MaterialTheme.colorScheme.secondary
                    else MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .clip(shape = shape)
                    .align(alignment = Alignment.End)
                    .then(
                        if(!LocalInspectionMode.current) {
                            Modifier.combinedClickable(
                                onClick = { onClickCallback?.invoke() },
                                onLongClick = { onLongClickCallback?.invoke() }
                            )
                        } else Modifier
                    )
            ) {
                Text(
                    text= text,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp),
                    color = if(isSelected)
                        MaterialTheme.colorScheme.onSecondary
                    else color
                )
            }

        }

        if(type == Telephony.Sms.MESSAGE_TYPE_FAILED) {
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

@Composable
fun ConversationItem(
    text: AnnotatedString,
    timestamp: String,
    date: String,
    type: Int,
    showDate: Boolean = true,
    contentType: ConversationType,
    status: Int,
    isSelected: Boolean = false,
    mmsContentUri: Uri? = null,
    mmsMimeType: String? = null,
    mmsFilename: String? = null,
    onClickCallback: (() -> Unit)? = null,
    onLongClickCallback: (() -> Unit)? = null,
) {
    val inPreview = LocalInspectionMode.current
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        when(contentType) {
            ConversationType.START_TIMESTAMP,
            ConversationType.NORMAL_TIMESTAMP -> {
                Text(
                    text=timestamp,
                    style= MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }
            else -> {}
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp)
        ) {
            when(type) {
                SmsMmsNatives.MMS_MESSAGE_TYPES_M_RETRIEVE_CONF,
                Telephony.TextBasedSmsColumns.MESSAGE_TYPE_INBOX -> {
                    Column {
                        if(mmsContentUri != null && mmsMimeType != null) {
                            MmsContentView(
                                mmsContentUri,
                                mmsMimeType,
                                mmsFilename,
                                type = type,
                                isSelected = isSelected,
                                onClickCallback = onClickCallback,
                                onLongClickCallback = onLongClickCallback,
                            )
                        }
                        if(text.isNotEmpty()) {
                            ConversationReceived(
                                text =text,
                                position =contentType,
                                date =date,
                                isSelected = isSelected,
                                onClickCallback = onClickCallback,
                                onLongClickCallback = onLongClickCallback,
                            )
                        }

                        if(showDate) {
                            Text(
                                text= date,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                        }
                    }
                }

                SmsMmsNatives.MMS_MESSAGE_TYPES_M_SEND_REQ,
                Telephony.TextBasedSmsColumns.MESSAGE_TYPE_SENT,
                Telephony.TextBasedSmsColumns.MESSAGE_TYPE_QUEUED,
                Telephony.TextBasedSmsColumns.MESSAGE_TYPE_FAILED,
                Telephony.TextBasedSmsColumns.MESSAGE_TYPE_OUTBOX -> {
                    Column(modifier = Modifier
                        .fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        if(mmsContentUri != null && mmsMimeType != null) {
                            MmsContentView(
                                mmsContentUri,
                                mmsMimeType,
                                mmsFilename,
                                isSelected = isSelected,
                                isSending = true,
                                type = type,
                                onClickCallback = onClickCallback,
                                onLongClickCallback = onLongClickCallback,
                            )
                        }
                        if(text.isNotEmpty()) {
                            ConversationSent(
                                text =text,
                                position =contentType,
                                type =type,
                                isSelected = isSelected,
                                onClickCallback = onClickCallback,
                                onLongClickCallback = onLongClickCallback,
                            )
                        }

                        if(showDate || inPreview) {
                            Text(
                                text= when(type) {
                                    Telephony.Sms.MESSAGE_TYPE_SENT -> {
                                        if(status == Telephony.Sms.STATUS_COMPLETE)
                                            "$date • ${stringResource(
                                                R.string.sms_status_delivered)}"
                                        else
                                            "$date • " + stringResource(R.string.sms_status_sent)
                                    }

                                    Telephony.Sms.MESSAGE_TYPE_FAILED ->
                                        stringResource(R.string.sms_status_failed)

                                    Telephony.Sms.MESSAGE_TYPE_QUEUED ->
                                        stringResource(R.string.waiting_to_send)

                                    else ->
                                        stringResource(R.string.sms_status_sending)
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = if(type == Telephony.Sms.MESSAGE_TYPE_FAILED)
                                    MaterialTheme.colorScheme.error
                                else MaterialTheme.colorScheme.outlineVariant,

                                modifier = Modifier
                                    .padding(bottom=4.dp)
                            )
                        }
                    }
                }
                else -> {}
            }
        }
    }
}


private fun getPredefinedType(type: Int) : ConversationsPredefinedTypes? {
    when(type) {
        Telephony.Sms.MESSAGE_TYPE_OUTBOX,
        Telephony.Sms.MESSAGE_TYPE_QUEUED,
        Telephony.Sms.MESSAGE_TYPE_SENT -> {
            return ConversationsPredefinedTypes.OUTGOING
        }
        Telephony.Sms.MESSAGE_TYPE_INBOX -> {
            return ConversationsPredefinedTypes.INCOMING
        }
    }
    return null
}

fun getConversationType(
    index: Int,
    conversations: List<Conversations>
): ConversationType {
    val conversation = conversations[index]
    // 1. Edge Case: If the list is empty or has only 1 item, it's always a standalone timestamp
    if (conversations.size < 2) {
        return ConversationType.NORMAL_TIMESTAMP
    }

    // 2. Safe Data Extraction: Unwraps the SMS object. If the current message has no SMS data,
    // it safely defaults to NORMAL instead of crashing.
    val currentSms = conversation.sms ?: return ConversationType.NORMAL
    val currentType = getPredefinedType(currentSms.type)
    val currentDate = currentSms.date.toLong()

    // Grab the neighbor messages safely (will be null if index is out of bounds)
    val prevSms = conversations.getOrNull(index - 1)?.sms
    val nextSms = conversations.getOrNull(index + 1)?.sms

    // 3. Logic for the FIRST message in the list
    if (index == 0 && nextSms != null) {
        val nextType = getPredefinedType(nextSms.type)
        val nextDate = nextSms.date.toLong()

        if (currentType == nextType && DateTimeUtils.isSameMinute(currentDate, nextDate)) {
            return ConversationType.END
        }
        if (!DateTimeUtils.isSameHour(currentDate, nextDate)) {
            return ConversationType.NORMAL_TIMESTAMP
        }
    }

    // 4. Logic for the LAST message in the list
    if (index == conversations.size - 1 && prevSms != null) {
        val prevType = getPredefinedType(prevSms.type)
        val prevDate = prevSms.date.toLong()

        if (currentType == prevType && DateTimeUtils.isSameMinute(currentDate, prevDate)) {
            return ConversationType.START_TIMESTAMP
        }
        return ConversationType.NORMAL_TIMESTAMP
    }

    // 5. Logic for MIDDLE messages (has both a previous and next neighbor)
    if (prevSms != null && nextSms != null) {
        val prevType = getPredefinedType(prevSms.type)
        val prevDate = prevSms.date.toLong()

        val nextType = getPredefinedType(nextSms.type)
        val nextDate = nextSms.date.toLong()

        // Scenario A: Surrounded by the same sender on both sides
        if (currentType == prevType && currentType == nextType) {
            if (DateTimeUtils.isSameHour(currentDate, prevDate)) {
                if (DateTimeUtils.isSameMinute(currentDate, prevDate) &&
                    DateTimeUtils.isSameMinute(currentDate, nextDate)) {
                    return ConversationType.MIDDLE
                }
                if (DateTimeUtils.isSameMinute(currentDate, prevDate)) {
                    return ConversationType.START
                }
            }
        }

        // Scenario B: Matches the next sender
        if (currentType == nextType) {
            if (DateTimeUtils.isSameHour(currentDate, nextDate) &&
                DateTimeUtils.isSameMinute(currentDate, nextDate)) {
                return ConversationType.END
            }
            return ConversationType.NORMAL_TIMESTAMP
        }

        // Scenario C: Matches the previous sender
        if (currentType == prevType) {
            if (DateTimeUtils.isSameMinute(currentDate, prevDate)) {
                if (DateTimeUtils.isSameHour(currentDate, nextDate)) {
                    return ConversationType.START_TIMESTAMP
                }
                return ConversationType.START
            }
        }
    }

    // Default fallback if no conditions are met
    return ConversationType.NORMAL
}
enum class ConversationsPredefinedTypes {
    OUTGOING,
    INCOMING
}

@Composable
fun ConversationContactName(
    contactName: String,
    isSecured: Boolean = false
) {
    Column {
        Row {
            Text(
                text= contactName,
                maxLines =1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(end=8.dp),
            )
            if(isSecured || LocalInspectionMode.current) {
                Icon(Icons.Default.Security,
                    stringResource(R.string.conversation_is_secured)
                )
            }
        }
        if(isSecured || LocalInspectionMode.current) {
            Text(
                stringResource(R.string.secured),
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Preview
@Composable
fun PreviewConversationItem_Delivered() {
    Surface(Modifier.safeDrawingPadding()) {
        Column {
            ConversationItem(
                text = AnnotatedString("Hello world"),
                timestamp = "Yesterday",
                date = "Yesterday",
                type = Telephony.Sms.MESSAGE_TYPE_SENT,
                contentType = ConversationType.NORMAL,
                status = Telephony.Sms.STATUS_COMPLETE,
                isSelected = false,
            )
        }
    }
}

@Preview
@Composable
fun PreviewConversationItem() {
    Surface(Modifier.safeDrawingPadding()) {
        Column {
            ConversationItem(
                text = AnnotatedString("Hello world"),
                timestamp = "Yesterday",
                date = "Yesterday",
                type = Telephony.Sms.MESSAGE_TYPE_OUTBOX,
                contentType = ConversationType.NORMAL,
                status = Telephony.Sms.STATUS_PENDING,
                isSelected = false,
            )
        }
    }
}

@Preview
@Composable
fun PreviewConversationsReceived() {
    Surface(Modifier.safeDrawingPadding()) {
        Column {
            ConversationReceived(
                text = AnnotatedString("Hello world"),
                date = "yesterday",
            )
            ConversationSent(
                text = AnnotatedString("Hello world"),
                type = Telephony.Mms.MESSAGE_BOX_FAILED
            )
        }
    }
}

