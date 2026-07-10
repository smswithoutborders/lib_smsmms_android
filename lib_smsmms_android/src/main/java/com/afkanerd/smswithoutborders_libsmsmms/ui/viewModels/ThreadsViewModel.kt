package com.afkanerd.smswithoutborders_libsmsmms.ui.viewModels

import android.content.Context
import android.os.Bundle
import android.provider.BlockedNumberContract.AUTHORITY_URI
import android.provider.Telephony
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.afkanerd.lib_smsmms_android.R
import com.afkanerd.smswithoutborders_libsmsmms.data.entities.Threads
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.ActivitiesConstant
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.blockContact
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.deleteSmsThreads
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.getDatabase
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.isDefault
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.loadRawSmsMmsDb
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.loadRawThreads
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.retrieveContactName
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.retrieveContactPhoto
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.settingsGetDeleteSystem
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.unblockContact
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


open class ThreadsViewModel : ViewModel() {

    var messagesLoading by mutableStateOf(false)
    var secondaryMessagesLoading by mutableStateOf(false)
    var foldOpenConversation by mutableStateOf("")

    enum class InboxType {
        INBOX,
        ARCHIVED,
        BLOCKED,
        DRAFTS,
        MUTED,
        CUSTOM,
        DEVELOPER,
    }


    private val _drawerState =
        MutableStateFlow(DrawerState(DrawerValue.Closed)) // default
    val drawerState: StateFlow<DrawerState> get() = _drawerState.asStateFlow()

    fun toggleDrawerValue() {
        viewModelScope.launch(AndroidUiDispatcher.Main) {
            _drawerState.value.apply {
                if (isClosed) open() else close()
            }
        }
    }

    private val _selectedInbox = MutableLiveData(InboxType.INBOX) // default
    val selectedInbox: LiveData<InboxType> get() = _selectedInbox

    fun setInboxType(type: InboxType) {
        _selectedInbox.value = type
    }

    private val _selectedItems = MutableStateFlow<List<Threads>>(emptyList()) // default
    val selectedItems: StateFlow<List<Threads>> = _selectedItems.asStateFlow()

    fun setSelectedItems(threads: List<Threads>) {
        _selectedItems.value = threads
    }

    fun removeAllSelectedItems() {
        _selectedItems.value = emptyList()
    }

    var pageSize: Int = 200
    var prefetchDistance: Int = 3 * pageSize
    var enablePlaceholder: Boolean = false
    var initialLoadSize: Int = 2 * pageSize
    var maxSize: Int = PagingConfig.MAX_SIZE_UNBOUNDED

    private var threadsPager: Flow<PagingData<ThreadsExtended>>? = null
    private var archivePager: Flow<PagingData<ThreadsExtended>>? = null
    private var draftsPager: Flow<PagingData<ThreadsExtended>>? = null
    private var mutePager: Flow<PagingData<ThreadsExtended>>? = null
    private var blockedPager: Flow<PagingData<ThreadsExtended>>? = null

    fun getThreads(context: Context): Flow<PagingData<ThreadsExtended>> {
        if (threadsPager == null) {
            threadsPager = Pager(
                config = PagingConfig(
                    pageSize,
                    prefetchDistance,
                    enablePlaceholder,
                    initialLoadSize,
                    maxSize
                ),
                pagingSourceFactory = {
                    context.getDatabase().threadsDao()!!.getThreads()
                }
            ).flow.enrich(this, context).cachedIn(viewModelScope)
        }
        return threadsPager!!
    }

    fun getArchives(context: Context): Flow<PagingData<ThreadsExtended>> {
        if (archivePager == null) {
            archivePager = Pager(
                config = PagingConfig(
                    pageSize,
                    prefetchDistance,
                    enablePlaceholder,
                    initialLoadSize,
                    maxSize
                ),
                pagingSourceFactory = {
                    context.getDatabase().threadsDao()!!.getArchived()
                }
            ).flow.enrich(this, context).cachedIn(viewModelScope)
        }
        return archivePager!!
    }

    fun getDrafts(context: Context): Flow<PagingData<ThreadsExtended>> {
        if (draftsPager == null) {
            draftsPager = Pager(
                config = PagingConfig(
                    pageSize,
                    prefetchDistance,
                    enablePlaceholder,
                    initialLoadSize,
                    maxSize
                ),
                pagingSourceFactory = {
                    context.getDatabase().threadsDao()!!
                        .getType(Telephony.Sms.MESSAGE_TYPE_DRAFT)
                }
            ).flow.enrich(this, context).cachedIn(viewModelScope)
        }
        return draftsPager!!
    }

    fun getIsMute(context: Context): Flow<PagingData<ThreadsExtended>> {
        if (mutePager == null) {
            mutePager = Pager(
                config = PagingConfig(
                    pageSize,
                    prefetchDistance,
                    enablePlaceholder,
                    initialLoadSize,
                    maxSize
                ),
                pagingSourceFactory = {
                    context.getDatabase().threadsDao()!!.getIsMute()
                }
            ).flow.enrich(this, context).cachedIn(viewModelScope)
        }
        return mutePager!!
    }

