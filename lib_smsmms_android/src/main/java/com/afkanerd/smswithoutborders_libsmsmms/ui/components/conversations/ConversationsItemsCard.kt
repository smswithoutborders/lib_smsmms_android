package com.afkanerd.smswithoutborders_libsmsmms.ui.components.conversations

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.core.net.toUri
import com.afkanerd.smswithoutborders_libsmsmms.data.entities.Conversations
import com.afkanerd.smswithoutborders_libsmsmms.ui.components.ConversationType
import com.afkanerd.smswithoutborders_libsmsmms.ui.components.ConversationItem
import com.afkanerd.smswithoutborders_libsmsmms.ui.viewModels.ConversationsViewModel
import com.afkanerd.smswithoutborders_libsmsmms.ui.viewModels.ThreadsViewModel
import com.google.android.mms.ContentType
import sh.calvin.autolinktext.rememberAutoLinkText

@Composable
fun ConversationUi(
    conversationsUi: ConversationsViewModel.ConversationsUi,
    showDate: Boolean,
    isSelected: Boolean,
    searchQuery: String?,
    cuiList: List<ConversationsViewModel.ConversationsUi>,
    onLongClickCallback: () -> Unit,
    onClickCallback: () -> Unit,
) {
    val context = LocalContext.current
    val inPreview = LocalInspectionMode.current
    val conversation = conversationsUi.conversation

    val body = conversation.sms?.body ?: ""
    var text = if(inPreview) AnnotatedString(body)
    else AnnotatedString.rememberAutoLinkText(
        conversation.mms_text ?: (body),
        defaultLinkStyles = TextLinkStyles(
            SpanStyle(textDecoration = TextDecoration.Underline))
    )

    if(!searchQuery.isNullOrEmpty()) {
        text = buildAnnotatedString {
            val startIndex = text
                .indexOf(searchQuery, ignoreCase = true)
            val endIndex = startIndex + searchQuery.length

            append(text)
            if (startIndex >= 0) {
                addStyle(
                    style = SpanStyle(
                        background = MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiary,
                    ),
                    start = startIndex,
                    end = endIndex
                )
            }
        }
    }
    var postComputed by remember(conversationsUi.id) {
        mutableStateOf<ConversationsViewModel.ConversationsComputed?>(null)
    }
    var contentType: ConversationType by remember{ mutableStateOf(ConversationType.NORMAL)}
    LaunchedEffect(conversationsUi.id) {
        postComputed = conversationsUi.loadPreComputed(context, cuiList)
    }

    ConversationItem(
        text= text,
        timestamp = postComputed?.timestamp ?: "",
        type= conversation.sms?.type!!,
        status = conversation.sms?.status!!,
        date = postComputed?.date ?: "",
        showDate = showDate,
        mmsContentUri = conversation.mms_content_uri?.toUri(),
        mmsMimeType = conversation.mms_mimetype,
        mmsFilename = conversation.mms_filename,
        onClickCallback = onClickCallback,
        onLongClickCallback = onLongClickCallback,
        isSelected = isSelected,
        contentType = contentType
    )

}

