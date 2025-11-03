package com.afkanerd.smswithoutborders_libsmsmms.ui

import android.provider.Telephony
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.afkanerd.lib_smsmms_android.R
import com.afkanerd.smswithoutborders_libsmsmms.activities.DeveloperModeNotificationCls
import com.afkanerd.smswithoutborders_libsmsmms.data.data.models.SmsMmsNatives
import com.afkanerd.smswithoutborders_libsmsmms.data.entities.Conversations
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.getUriForDrawable
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.notify

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeveloperModemMain(
    navController: NavController,
) {
    val context = LocalContext.current
    BackHandler {
        navController.popBackStack()
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Developer mode") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack,
                            "Go back")
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            DeveloperModeItems("Trigger MMS notification") {
                val conversation = Conversations(
                    mms_text = "Hello world MMS"
                ).apply {
                    sms = SmsMmsNatives.Sms(
                        _id = -1,
                        thread_id = -1,
                        address = "+237123456789",
                        date = System.currentTimeMillis(),
                        date_sent = System.currentTimeMillis(),
                        read = 0,
                        status = Telephony.Sms.STATUS_NONE,
                        type = Telephony.Sms.MESSAGE_TYPE_INBOX,
                        body = "Hello world MMS",
                        sub_id = -1,
                    )
                    mms_content_uri = context.getUriForDrawable(R.drawable.egs_cyberpunk2077_cdprojektred_s1_03_2560x1440_359e77d3cd0a40aebf3bbc130d14c5c7)
                        .toString()
                    mms = SmsMmsNatives.Mms(
                        _id = -1,
                        thread_id = -1,
                        date = System.currentTimeMillis(),
                        date_sent = System.currentTimeMillis(),
                        sub = "New MMS",
                        msg_box = Telephony.Mms.MESSAGE_BOX_INBOX,
                    )
                }

                context.notify(conversation, DeveloperModeNotificationCls::class.java)
            }
        }
    }
}

@Composable
fun DeveloperModeItems(
    title: String,
    onClickCallback: () -> Unit,
) {
    HorizontalDivider()
    ListItem(
        headlineContent = { Text(title) },
        modifier = if(LocalInspectionMode.current) Modifier else  Modifier.clickable(onClick = {
            onClickCallback()
        })
    )
}

@Preview(showBackground = true)
@Composable
fun DeveloperModemMain_Preview() {
    DeveloperModemMain(rememberNavController())
}