    fun getIsBlocked(context: Context): Flow<PagingData<ThreadsExtended>> {
        if (blockedPager == null) {
            blockedPager = Pager(
                config = PagingConfig(
                    pageSize,
                    prefetchDistance,
                    enablePlaceholder,
                    initialLoadSize,
                    maxSize
                ),
                pagingSourceFactory = {
                    context.getDatabase().threadsDao()!!.getIsBlocked()
                }
            ).flow.enrich(this, context).cachedIn(viewModelScope)
        }
        return blockedPager!!
    }

    fun deleteThreads(context: Context, threads: List<Threads>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                context.getDatabase().threadsDao()?.delete(threads)
                if (context.settingsGetDeleteSystem) {
                    context.deleteSmsThreads(
                        threads
                            .map { it.threadId.toString() }.toTypedArray()
                    )
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
                    if (isBlocked) {
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
                messagesLoading = true

                try {
                    val threads = context.loadRawThreads()
                    threads.forEach { thread ->
                        val conversations = context
                            .loadRawSmsMmsDb(thread.first, thread.second)
                        context.getDatabase().conversationsDao()
                            ?.insertAllThreads(conversations, thread.second)
                        messagesLoading = false
                        secondaryMessagesLoading = true
                    }
                    messagesLoading = false
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    withContext(Dispatchers.Main) {
                        secondaryMessagesLoading = false
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
                AUTHORITY_URI, METHOD_IS_BLOCKED, thread.address, null
            );
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

    private val cache = mutableMapOf<String, StateFlow<String?>>()
    private val contactRepository = ContactRepository()

    fun contactPhoto(context: Context, phoneNumber: String): StateFlow<String?> {
        return cache.getOrPut(phoneNumber) {
            contactRepository
                .contactPhoto(context, phoneNumber)
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = null
                )
        }
    }

    class ContactRepository() {
        fun contactPhoto(context: Context, phoneNumber: String): Flow<String?> = flow {
            val uri = if (!context.isDefault()) "" else context.retrieveContactPhoto(phoneNumber)
            emit(uri)
        }.flowOn(Dispatchers.IO)
    }

    fun execMigrations(context: Context) {
        viewModelScope.launch(Dispatchers.Default) {
            Migrations(this@ThreadsViewModel)
                .migrateV1ToV2(context)
        }
    }

    class Migrations(private val threadsViewModel: ThreadsViewModel) {
        private val dbV2Migration = "dbV2Migration"

        private fun Context.getMigratedV2(): Boolean {
            val sharedPreferences = getSharedPreferences(
                ActivitiesConstant.ACTIVITIES_FILENAMES, Context.MODE_PRIVATE
            )
            return sharedPreferences.getBoolean(dbV2Migration, false)
        }

        private fun Context.setMigratedV2(load: Boolean) {
            val sharedPreferences = getSharedPreferences(
                ActivitiesConstant.ACTIVITIES_FILENAMES, Context.MODE_PRIVATE
            )
            return sharedPreferences.edit {
                putBoolean(dbV2Migration, load)
            }
        }

        fun migrateV1ToV2(context: Context) {
            if (context.isDefault()) {
                val roomVersion = context.getDatabase().openHelper.readableDatabase.version
                if (roomVersion == 2 && !context.getMigratedV2()) {
                    threadsViewModel.loadNativesAsync(context) {
                        CoroutineScope(Dispatchers.Main).launch {
                            context.setMigratedV2(true)
                            Toast.makeText(
                                context,
                                context.getString(R.string.secure_database_migrated),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } else {
                context.setMigratedV2(true)
            }
        }

    }

}

/**
 * This function maps a flow of paging data, such that it contains
 * necessary information that is frequently computed.
 * In this way, we reduce repetitive work that was previously done on the UI.
 */
fun Flow<PagingData<Threads>>.enrich(
    threadsViewModel: ThreadsViewModel,
    context: Context,
): Flow<PagingData<ThreadsExtended>> {
    return this.map { page ->
        page.map { ThreadsExtended(it, threadsViewModel, context) }
    }
}


/**
 * This class provides access to the same fields that a Threads object has,
 * while providing useful additional fields.
 * The additional fields are mostly about values that are lazily computed and cached.
 */
@Immutable
data class ThreadsExtended(
    val raw: Threads,
    private val threadsViewModel: ThreadsViewModel,
    private val context: Context
) {

    val contactName by lazy {
        // Now, why? query the system to fetch a contact for a sender id, when it's technically impossible
        // to store a contact whose "number" is a text-based sender id?
        if (!this.canBeContact) {
            raw.address
        } else {
            try {
                context.retrieveContactName(raw.address)
            } catch (e: Exception) {
                Log.e("ThreadsExtended", "Could not retrieve contact for ${raw.address}", e)
                null
            }
        }
    }

    val contactPhotoUri by lazy {
        // Now, why search for contact photo for an address that is not saveable as a contact?
        // Where should the contact photo come from?
        if (!this.canBeContact) return@lazy null

        threadsViewModel
            .contactPhoto(context, raw.address).value
    }

    val isContact by lazy {
        if (!this.canBeContact) {
            false
        } else {
            !contactName.isNullOrBlank()
        }
    }

    /**
     * This field tells us if the address is saveable as a contact.
     * This helps us reduce unnecessary computation in other areas.
     */
    val canBeContact by lazy {
        // The address can be a contact, if it starts with a numeric value
        // When checking, let's not check the entire sequence. Let's further cut costs, by
        // checking only the first character.
        Regex("^[0-9+]$").matches(raw.address.first().toString())
    }

}
