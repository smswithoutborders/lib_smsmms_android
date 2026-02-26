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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.afkanerd.smswithoutborders.libsmsmms.app.ui.theme.Lib_smsmms_androidTheme
import kotlin.jvm.java

class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    private val threadsViewModel: ThreadsViewModel by viewModels()
    private val searchViewModel: SearchViewModel by viewModels()

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
                                    NavHostControllerInstance(
                                        newLayoutInfo = newLayoutInfo,
                                        navController = navController,
                                        threadsViewModel = threadsViewModel,
                                        searchViewModel = searchViewModel,
                                    ) { }
                                }
                            }
                        }
                    }
            }
        }
    }

}
