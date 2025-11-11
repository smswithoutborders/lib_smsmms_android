package com.afkanerd.smswithoutborders_libsmsmms.ui.viewModels

import android.content.Context
import android.os.Bundle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.afkanerd.smswithoutborders_libsmsmms.data.entities.Conversations

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
    var rememberMenuExpanded by mutableStateOf(false)
        private set

    var anythingTrigger by mutableStateOf(false)
        private set

    var address: String? by mutableStateOf(null)
        private set

    var subscriptionId: Long? by mutableStateOf(null)
        private set

    var threadId: Int? by mutableStateOf(null)
        private set

    var isSecured: Boolean by mutableStateOf(false)
        private set

    fun setIsSecured(isSecured: Boolean) {
        this.isSecured = isSecured
    }

    fun setConversationThreadId(threadId: Int) {
        this.threadId = threadId
    }

    fun setConversationAddress(address: String) {
        this.address = address
    }

    fun setConversationSubscriptionId(subscriptionId: Long?) {
        this.subscriptionId = subscriptionId
    }

    fun setModal(show: Boolean) {
        anythingTrigger = show
        rememberMenuExpanded = !show
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