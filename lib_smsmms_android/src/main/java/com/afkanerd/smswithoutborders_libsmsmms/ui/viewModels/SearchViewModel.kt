package com.afkanerd.smswithoutborders_libsmsmms.ui.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.afkanerd.smswithoutborders_libsmsmms.data.dao.ThreadsDao
import com.afkanerd.smswithoutborders_libsmsmms.data.entities.Threads
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.getDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class SearchViewModel : ViewModel() {
//    var pageSize: Int = 10
//    var prefetchDistance: Int = 3 * pageSize
//    var enablePlaceholder: Boolean = true
//    var initialLoadSize: Int = 2 * pageSize
//    var maxSize: Int = PagingConfig.Companion.MAX_SIZE_UNBOUNDED

    private val _threadId = MutableStateFlow<Int?>(null)
    val threadId = _threadId.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    lateinit var database: ThreadsDao

    fun clearSearch() {
        _searchQuery.value = ""
        _threadId.value = null
    }

    fun setSearchQuery(context: Context, query: String, threadId: Int?) {
        if(!::database.isInitialized) {
            database = context.getDatabase().threadsDao()!!
        }
        _searchQuery.value = query
        _threadId.value = threadId
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val threads: Flow<PagingData<Threads>> = searchQuery.flatMapLatest { query ->
        if(query.isBlank()) {
            flowOf(PagingData.empty())
        }
        else {
            Pager(
                config = PagingConfig(
                    pageSize = 20,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = {
                    if(_threadId.value == null) database.search(query)
                    else database.search(query, _threadId.value!!)
                }
            ).flow
        }
    }.cachedIn(viewModelScope)
}