package com.afkanerd.smswithoutborders.libsmsmms.app

import android.content.Intent
import androidx.compose.material3.Surface
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.WindowInfoTracker
import com.afkanerd.smswithoutborders_libsmsmms.ui.components.NavHostControllerInstance
import com.afkanerd.smswithoutborders_libsmsmms.ui.viewModels.SearchViewModel
import com.afkanerd.smswithoutborders_libsmsmms.ui.viewModels.ThreadsViewModel
import kotlinx.coroutines.Dispatchers

import android.os.Bundle
import androidx.activity.viewModels
import kotlinx.coroutines.launch
import kotlin.getValue

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import com.afkanerd.smswithoutborders.libsmsmms.app.ui.theme.Lib_smsmms_androidTheme
import com.afkanerd.smswithoutborders_libsmsmms.ui.components.ModalDrawerCustomComposable
import com.afkanerd.smswithoutborders_libsmsmms.ui.viewModels.ConversationsViewModel
import kotlinx.serialization.Serializable
import kotlin.jvm.java


@Serializable
object CustomScreenNav

class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    private val threadsViewModel: ThreadsViewModel by viewModels()
    private val conversationsViewModel: ConversationsViewModel by viewModels()
    private val searchViewModel: SearchViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lifecycleScope.launch(Dispatchers.Main) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                WindowInfoTracker.getOrCreate(this@MainActivity)
                    .windowLayoutInfo(this@MainActivity)
                    .collect { newLayoutInfo ->
                        setContent {
                            navController = rememberNavController()
                            Lib_smsmms_androidTheme {
                                Surface(Modifier
                                    .fillMaxSize()
                                ) {
                                    val customItemSelected by threadsViewModel
                                        .inboxType.collectAsStateWithLifecycle()
                                    NavHostControllerInstance(
                                        appName = stringResource(R.string.app_name),
                                        navController = navController,
                                        threadsViewModel = threadsViewModel,
                                        searchViewModel = searchViewModel,
                                        conversationsViewModel = conversationsViewModel,
                                        modalNavigationModalItems = {
                                            NavigationDrawerItem(
                                                icon = {
                                                },
                                                label = {
                                                    Text(
                                                        "Customized",
                                                        fontSize = 14.sp
                                                    )
                                                },
                                                badge = {
                                                    Text("0", fontSize = 14.sp)
                                                },
                                                selected = customItemSelected == ThreadsViewModel.InboxType.CUSTOM,
                                                onClick = {
                                                    threadsViewModel.setInboxType(
                                                        ThreadsViewModel.InboxType.CUSTOM
                                                    )
                                                    navController.navigate(CustomScreenNav)
                                                    threadsViewModel.toggleDrawerValue()
                                                }
                                            )
                                        }
                                    ) {
                                        composable<CustomScreenNav> {
                                            Scaffold(
                                                topBar = {
                                                    TopAppBar(
                                                        title = {
                                                            Text(stringResource(R.string.app_name))
                                                        },
                                                        navigationIcon = {
                                                            IconButton(onClick = {
                                                                threadsViewModel.toggleDrawerValue()
                                                            }) {
                                                                Icon(
                                                                    imageVector = Icons.Filled.Menu,
                                                                    contentDescription = ""
                                                                )
                                                            }
                                                        },
                                                    )
                                                }
                                            ) { innerPadding ->
                                                Column(
                                                    Modifier
                                                        .padding(innerPadding)
                                                        .fillMaxSize(),
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    verticalArrangement = Arrangement.Center
                                                ) {
                                                    Text("Hello world")
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
            }
        }
    }

}
