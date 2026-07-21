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
import androidx.paging.insertFooterItem
import androidx.paging.map
import com.afkanerd.lib_smsmms_android.R
import com.afkanerd.smswithoutborders_libsmsmms.data.data.models.DateTimeUtils
import com.afkanerd.smswithoutborders_libsmsmms.data.data.models.SmsMmsNatives
import com.afkanerd.smswithoutborders_libsmsmms.data.entities.Conversations
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.getDatabase
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.getSubscriptionName
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.isDefault
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.isDualSim
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.sendMms
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.sendSms
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.settingsGetKeepMessagesArchived
import com.afkanerd.smswithoutborders_libsmsmms.ui.components.ConvenientMethods.deriveMetaDate
import com.afkanerd.smswithoutborders_libsmsmms.ui.components.ConversationType
import com.afkanerd.smswithoutborders_libsmsmms.ui.components.getConversationType
import com.afkanerd.smswithoutborders_libsmsmms.ui.navigation.ImageViewScreenNav
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.toMutableList
import kotlin.concurrent.thread

class ConversationsViewModel : ViewModel(),  CustomConversationServices {
    private val _isMuted = MutableStateFlow(false) // default
    val isMuted: StateFlow<Boolean> = _isMuted

    private val _isArchived = MutableStateFlow(false) // default
    val isArchived: StateFlow<Boolean> = _isArchived

    private val _isBlocked = MutableStateFlow(false) // TODO: actually checked if blocked
    val isBlocked: StateFlow<Boolean> = _isBlocked

    private val _showFailureRetryModal = MutableStateFlow(false) // TODO: actually checked if blocked
    val showFailureRetryModal: StateFlow<Boolean> = _showFailureRetryModal

    private val _selectedItems = MutableStateFlow<List<ConversationsUi>>(emptyList()) // default
    val selectedItems: StateFlow<List<ConversationsUi>> = _selectedItems

    private val _subscriptionId = MutableStateFlow(-1) // default
    val subscriptionId: StateFlow<Int> = _subscriptionId

    private val _highlightedMessage = MutableStateFlow<ConversationsUi?>(null) // default
    val highlightedMessage: StateFlow<ConversationsUi?> = _highlightedMessage

    fun toggleIsMute() {
        _isMuted.value = !_isMuted.value
    }

    fun toggleIsBlocked() {
        _isBlocked.value = !_isBlocked.value
    }

    fun setSelectedItems(conversations: List<ConversationsUi>) {
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

    data class ConversationsUi(
        val id: Long,
        val conversation: Conversations,
        val onClick: (ConversationsUi) -> Unit,
        val onLongClick: (ConversationsUi) -> Unit,
        val loadPreComputed: suspend (Context, List<ConversationsUi>) -> ConversationsComputed?,
    )

    data class ConversationsComputed(
        val timestamp: String,
        val date: String,
        val contentType: ConversationType,
    )

    fun getConversations(
        context: Context,
        threadId: Int,
        mmsOnClickCallback: (ConversationsUi) -> Unit,
    ): Flow<PagingData<ConversationsUi>> {
        // TODO: put isBlocked, isArchived etc to be computed in here

        val db = context.getDatabase().conversationsDao()!!
        return Pager(
            config=PagingConfig(
                pageSize,
                prefetchDistance,
                enablePlaceholder,
                initialLoadSize,
                maxSize
            ),
            pagingSourceFactory = {
                db.getConversations(threadId)
            }
        ).flow.map { pg ->
            val counter = AtomicInteger(0) // Tracks the index per generation
            pg.map { conversation ->
                ConversationsUi(
                    id = conversation.id,
                    conversation = conversation,
                    onClick = { cui ->
                        val currentSelected = _selectedItems.value
                        if(currentSelected.isNotEmpty()) {
                            if (currentSelected.contains(cui)) {
                                _selectedItems.value = currentSelected - cui
                            } else {
                                _selectedItems.value = currentSelected + cui
                            }
                        }
                        else if(conversation.sms?.type == Telephony.Sms.MESSAGE_TYPE_FAILED) {
                            _highlightedMessage.value = cui
                            _showFailureRetryModal.value = true
                        }
                        else if(conversation.mms_content_uri != null) {
                            mmsOnClickCallback(cui)
                        }
                    },
                    onLongClick = { cui ->
                        val currentSelected = _selectedItems.value
                        if (currentSelected.contains(cui)) {
                            _selectedItems.value = currentSelected - cui
                        } else {
                            _selectedItems.value = currentSelected + cui
                        }
                    },
                    loadPreComputed = { ctx, listCui ->
                        if(!ctx.isDefault()) return@ConversationsUi null
                        withContext(Dispatchers.IO) {
                            val timestamp = async { DateTimeUtils
                                .formatDateExtended( context, conversation.sms?.date!!) }
                            val date = async {
                                deriveMetaDate(conversation) + if(ctx.isDualSim()) {
                                    " • " + context.getSubscriptionName(conversation.sms?.sub_id ?: -1)
                                } else ""
                            }
//                            val contentType = async {
//                                getConversationType(
//                                    index = counter.getAndIncrement(),
//                                    conversations = listCui.map{ it.conversation }
//                                )
//                            }

                            return@withContext ConversationsComputed(
//                                contentType = contentType.await(),
                                contentType = ConversationType.NORMAL,
                                timestamp = timestamp.await(),
                                date = date.await()
                            )
                        }
                    }
                )
            }
        }.cachedIn(viewModelScope)
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
    ) : ConversationType? {
        if(index == 0) {
            // check next
            if(nextConversation?.sms?.type == conversation.sms?.type) {
                if(DateTimeUtils.isSameMinute(conversation.sms!!.date,
                        nextConversation?.sms!!.date)) {
                    return ConversationType.END
                }
            }
        }
        else if(nextConversation == null) {
            if(DateTimeUtils.isSameMinute(conversation.sms!!.date,
                    previousConversation?.sms!!.date)) {
                return ConversationType.START_TIMESTAMP
            }
        }
        else {
            if(nextConversation.sms?.type == conversation.sms?.type &&
                previousConversation?.sms?.type == conversation.sms?.type) {
                if(DateTimeUtils.isSameMinute(conversation.sms!!.date,
                            previousConversation?.sms!!.date) &&
                    DateTimeUtils.isSameMinute(conversation.sms!!.date,
                        nextConversation.sms!!.date)) {
                    return ConversationType.MIDDLE
                }

                if(DateTimeUtils.isSameMinute(conversation.sms!!.date,
                        previousConversation.sms!!.date) &&
                    !DateTimeUtils.isSameMinute(conversation.sms!!.date,
                        nextConversation.sms!!.date)) {
                    if(!DateTimeUtils.isSameHour(conversation.sms!!.date,
                            nextConversation.sms!!.date)) {
                        return ConversationType.START_TIMESTAMP
                    }
                    return ConversationType.START
                }

                if(!DateTimeUtils.isSameMinute(conversation.sms!!.date,
                        previousConversation.sms!!.date) &&
                    DateTimeUtils.isSameMinute(conversation.sms!!.date,
                        nextConversation.sms!!.date)) {
                    return ConversationType.END
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
    ) : ConversationType {
        if(index == 0 && nextConversation == null) {
            return ConversationType.NORMAL_TIMESTAMP
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
            return ConversationType.NORMAL_TIMESTAMP
        }

        return ConversationType.NORMAL
    }

}