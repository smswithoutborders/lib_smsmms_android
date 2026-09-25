package com.afkanerd.smswithoutborders_libsmsmms.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.window.layout.WindowLayoutInfo
import com.afkanerd.lib_smsmms_android.R
import com.afkanerd.smswithoutborders_libsmsmms.data.entities.Conversations
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.isDefault
import com.afkanerd.smswithoutborders_libsmsmms.ui.ComposeNewMessage
import com.afkanerd.smswithoutborders_libsmsmms.ui.ContactDetails
import com.afkanerd.smswithoutborders_libsmsmms.ui.ConversationsMainLayout
import com.afkanerd.smswithoutborders_libsmsmms.ui.DefaultCheckMain
import com.afkanerd.smswithoutborders_libsmsmms.ui.DeveloperModeMain
import com.afkanerd.smswithoutborders_libsmsmms.ui.MediaMain
import com.afkanerd.smswithoutborders_libsmsmms.ui.SearchThreadsMain
import com.afkanerd.smswithoutborders_libsmsmms.ui.SettingsMain
import com.afkanerd.smswithoutborders_libsmsmms.ui.ThreadConversationLayout
import com.afkanerd.smswithoutborders_libsmsmms.ui.navigation.ComposeNewMessageScreenNav
import com.afkanerd.smswithoutborders_libsmsmms.ui.navigation.ContactDetailsScreenNav
import com.afkanerd.smswithoutborders_libsmsmms.ui.navigation.ConversationsScreenNav
import com.afkanerd.smswithoutborders_libsmsmms.ui.navigation.DefaultScreenNav
import com.afkanerd.smswithoutborders_libsmsmms.ui.navigation.DeveloperModeScreen
import com.afkanerd.smswithoutborders_libsmsmms.ui.navigation.HomeScreenNav
import com.afkanerd.smswithoutborders_libsmsmms.ui.navigation.ImageViewScreenNav
import com.afkanerd.smswithoutborders_libsmsmms.ui.navigation.SearchScreenNav
import com.afkanerd.smswithoutborders_libsmsmms.ui.navigation.SettingsScreenNav
import com.afkanerd.smswithoutborders_libsmsmms.ui.requiredReadPhoneStatePermissions
import com.afkanerd.smswithoutborders_libsmsmms.ui.viewModels.ConversationsViewModel
import com.afkanerd.smswithoutborders_libsmsmms.ui.viewModels.CustomsConversationsViewModel
import com.afkanerd.smswithoutborders_libsmsmms.ui.viewModels.SearchViewModel
import com.afkanerd.smswithoutborders_libsmsmms.ui.viewModels.ThreadsViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.settingsGetUseSystemFont

