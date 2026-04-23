package com.afkanerd.smswithoutborders_libsmsmms

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.afkanerd.smswithoutborders_libsmsmms.ui.DefaultCheckMain
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DefaultCheckMainTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun clickSetDefaultButton() {
        composeTestRule.setContent {
            DefaultCheckMain()
        }

        composeTestRule
        composeTestRule
            .onNodeWithText("Set default SMS app")
            .performClick()
    }
}