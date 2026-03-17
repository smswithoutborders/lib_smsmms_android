package com.afkanerd.smswithoutborders.libsmsmms.app.ui

import android.R.style.Theme
import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.afkanerd.smswithoutborders.libsmsmms.app.R
import com.afkanerd.smswithoutborders.libsmsmms.app.ui.theme.Lib_smsmms_androidTheme
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.isDefault
import com.afkanerd.smswithoutborders_libsmsmms.ui.DefaultCheckMain
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ThreadsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    val context: Context = getInstrumentation().targetContext

    val device = UiDevice.getInstance(getInstrumentation())

    @Before
    fun startup() {
        composeTestRule.setContent {
            Lib_smsmms_androidTheme {
                DefaultCheckMain {
                }
            }
        }

        composeTestRule.onNodeWithText("Set default SMS app").performClick()

        val defaultSmsDialog = device.wait(
            Until.hasObject(
                By.textContains(
                "Set ${context.getString(R.string.app_name)} as your default SMS app?")),
            3000
        )
        assertTrue(defaultSmsDialog)

        val myApp = device.findObject(By.text(context.getString(R.string.app_name)))
        myApp.click()

        val setButton = device.findObject(By.text("Set as default"))
        setButton.click()


        Thread.sleep(250)
        assertTrue(context.isDefault())

    }

    @Test
    fun loadingDefaults() {
    }
}