@Composable
fun NavHostControllerInstance(
    navController: NavHostController,
    threadsViewModel: ThreadsViewModel,
    conversationsViewModel: ConversationsViewModel,
    searchViewModel: SearchViewModel?,
    appName: String,
    isDefault: Boolean,
    threadsMainMenuItems: (@Composable ((Boolean) -> Unit) -> Unit)? = null,
    customMenuItems: (@Composable ((Boolean) -> Unit) -> Unit)? = null,
    conversationsCustomComposable: (@Composable (CustomsConversationsViewModel?) -> Unit)? = null,
    conversationsCustomViewModel: CustomsConversationsViewModel? = null,
    conversationsCustomDataView: (@Composable (Conversations) -> Unit)? = null,
    modalNavigationModalItems: (@Composable () -> Unit)? = null,
    customStartDestination: Any? = null,
    customBottomBar: @Composable (() -> Unit)? = null,
    showThreadsTopBar: Boolean = true,
    builder: NavGraphBuilder.() -> Unit,
) {
    val context = LocalContext.current

    var useSystemFont by remember {
        mutableStateOf(context.settingsGetUseSystemFont)
    }

    val currentTypography = MaterialTheme.typography

    val typography = if (useSystemFont) {
        Typography(
            displayLarge = currentTypography.displayLarge.copy(
                fontFamily = FontFamily.Default
            ),
            displayMedium = currentTypography.displayMedium.copy(
                fontFamily = FontFamily.Default
            ),
            displaySmall = currentTypography.displaySmall.copy(
                fontFamily = FontFamily.Default
            ),
            headlineLarge = currentTypography.headlineLarge.copy(
                fontFamily = FontFamily.Default
            ),
            headlineMedium = currentTypography.headlineMedium.copy(
                fontFamily = FontFamily.Default
            ),
            headlineSmall = currentTypography.headlineSmall.copy(
                fontFamily = FontFamily.Default
            ),
            titleLarge = currentTypography.titleLarge.copy(
                fontFamily = FontFamily.Default
            ),
            titleMedium = currentTypography.titleMedium.copy(
                fontFamily = FontFamily.Default
            ),
            titleSmall = currentTypography.titleSmall.copy(
                fontFamily = FontFamily.Default
            ),
            bodyLarge = currentTypography.bodyLarge.copy(
                fontFamily = FontFamily.Default
            ),
            bodyMedium = currentTypography.bodyMedium.copy(
                fontFamily = FontFamily.Default
            ),
            bodySmall = currentTypography.bodySmall.copy(
                fontFamily = FontFamily.Default
            ),
            labelLarge = currentTypography.labelLarge.copy(
                fontFamily = FontFamily.Default
            ),
            labelMedium = currentTypography.labelMedium.copy(
                fontFamily = FontFamily.Default
            ),
            labelSmall = currentTypography.labelSmall.copy(
                fontFamily = FontFamily.Default
            ),
        )
    } else {
        currentTypography
    }

    val drawerState by threadsViewModel.drawerState.collectAsStateWithLifecycle()
    val inboxType by threadsViewModel.inboxType.collectAsStateWithLifecycle()


    var startDestination: Any by remember {
        mutableStateOf(
            customStartDestination
                ?: if (isDefault) HomeScreenNav() else DefaultScreenNav
        )
    }

    LaunchedEffect(isDefault) {
        if (customStartDestination == null && isDefault && startDestination !is HomeScreenNav) {
            startDestination = HomeScreenNav()
        }
    }
    MaterialTheme(
        typography = typography
    ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                if (isDefault) {
                    ModalDrawerSheetLayout(
                        callback = { type ->
                            if (type == ThreadsViewModel.InboxType.DEVELOPER) {
                                navController.navigate(DeveloperModeScreen)
                            } else if (type != ThreadsViewModel.InboxType.CUSTOM) {
                                navController.navigate(HomeScreenNav())
                                threadsViewModel.setInboxType(type)
                            } else {
                                threadsViewModel.setInboxType(type)
                            }
                            threadsViewModel.toggleDrawerValue()
                        },
                        selectedItemIndex = inboxType,
                        customComposable = modalNavigationModalItems,
                    )
                }
            },
        ) {
            NavHost(
                modifier = Modifier,
                navController = navController,
                startDestination = startDestination
            ) {
                builder()

                composable<DefaultScreenNav> {
                    DefaultCheckMain(appName) { default ->
                        if (default) {
                            threadsViewModel.setIsDefault(context.isDefault())
                            navController.navigate(HomeScreenNav()) {
                                popUpTo(navController.graph.id) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                }
                composable<HomeScreenNav> { backStackEntry ->
                    ThreadConversationLayout(
                        threadsViewModel = threadsViewModel,
                        navController = navController,
                        threadsMainMenuItems = threadsMainMenuItems,
                        customBottomBar = customBottomBar,
                        showTopBar = showThreadsTopBar,
                        appName = appName,
                    )
                }
                composable<ConversationsScreenNav> { backStackEntry ->
                    val convScreen: ConversationsScreenNav = backStackEntry.toRoute()
                    ConversationsMainLayout(
                        address = convScreen.address,
                        text = convScreen.text ?: "",
                        searchQuery = convScreen.query,
                        navController = navController,
                        threadId = convScreen.threadId,
                        threadsViewModel = threadsViewModel,
                        conversationsViewModel = conversationsViewModel,
                        customComposable = conversationsCustomComposable,
                        customMenuItems = customMenuItems,
                        customsConversationsViewModel = conversationsCustomViewModel,
                        customDataView = conversationsCustomDataView,
                    )
                }
                composable<SearchScreenNav> { backStackEntry ->
                    val searchScreen: SearchScreenNav = backStackEntry.toRoute()
                    SearchThreadsMain(
                        address = searchScreen.address,
                        searchViewModel = searchViewModel!!,
                        navController = navController
                    )
                }
                composable<ContactDetailsScreenNav> { backStackEntry ->
                    val contactsDetailsScreen: ContactDetailsScreenNav = backStackEntry.toRoute()
                    ContactDetails(
                        address = contactsDetailsScreen.address,
                        navController = navController,
                        isEncryptionEnabled = contactsDetailsScreen.encryptionAvailable,
                        subscriptionId = contactsDetailsScreen.subscriptionId
                    )
                }

                composable<ComposeNewMessageScreenNav> { backStackEntry ->
                    val composeDetailsScreen: ComposeNewMessageScreenNav = backStackEntry.toRoute()
                    ComposeNewMessage(
                        navController = navController,
                        text = composeDetailsScreen.text,
                        subscriptionId = composeDetailsScreen.subscriptionId,
                    )
                }

                composable<SettingsScreenNav> {
                    SettingsMain(navController = navController,
                        onUseSystemFontChanged = { useSystemFont = it}
                    )
                }

                composable<DeveloperModeScreen> {
                    DeveloperModeMain(navController)
                }

                composable<ImageViewScreenNav> { backStackEntry ->
                    val imageViewScreen: ImageViewScreenNav = backStackEntry.toRoute()
                    MediaMain(
                        contentUri = imageViewScreen.contentUri.toUri(),
                        address = imageViewScreen.address,
                        date = imageViewScreen.date,
                        navController = navController,
                        filename = imageViewScreen.filename,
                        mimeType = imageViewScreen.mimeType
                    )
                }
            }
        }
    }
}

@Composable
private fun FoldOpen(
    threadsViewModel: ThreadsViewModel,
    homeScreenNav: HomeScreenNav,
    navController: NavHostController,
) {
    Row {
        Column(modifier = Modifier.fillMaxWidth(0.5f)){
            ThreadConversationLayout(
                threadsViewModel = threadsViewModel,
                navController = navController,
            )
        }

        if(!homeScreenNav.address.isNullOrEmpty()) {
            Column {
//                ConversationsMainLayout(
//                    address = homeScreenNav.address,
//                    threadsViewModel = threadsViewModel,
//                    searchQuery = homeScreenNav.query,
//                    navController = navController,
//                    foldOpen = true,
//                )
            }
        }
        else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NoMessageSelected()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoMessageSelected() {
    Text(
        stringResource(
            R.string.select_a_conversation_from_the_list_on_the_left),
        fontSize = 12.sp,
        textAlign = TextAlign.Center
    )
}
