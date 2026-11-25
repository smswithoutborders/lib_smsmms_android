package com.afkanerd.smswithoutborders_libsmsmms.ui.viewModels

import android.content.Context
import android.os.Bundle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.afkanerd.smswithoutborders_libsmsmms.data.entities.Conversations
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

interface CustomConversationServices {
    fun sendSms(
        context: Context,
        text: String,
        address: String,
        subscriptionId: Long,
        threadId: Int,
        data: ByteArray? = null,
        bundle: Bundle = Bundle(),
        callback: (Conversations?) -> Unit
    )
}

open class CustomsConversationsViewModel : ViewModel(), CustomConversationServices {
    private var _rememberMenuExpanded = MutableStateFlow(false)
    var rememberMenuExpanded = _rememberMenuExpanded.asStateFlow()
        private set

    private var _triggerRequested = MutableStateFlow(false)
    var triggerRequested = _triggerRequested.asStateFlow()
        private set

    private var _address = MutableStateFlow<String?>(null)
    var address = _address.asStateFlow()
        private set

    private var _subscriptionId = MutableStateFlow<Long?>(null)
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

    fun setConversationAddress(address: String) {
        _address.value = address
    }

    fun setConversationSubscriptionId(subscriptionId: Long?) {
        _subscriptionId.value = subscriptionId
    }

    fun triggerAnything(state: Boolean) {
        _triggerRequested.value = state
    }

    fun setRememberMenuExpanded(state: Boolean) {
        _rememberMenuExpanded.value = state
    }

    fun setIsSecured(state: Boolean) {
        _isSecured.value = state
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
        ConversationsViewModel().sendSms(
            context = context,
            text = text,
            address = address,
            subscriptionId = subscriptionId,
            threadId = threadId,
            data = data,
            callback = callback,
            bundle = bundle
        )
    }
}