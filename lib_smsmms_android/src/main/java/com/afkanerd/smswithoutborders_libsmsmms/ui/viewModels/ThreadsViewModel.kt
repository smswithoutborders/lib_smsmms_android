package com.afkanerd.smswithoutborders_libsmsmms.ui.viewModels

import android.R.attr.onClick
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.BlockedNumberContract.AUTHORITY_URI
import android.provider.BlockedNumberContract.isBlocked
import android.provider.Telephony
import android.widget.Toast
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toString
import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.compose.ui.test.isSelected
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import androidx.paging.map
import com.afkanerd.lib_smsmms_android.R
import com.afkanerd.smswithoutborders_libsmsmms.data.dao.ConversationsDao
import com.afkanerd.smswithoutborders_libsmsmms.data.data.models.DateTimeUtils
import com.afkanerd.smswithoutborders_libsmsmms.data.entities.Threads
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.ActivitiesConstant
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.blockContact
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.deleteSmsThreads
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.getDatabase
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.isDefault
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.isNumberBlocked
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.loadRawSmsMmsDb
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.loadRawThreads
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.retrieveContactName
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.retrieveContactPhoto
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.settingsGetDeleteSystem
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.unblockContact
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread

open class ThreadsViewModel: ViewModel() {


    enum class InboxType {
        INBOX,
        ARCHIVED,
        BLOCKED,
        DRAFTS,
        MUTED,
        CUSTOM,
        DEVELOPER,
    }

    private val _messageLoadingUiState = MutableStateFlow(false) // default
    val messageLoadingUiState: StateFlow<Boolean> = _messageLoadingUiState

    private val _secondaryLoadingUiState = MutableStateFlow(false) // default
    val secondaryLoadingUiState: StateFlow<Boolean> = _secondaryLoadingUiState

    private val _inboxType = MutableStateFlow(InboxType.INBOX) // default
    val inboxType: StateFlow<InboxType> = _inboxType

    private val _drawerState = MutableStateFlow(DrawerState(DrawerValue.Closed)) // default
    val drawerState: StateFlow<DrawerState> = _drawerState

    fun toggleDrawerValue() {
        viewModelScope.launch(AndroidUiDispatcher.Main) {
            _drawerState.value.apply {
                if(isClosed) open() else close()
            }
        }
    }

    private val _selectedItems = MutableStateFlow<List<Threads>>(mutableListOf()) // default
    val selectedItems: StateFlow<List<Threads>> = _selectedItems

    fun setSelectedItems(threads: List<Threads>) {
        _selectedItems.value = threads.toMutableList()
    }

    fun removeAllSelectedItems() {
        _selectedItems.value = mutableListOf()
    }

    var pageSize: Int = 200
    var prefetchDistance: Int = 3 * pageSize
    var enablePlaceholder: Boolean = false
    var initialLoadSize: Int = 3 * pageSize
    var maxSize: Int = PagingConfig.MAX_SIZE_UNBOUNDED

    fun setInboxType(inboxType: InboxType) {
        this._inboxType.value = inboxType
    }

    data class ThreadsUi(
        val id: Int,
        val threads: Threads,
        val date: String,
        val isSelected: Boolean,
        val unreadCount: Flow<Int>,
        val onClick: () -> Unit,
        val onLongClick: () -> Unit,
        val loadPreComputed: suspend (Context) -> ThreadsComputations?,
    )

    data class ThreadsComputations(
        val name: String?,
        val blocked: Boolean,
        val photo: String?,
    )

    fun getThreads(
        context: Context,
        navigationCallback: (thread: Threads) -> Unit,
    ): Flow<PagingData<ThreadsUi>> {
        val db = context.getDatabase()
        val threadsDao = db.threadsDao() ?: throw Exception("Failed to open threads db")

        return Pager(
            config=PagingConfig(
                pageSize,
                prefetchDistance,
                enablePlaceholder,
                initialLoadSize,
                maxSize
            ),
            pagingSourceFactory = {
                when(_inboxType.value) {
                    InboxType.ARCHIVED -> threadsDao.getArchived()
                    InboxType.BLOCKED -> threadsDao.getIsBlocked()
                    InboxType.DRAFTS -> threadsDao.getType(Telephony.Sms.MESSAGE_TYPE_DRAFT)
                    InboxType.MUTED -> threadsDao.getIsMute()
                    else -> threadsDao.getThreads()
                }
            }
        )
            .flow
            .map{ pd -> pd.map{ thread ->
                val isSelected = _selectedItems.value.contains(thread)
                val date = DateTimeUtils.formatDate(context, thread.date) ?: ""
                val unreadCount = threadsDao.getUnreadCount(thread.threadId)

                ThreadsUi(
                    id = thread.threadId,
                    threads = thread,
                    date = date,
                    isSelected = isSelected,
                    unreadCount = unreadCount,
                    onClick = {
                        val currentSelected = _selectedItems.value
                        if (currentSelected.contains(thread)) {
                            _selectedItems.value = currentSelected - thread
                        } else if (currentSelected.isNotEmpty()) {
                            _selectedItems.value = currentSelected + thread
                        } else {
                            navigationCallback(thread)
                        }
                    },
                    onLongClick = {
                        val currentSelected = _selectedItems.value
                        if (currentSelected.contains(thread)) {
                            _selectedItems.value = currentSelected - thread
                        } else {
                            _selectedItems.value = currentSelected + thread
                        }
                    },
                    loadPreComputed = { ctx ->
                        if(!ctx.isDefault()) return@ThreadsUi null
                        withContext(Dispatchers.IO) {
                            val nameDeferred = async { ctx.retrieveContactName(thread.address) }
                            val blockedDeferred = async { ctx.isNumberBlocked(thread.address) }
                            val photoDeferred = async { getContactPhoto(ctx, thread.address) }

                            return@withContext ThreadsComputations(
                                name = nameDeferred.await(),
                                blocked = blockedDeferred.await(),
                                photo = photoDeferred.await(),
                            )
                        }
                    }
                )
            }}
            .cachedIn(viewModelScope)
//        return threadsPager!!
    }

