package com.afkanerd.smswithoutborders_libsmsmms

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.afkanerd.smswithoutborders_libsmsmms.data.data.models.Contacts
import com.afkanerd.smswithoutborders_libsmsmms.ui.ComposeNewMessage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ComposeNewMessageTest {

    private val fakeContacts = listOf(
        Contacts(1L, "+237612345678", "Test User One"),
        Contacts(2L, "+237687654321", "Test User Two"),
    )

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun composeScreenTitleIsDisplayed() {
        composeTestRule.setContent {
            ComposeNewMessage(
                navController = rememberNavController(),
                _items = fakeContacts
            )
        }

        composeTestRule
            .onNodeWithText("New conversation")
            .assertIsDisplayed()
    }

    @Test
    fun fakeContactIsDisplayedOnScreen() {
        composeTestRule.setContent {
            ComposeNewMessage(
                navController = rememberNavController(),
                _items = fakeContacts
            )
        }

        composeTestRule
            .onNodeWithText("Test User One")
            .assertIsDisplayed()
    }
}