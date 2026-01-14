package com.afkanerd.smswithoutborders_libsmsmms.ui.viewModels

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.BlockedNumberContract
import android.provider.Telephony
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.afkanerd.lib_smsmms_android.R
import com.afkanerd.smswithoutborders_libsmsmms.data.data.models.DateTimeUtils
import com.afkanerd.smswithoutborders_libsmsmms.data.data.models.SmsMmsNatives
import com.afkanerd.smswithoutborders_libsmsmms.data.entities.Conversations
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.getDatabase
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.sendMms
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.sendSms
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.settingsGetKeepMessagesArchived
import com.afkanerd.smswithoutborders_libsmsmms.ui.components.ConversationPositionTypes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConversationsViewModel : ViewModel(),  CustomConversationServices {
    private val _selectedItems = MutableStateFlow<List<Conversations>>(emptyList()) // default
    val selectedItems: StateFlow<List<Conversations>> = _selectedItems.asStateFlow()

    fun setSelectedItems(conversations: List<Conversations>) {
        _selectedItems.value = conversations
    }

    fun removeAllSelectedItems() {
        _selectedItems.value = emptyList()
    }

    fun getSelectedItemCount(): Int {
        return _selectedItems.value.size
    }

    var pageSize: Int = 50
    var prefetchDistance: Int = 3 * pageSize
    var enablePlaceholder: Boolean = true
    var initialLoadSize: Int = 2 * pageSize
    var maxSize: Int = PagingConfig.MAX_SIZE_UNBOUNDED


    private var conversationsPager: Flow<PagingData<Conversations>>? = null

    fun getConversations(context: Context, threadId: Int): Flow<PagingData<Conversations>> {
        if(conversationsPager == null) {
            conversationsPager = Pager(
                config=PagingConfig(
                    pageSize,
                    prefetchDistance,
                    enablePlaceholder,
                    initialLoadSize,
                    maxSize
                ),
                pagingSourceFactory = {
                    context.getDatabase().conversationsDao()!!
                        .getConversations(threadId)
                }
            ).flow.cachedIn(viewModelScope)
        }
        return conversationsPager!!
    }

    fun contactIsBlocked(
        context: Context,
        address: String,
        callback: (Boolean) -> Unit
    ): Boolean {
        viewModelScope.launch(Dispatchers.IO) {
            val isBlocked = try {
                BlockedNumberContract.isBlocked(context,address)
            } catch (e: Exception) {
                e.printStackTrace()
                context.getDatabase().threadsDao()?.get(address)?.isBlocked ?: false
            }
            callback(isBlocked)
        }
        return false
    }

    fun fetchDraft(
        context: Context,
        threadId: Int,
        callback: (Conversations?) -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                context.getDatabase().conversationsDao()
                    ?.fetchConversationsForType( threadId, Telephony.Sms.MESSAGE_TYPE_DRAFT)
                    ?.let { callback(it) }
            }
        }
    }

    fun search(
        context: Context,
        query: String,
        threadId: Int,
        callback: (List<Int>) -> Unit,
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val searchIndexes = mutableListOf<Int>()
                context.getDatabase().conversationsDao()
                    ?.getConversationsList(threadId)?.let { items ->
                        items.forEachIndexed { index, it ->
                            it.sms?.body?.let { text ->
                                if(!searchIndexes.contains(index) &&
                                    text.contains(other=query, ignoreCase=true))
                                    searchIndexes.add(index)
                            }
                        }

                }
                callback(searchIndexes)
            }
        }
    }

    fun clearDraft(
        context: Context,
        conversation: Conversations
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                context.getDatabase().conversationsDao()
                    ?.delete(conversation, !context.settingsGetKeepMessagesArchived)
            }
        }
    }

    fun mute(context: Context, threadId: Int, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                context.getDatabase().threadsDao()?.get(threadId)?.let { thread ->
                    ThreadsViewModel().update(context, listOf(thread.apply {
                        isMute = true
                    })) {
                        callback(it)
                    }
                }
            }
        }
    }

    fun unMute(context: Context, threadId: Int, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                context.getDatabase().threadsDao()?.get(threadId)?.let { thread ->
                    ThreadsViewModel().update(context, listOf(thread.apply {
                        isMute = false
                    })) {
                        callback(it)
                    }
                }
            }
        }
    }

    fun unArchive(context: Context, threadId: Int, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                context.getDatabase().threadsDao()?.get(threadId)?.let { thread ->
                    ThreadsViewModel().update(context, listOf(thread.apply {
                        isArchive = false
                    })) {
                        callback(it)
                    }
                }
            }
        }
    }

    fun archive(context: Context, threadId: Int, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                context.getDatabase().threadsDao()?.get(threadId)?.let { thread ->
                    ThreadsViewModel().update(context, listOf(thread.apply {
                        isArchive = true
                    })) {
                        callback(it)
                    }
                }
            }
        }
    }

    fun sendMms(
        context: Context,
        uri: Uri,
        text: String,
        address: String,
        subscriptionId: Long,
        threadId: Int,
        filename: String,
        mimeType: String,
        callback: (Conversations?) -> Unit
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    context.sendMms(
                        contentUri = uri,
                        text = text,
                        address = address,
                        threadId = threadId,
                        subscriptionId = subscriptionId,
                        filename = filename,
                        mimeType = mimeType
                    ).let { conversation ->
                        callback(conversation)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context,
                            context.getString(R.string.something_went_wrong_with_sending),
                            Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun sendSms(
        context: Context,
        text: String,
        address: String,
        subscriptionId: Long,
        threadId: Int,
        data: ByteArray?,
        bundle: Bundle,
        callback: (Conversations?) -> Unit
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    context.sendSms(
                        text = text,
                        address = address,
                        threadId = threadId,
                        subscriptionId = subscriptionId,
                        data = data,
                        bundle = bundle
                    )?.let { conversation ->
                        callback(conversation)
                    }
                } catch(e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context,
                            context.getString(R.string.something_went_wrong_with_sending),
                            Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    fun delete(
        context: Context,
        conversations: List<Conversations>,
        callback: () -> Unit
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                context.getDatabase().conversationsDao()
                    ?.delete(conversations, !context.settingsGetKeepMessagesArchived)
                callback()
            }
        }
    }

    fun deleteThread(
        context: Context,
        threadId: Int,
        callback: () -> Unit,
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                context.getDatabase().threadsDao()?.get(threadId)?.let { thread ->
                    ThreadsViewModel().deleteThreads(context, listOf(thread))
                    callback()
                }
            }
        }
    }

    fun addDraft(
        context: Context,
        body: String,
        mmsUri: Uri?,
        address: String,
        subId: Long,
        threadId: Int,
        callback: (Conversations) -> Unit
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val conversation = Conversations(
                    sms = SmsMmsNatives.Sms(
                        _id = System.currentTimeMillis(),
                        thread_id = threadId,
                        address = address,
                        date = System.currentTimeMillis(),
                        date_sent = System.currentTimeMillis(),
                        read = 1,
                        status = Telephony.Sms.STATUS_PENDING,
                        type = Telephony.Sms.MESSAGE_TYPE_DRAFT,
                        body = body,
                        sub_id = subId
                    ),
                    mms_content_uri = mmsUri?.toString()
                )
                context.getDatabase().conversationsDao()?.insert(conversation)
                callback(conversation)
            }
        }
    }

    private fun isGroup(
        index: Int,
        conversation: Conversations,
        previousConversation: Conversations?,
        nextConversation: Conversations?
    ) : ConversationPositionTypes? {
        if(index == 0) {
            // check next
            if(nextConversation?.sms?.type == conversation.sms?.type) {
                if(DateTimeUtils.isSameMinute(conversation.sms!!.date,
                        nextConversation?.sms!!.date)) {
                    return ConversationPositionTypes.END
                }
            }
        }
        else if(nextConversation == null) {
            if(DateTimeUtils.isSameMinute(conversation.sms!!.date,
                    previousConversation?.sms!!.date)) {
                return ConversationPositionTypes.START_TIMESTAMP
            }
        }
        else {
            if(nextConversation.sms?.type == conversation.sms?.type &&
                previousConversation?.sms?.type == conversation.sms?.type) {
                if(DateTimeUtils.isSameMinute(conversation.sms!!.date,
                            previousConversation?.sms!!.date) &&
                    DateTimeUtils.isSameMinute(conversation.sms!!.date,
                        nextConversation.sms!!.date)) {
                    return ConversationPositionTypes.MIDDLE
                }

                if(DateTimeUtils.isSameMinute(conversation.sms!!.date,
                        previousConversation.sms!!.date) &&
                    !DateTimeUtils.isSameMinute(conversation.sms!!.date,
                        nextConversation.sms!!.date)) {
                    if(!DateTimeUtils.isSameHour(conversation.sms!!.date,
                            nextConversation.sms!!.date)) {
                        return ConversationPositionTypes.START_TIMESTAMP
                    }
                    return ConversationPositionTypes.START
                }

                if(!DateTimeUtils.isSameMinute(conversation.sms!!.date,
                        previousConversation.sms!!.date) &&
                    DateTimeUtils.isSameMinute(conversation.sms!!.date,
                        nextConversation.sms!!.date)) {
                    return ConversationPositionTypes.END
                }
            }
        }
        return null
    }

    fun getMessagePositionType(
        index: Int,
        conversation: Conversations,
        previousConversation: Conversations?,
        nextConversation: Conversations?
    ) : ConversationPositionTypes {
        if(index == 0 && nextConversation == null) {
            return ConversationPositionTypes.NORMAL_TIMESTAMP
        }

        val groupType = isGroup(
            index,
            conversation,
            previousConversation,
            nextConversation
        )

        if(groupType != null) return groupType

        if(nextConversation == null || !DateTimeUtils.isSameHour(conversation.sms!!.date,
                nextConversation.sms?.date)) {
            return ConversationPositionTypes.NORMAL_TIMESTAMP
        }

        return ConversationPositionTypes.NORMAL
    }

}