package com.afkanerd.smswithoutborders_libsmsmms

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.afkanerd.smswithoutborders_libsmsmms.ui.ThreadConversationLayout
import com.afkanerd.smswithoutborders_libsmsmms.ui.viewModels.ThreadsViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ThreadConversationLayoutTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun appNameIsDisplayedOnScreen() {
        composeTestRule.setContent {
            val threadsViewModel: ThreadsViewModel = viewModel()
            ThreadConversationLayout(
                threadsViewModel = threadsViewModel,
                navController = rememberNavController()
            )
        }

        composeTestRule
            .onNodeWithText("DekuSMS")
            .assertIsDisplayed()
    }


    @Test
    fun searchButtonNotShownWhenNotDefault() {
        composeTestRule.setContent {
            val threadsViewModel: ThreadsViewModel = viewModel()
            ThreadConversationLayout(
                threadsViewModel = threadsViewModel,
                navController = rememberNavController()
            )
        }

        composeTestRule
            .onNodeWithContentDescription("Search messages")
            .assertDoesNotExist()
    }

    @Test
    fun composeButtonNotShownWhenNotDefault() {
        composeTestRule.setContent {
            val threadsViewModel: ThreadsViewModel = viewModel()
            ThreadConversationLayout(
                threadsViewModel = threadsViewModel,
                navController = rememberNavController()
            )
        }

        composeTestRule
            .onNodeWithText("Compose")
            .assertDoesNotExist()
    }

    @Test
    fun clickHamburgerMenuOpensDrawer() {
        composeTestRule.setContent {
            val threadsViewModel: ThreadsViewModel = viewModel()
            ThreadConversationLayout(
                threadsViewModel = threadsViewModel,
                navController = rememberNavController()
            )
        }

        composeTestRule
            .onNodeWithContentDescription("Open side menu")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription("Open side menu")
            .performClick()
    }
}