    fun deleteThreads(context: Context, threads: List<Threads>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                context.getDatabase().threadsDao()?.delete(threads)
                if(context.settingsGetDeleteSystem) {
                    context.deleteSmsThreads(threads
                        .map { it.threadId.toString() }.toTypedArray())
                }
            }
        }
    }

    fun setIsBlocked(
        context: Context,
        addresses: List<String>,
        isBlocked: Boolean,
        callback: () -> Unit = {}
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                context.getDatabase().threadsDao()?.setIsBlocked(isBlocked, addresses)
                try {
                    if(isBlocked) {
                        context.unblockContact(addresses)
                    } else {
                        context.blockContact(addresses)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                callback()
            }
        }
    }

    fun update(
        context: Context,
        threads: List<Threads>,
        callback: (Boolean) -> Unit = {}
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val count = context.getDatabase().threadsDao()?.update(threads)
                callback(count != 0)
            }
        }
    }

    fun loadNativesAsync(
        context: Context,
        completeCallback: () -> Unit,
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _messageLoadingUiState.value = true

                try {
                    val threads = context.loadRawThreads()
                    threads.forEach { thread ->
                        val conversations = context
                            .loadRawSmsMmsDb(thread.first, thread.second)
                        context.getDatabase().conversationsDao()
                            ?.insertAllThreads(conversations, thread.second)
                        _messageLoadingUiState.value = false
                        _secondaryLoadingUiState.value = true
                    }
                    _messageLoadingUiState.value = false
                } catch(e: Exception) {
                    e.printStackTrace()
                } finally {
                    _secondaryLoadingUiState.value = false
                    withContext(Dispatchers.Main) {
                        completeCallback()
                    }
                }
            }
        }
    }

//    fun loadNatives(
//        context: Context,
//        deleteDb: Boolean = false,
//        completeCallback: () -> Unit,
//    ) {
//        viewModelScope.launch {
//            withContext(Dispatchers.IO) {
//                messagesLoading = true
//
//                try {
//                    val conversations = context.loadRawSmsMmsDb()
//                    context.getDatabase().conversationsDao()
//                        ?.insertAll(conversations, deleteDb)
//
//                } catch(e: Exception) {
//                    e.printStackTrace()
//                } finally {
//                    withContext(Dispatchers.Main) {
//                        messagesLoading = false
//                        completeCallback()
//                    }
//                }
//            }
//        }
//    }

    fun isArchived(context: Context, threadId: Int, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                context.getDatabase().threadsDao()?.get(threadId)?.isArchive?.let {
                    callback(it)
                    return@withContext
                }
                callback(false)
            }
        }
    }

    fun isBlocked(context: Context, thread: Threads, blockedList: List<Threads>?): Boolean {
        val METHOD_IS_BLOCKED = "is_blocked"
        val RES_NUMBER_IS_BLOCKED = "blocked"

        return try {
            val res: Bundle? = context.contentResolver.call(
                AUTHORITY_URI, METHOD_IS_BLOCKED, thread.address, null);
            res != null && res.getBoolean(RES_NUMBER_IS_BLOCKED, false);
        } catch (e: Exception) {
            e.printStackTrace()
            blockedList?.contains(thread) ?: false
        }
    }

    fun isMuted(context: Context, threadId: Int, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                context.getDatabase().threadsDao()?.get(threadId)?.isMute?.let {
                    callback(it)
                    return@withContext
                }
                callback(false)
            }
        }
    }

    fun get(
        context: Context,
        threadId: Int,
        callback: (Threads?) -> Unit,
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val thread = context.getDatabase().threadsDao()?.get(threadId)
                callback(thread)
            }
        }
    }

    fun markAllAsRead(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            context.getDatabase().threadsDao()?.markAllAsRead()
        }
    }

    private fun getContactPhoto(context: Context, phoneNumber: String): String? {
        return if(!context.isDefault()) null else context.retrieveContactPhoto(phoneNumber)
    }

    fun execMigrations(context: Context) {
        viewModelScope.launch(Dispatchers.Default) {
            Migrations(this@ThreadsViewModel)
                .migrateV1ToV2(context)
        }
    }

    class Migrations(private val threadsViewModel: ThreadsViewModel){
        private val dbV2Migration = "dbV2Migration"

        private fun Context.getMigratedV2(): Boolean {
            val sharedPreferences = getSharedPreferences(
                ActivitiesConstant.ACTIVITIES_FILENAMES, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(dbV2Migration, false)
        }

        private fun Context.setMigratedV2(load: Boolean) {
            val sharedPreferences = getSharedPreferences(
                ActivitiesConstant.ACTIVITIES_FILENAMES, Context.MODE_PRIVATE)
            return sharedPreferences.edit {
                putBoolean(dbV2Migration, load)
            }
        }
        fun migrateV1ToV2(context: Context) {
            if(context.isDefault()) {
                val roomVersion = context.getDatabase().openHelper.readableDatabase.version
                if(roomVersion == 2 && !context.getMigratedV2()) {
                    threadsViewModel.loadNativesAsync(context) {
                        CoroutineScope(Dispatchers.Main).launch {
                            context.setMigratedV2(true)
                            Toast.makeText(context,
                                context.getString(R.string.secure_database_migrated),
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                context.setMigratedV2(true)
            }
        }

    }

}