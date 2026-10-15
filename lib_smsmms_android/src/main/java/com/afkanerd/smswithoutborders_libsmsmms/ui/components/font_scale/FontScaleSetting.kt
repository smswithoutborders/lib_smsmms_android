/*
    This file is concerned with giving the user a means of varying the base font-size of the app.
    For example, a user may choose to make all text 200% larger.
 */
package com.afkanerd.smswithoutborders_libsmsmms.ui.components.font_scale

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.getFontScale
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.setFontScale
import com.afkanerd.smswithoutborders_libsmsmms.ui.SettingsItem


/**
 * This setting allows the user to vary the base font-size of the app.
 */
@Composable
fun FontScaleSetting() {
    val context = LocalContext.current
    val state = remember { mutableIntStateOf(context.getFontScale()) }
    val isDetailsViewShowing = remember { mutableStateOf(false) }

    CTA(state, onClick = { isDetailsViewShowing.value = true })

    Frame(isDetailsViewShowing) { FullView(state) }
}

/**
 * CTA means Call-To-Action.
 * This is basically the part of the component, which is clicked to reveal the full
 * font-scale settings menu.
 * This component is designed to sit within the Settings view
 */
@Composable
private fun CTA(state: MutableIntState, onClick: () -> Unit) {
    SettingsItem(
        // TODO: Translate this
        itemTitle = "Font Size", itemDescription = "${state.intValue}%"
    ) {
        onClick()
    }
}

/**
 * This component (the full view) is where the user can actually change the font sizes.
 */
@Composable
private fun FullView(state: MutableIntState) {
    // Our goal is to allow the user select from a list of font-sizes, graduated on 25% increments.
    // e.g 100%, 150%, 200%, etc..


    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        for (fontSize in 100..250 step 25) Option(fontSize, state)

    }
}

/**
 * This represents a single font-size option which the user can select, e.g 250%.
 */
@Composable
private fun Option(value: Int, state: MutableState<Int>) {
    val bg = MaterialTheme.colors.run { lerp(onBackground, background, 0.20f) }
    val colorOff = MaterialTheme.colors.background
    val context = LocalContext.current

    Row(
        Modifier
            .clip(RoundedCornerShape(10.dp))
            .fillMaxWidth()
            .background(bg)
            .clickable {
                context.setFontScale(value)
                state.value = value
            }
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            "$value %",
            fontSize = (24.sp) * (value.toFloat() / 100) // Let's create an impression of what the font-sizes would look like.
        )
        RadioButton(
            selected = state.value == value,
            onClick = null,
            colors = RadioButtonDefaults.colors(unselectedColor = colorOff)
        )
    }
}


/**
 * This view is the frame around which the FullView is rendered.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Frame(visibility: MutableState<Boolean>, content: @Composable () -> Unit) {
    if (!visibility.value) return
    val bg = MaterialTheme.colors.run { lerp(onBackground, background, 0.15f) }
    val clearText = TextStyle(color = MaterialTheme.colors.background)

    CompositionLocalProvider(LocalTextStyle provides clearText) {

        ModalBottomSheet(onDismissRequest = { visibility.value = false }) {

            Column(
                Modifier
                    .padding(20.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(bg)
                    .padding(25.dp)
            ) {
                Text("Font Size", fontSize = 32.sp)
                Text("Which are you most-comfortable with?")

                Box(Modifier.height(30.dp))

                Box(
                    Modifier
                        .sizeIn(maxHeight = LocalWindowInfo.current.containerDpSize.height * 0.65f)
                        .verticalScroll(rememberScrollState())
                ) {
                    content()
                }
            }


        }
    }
}

