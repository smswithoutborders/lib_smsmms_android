package com.afkanerd.smswithoutborders_libsmsmms.ui.components.conversations

import android.content.Context
import android.provider.BlockedNumberContract.isBlocked
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.afkanerd.lib_smsmms_android.R
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.blockContact
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.unblockContact
import com.afkanerd.smswithoutborders_libsmsmms.ui.backHandler
import com.afkanerd.smswithoutborders_libsmsmms.ui.components.DeleteConfirmationAlert
import com.afkanerd.smswithoutborders_libsmsmms.ui.navigation.SearchScreenNav
import com.afkanerd.smswithoutborders_libsmsmms.ui.viewModels.ConversationsViewModel
import com.afkanerd.smswithoutborders_libsmsmms.ui.viewModels.ThreadsViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun ConversationsMainDropDownMenu(
    expanded: Boolean = true,
    searchCallback: (() -> Unit)? = null,
    blockCallback: (() -> Unit)? = null,
    deleteCallback: (() -> Unit)? = null,
    archiveCallback: (() -> Unit)? = null,
    muteCallback: (() -> Unit)? = null,
    isMute: Boolean = false,
    isBlocked: Boolean = false,
    isArchived: Boolean = false,
    customMenuCallbacks: (@Composable ((Boolean) -> Unit) -> Unit)? = null,
    dismissCallback: (() -> Unit)? = null,
) {
    val expanded = expanded
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentSize(Alignment.TopEnd)
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { dismissCallback?.invoke()},
        ) {

            customMenuCallbacks?.invoke {
                dismissCallback?.invoke()
            }

            HorizontalDivider()

            DropdownMenuItem(
                text = {
                    Text(
                        text=stringResource(R.string.conversations_menu_search_title),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                onClick = {
                    searchCallback?.let{
                        dismissCallback?.invoke()
                        it()
                    }
                }
            )

            DropdownMenuItem(
                text = {
                    Text(
                        text=if(isBlocked) stringResource(R.string.conversations_menu_unblock)
                        else stringResource(R.string.conversation_menu_block),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                onClick = {
                    blockCallback?.let {
                        dismissCallback?.invoke()
                        it()
                    }
                }
            )

            DropdownMenuItem(
                text = {
                    Text(
                        text=if(isArchived) stringResource(R.string.conversation_menu_unarchive)
                        else stringResource(R.string.archive),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                onClick = {
                    archiveCallback?.let {
                        dismissCallback?.invoke()
                        it()
                    }
                }
            )

            DropdownMenuItem(
                text = {
                    Text(
                        text=stringResource(R.string.conversation_menu_delete),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                onClick = {
                    deleteCallback?.let {
                        dismissCallback?.invoke()
                        it()
                    }
                }
            )

            DropdownMenuItem(
                text = {
                    Text(
                        text= if(isMute) stringResource(R.string.conversation_menu_unmute)
                        else stringResource(R.string.conversation_menu_mute),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                onClick = {
                    muteCallback?.let {
                        dismissCallback?.invoke()
                        it()
                    }
                }
            )
        }
    }
}

@Composable
fun ConversationDropDown(
    expanded: Boolean,
    context: Context,
    navController: NavController,
    address: String,
    threadId: Int,
    isMute: Boolean,
    isBlocked: Boolean,
    isArchived: Boolean,
    threadsViewModel: ThreadsViewModel,
    conversationsViewModel: ConversationsViewModel,
    customMenuItems: (@Composable ((Boolean) -> Unit) -> Unit)? = null,
    onArchiveCallback: () -> Unit,
    onDropDownCloseCallback: () -> Unit,
) {
    var rememberDeleteAlert by remember { mutableStateOf(false) }

    ConversationsMainDropDownMenu(
        expanded = expanded,
        isMute = isMute,
        isBlocked = isBlocked,
        isArchived = isArchived,
        searchCallback = {
            navController.navigate(SearchScreenNav(address = address))
        },
        blockCallback = {
            if(isBlocked) {
                threadsViewModel.setIsBlocked(context,
                    listOf(address), false) {
                    context.unblockContact(listOf(address))
                    conversationsViewModel.removeAllSelectedItems()
                }
            }
            else {
                threadsViewModel.setIsBlocked(context, listOf(address), true) {
                    context.blockContact(listOf(address))
                    conversationsViewModel.removeAllSelectedItems()
                }
            }
            conversationsViewModel.toggleIsBlocked()
        },
        deleteCallback = {
            rememberDeleteAlert = true
        },
        archiveCallback = {
            if(isArchived) {
                conversationsViewModel.unArchive(context, threadId) {}
            }
            else {
                conversationsViewModel.archive(context, threadId) {}
            }
            onArchiveCallback()
        },
        muteCallback = {
            if(isMute) {
                conversationsViewModel.unMute(context, threadId) {}
            } else {
                conversationsViewModel.mute(context, threadId) {}
            }
            conversationsViewModel.toggleIsMute()
        },
        customMenuCallbacks = customMenuItems,
        dismissCallback = onDropDownCloseCallback
    )

    if(rememberDeleteAlert) {
        DeleteConfirmationAlert(
            confirmCallback = {
                conversationsViewModel.deleteThread(context, threadId) {
                    rememberDeleteAlert = false
                    navController.popBackStack()
                }
            }
        ) {
            rememberDeleteAlert = false
            conversationsViewModel.removeAllSelectedItems()
        }
    }
}

