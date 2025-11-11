package com.afkanerd.smswithoutborders_libsmsmms.ui.navigation

import android.net.Uri
import kotlinx.serialization.Serializable

@Serializable
data class HomeScreenNav(
    val address: String? = null,
    val query: String? = null,
)

@Serializable
data class ConversationsScreenNav(
    val address: String,
    val query: String? = null,
    val text: String? = null,
    val threadId: Int? = null,
)

@Serializable
data class SearchScreenNav(val address: String? = null)

@Serializable
data class ContactDetailsScreenNav(val address: String)

@Serializable
data class ComposeNewMessageScreenNav(
    var text: String? = null,
    var subscriptionId: Long? = null,
)

@Serializable
object SettingsScreenNav

@Serializable
object DeveloperModeScreen

@Serializable
data class ImageViewScreenNav(
    var contentUri: String,
    var address: String,
    var date: String,
    var filename: String,
    var mimeType: String,
)