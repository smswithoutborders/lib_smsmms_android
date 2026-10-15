/*
    This file is concerned with one thing.
    It provides an extension to the Typography class, such that, the typography
    can be modified according to the user's set font size
 */

package com.afkanerd.smswithoutborders_libsmsmms.ui.components.font_scale

import android.content.Context
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.Settings
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.getFontScale

/**
 * This method modifies the font sizes in such a way as to suit the user's font settings.
 */
fun Typography.scale(context: Context): Typography {
    /** The scale factor divided by 100. Here, for example, 1.5 represents 150%, 2.5 is 250%. */
    val factor = context.getFontScale() / 100f

    /**
     * This method multiplies the current value, with the scale factor define in the user's settings.
     * For example, 20sp, under 200%, becomes 40sp
     */
    fun TextStyle.scale(): TextStyle {
        return this.copy(fontSize = this.fontSize * factor)
    }

    return this.copy(
        // After thorough testing, some values were commented out, because, with respect to how they've
        // been used throughout the app, increasing font sizes caused too much jumbling of UI components.
        displayLarge = displayLarge.scale(),
        displayMedium = displayMedium.scale(),
        displaySmall = displaySmall.scale(),
//        headlineLarge = headlineLarge.scale(),
//        headlineMedium = headlineMedium.scale(),
        bodyLarge = bodyLarge.scale(),
        bodyMedium = bodyMedium.scale(),
        bodySmall = bodySmall.scale(),
//        labelLarge = labelLarge.scale(),
//        labelMedium = labelMedium.scale(),
//        labelSmall = labelSmall.scale(),
    )
}

/**
 * This method makes sure, that whenever a user's font size settings change, we'll scale
 * the current typography accordingly.
 */
@Composable
fun Typography.withUserSettings(): State<Typography> {
    val context = LocalContext.current
    val adjustedFonts = remember { mutableStateOf(this.scale(context)) }

    context.getSharedPreferences(Settings.FILENAME, Context.MODE_PRIVATE)
        .registerOnSharedPreferenceChangeListener { _, key ->
            if (key == Settings.FONT_SCALE) {
                // The user changed his preference of font size, so let's update the view.
                adjustedFonts.value = this.scale(context)
            }
        }

    return adjustedFonts
}