package com.afkanerd.smswithoutborders_libsmsmms.ui.viewModels

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.afkanerd.smswithoutborders_libsmsmms.data.Addresses
import com.afkanerd.smswithoutborders_libsmsmms.data.entities.Conversations
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

open class CustomsConversationsViewModel : ViewModel() {
    private var _rememberMenuExpanded = MutableStateFlow(false)
    var rememberMenuExpanded = _rememberMenuExpanded.asStateFlow()
        private set

    private var _triggerRequested = MutableStateFlow(false)
    var triggerRequested = _triggerRequested.asStateFlow()
        private set

    private var _address = MutableStateFlow<Addresses?>(null)
    var address = _address.asStateFlow()
        private set

    private var _subscriptionId = MutableStateFlow<Long>(-1)
    var subscriptionId = _subscriptionId.asStateFlow()
        private set

    private var _threadId = MutableStateFlow(-1)
    var threadId = _threadId.asStateFlow()
        private set

    private var _isSecured = MutableStateFlow(false)
    var isSecured = _isSecured.asStateFlow()
        private set

    fun setConversationThreadId(threadId: Int) {
        _threadId.value = threadId
    }

    fun setConversationAddress(address: Addresses) {
        _address.value = address
    }

    fun setConversationSubscriptionId(subscriptionId: Long) {
        _subscriptionId.value = subscriptionId
    }

    fun setTrigger(state: Boolean) {
        _triggerRequested.value = state
    }

    fun setRememberMenuExpanded(state: Boolean) {
        _rememberMenuExpanded.value = state
    }

    fun setIsSecured(state: Boolean) {
        _isSecured.value = state
    }
}