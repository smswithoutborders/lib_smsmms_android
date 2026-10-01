package com.afkanerd.smswithoutborders.libsmsmms.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.afkanerd.smswithoutborders.libsmsmms.app.R

val UnboundedFontFamily = FontFamily(
    Font(R.font.unbounded_extralight, FontWeight.ExtraLight),
    Font(R.font.unbounded_light, FontWeight.Light),
    Font(R.font.unbounded_regular, FontWeight.Normal),
    Font(R.font.unbounded_medium, FontWeight.Medium),
    Font(R.font.unbounded_semibold, FontWeight.SemiBold),
    Font(R.font.unbounded_bold, FontWeight.Bold),
    Font(R.font.unbounded_extrabold, FontWeight.ExtraBold),
    Font(R.font.unbounded_black, FontWeight.Black),
)
private val DefaultTypography = Typography()
val Typography = DefaultTypography.copy(
    displayLarge = DefaultTypography.displayLarge.copy(
        fontFamily = UnboundedFontFamily,
        fontSize = 50.sp
    ),
    displayMedium = DefaultTypography.displayMedium.copy(
        fontFamily = UnboundedFontFamily,
        fontSize = 40.sp
    ),
    displaySmall = DefaultTypography.displaySmall.copy(
        fontFamily = UnboundedFontFamily,
        fontSize = 30.sp
    ),
    headlineLarge = DefaultTypography.headlineLarge.copy(
        fontFamily = UnboundedFontFamily,
        fontSize = 20.sp
    ),
    headlineMedium = DefaultTypography.headlineMedium.copy(
        fontFamily = UnboundedFontFamily
    ),
    headlineSmall = DefaultTypography.headlineSmall.copy(
        fontFamily = UnboundedFontFamily
    ),
    titleLarge = DefaultTypography.titleLarge.copy(
        fontFamily = UnboundedFontFamily
    ),
    titleMedium = DefaultTypography.titleMedium.copy(
        fontFamily = UnboundedFontFamily
    ),
    titleSmall = DefaultTypography.titleSmall.copy(
        fontFamily = UnboundedFontFamily,
        fontSize = 14.sp
    ),
    bodyLarge = DefaultTypography.bodyLarge.copy(
        fontFamily = UnboundedFontFamily,
        fontSize = 16.sp
    ),
    bodyMedium = DefaultTypography.bodyMedium.copy(
        fontFamily = UnboundedFontFamily,
        fontSize = 14.sp
    ),
    bodySmall = DefaultTypography.bodySmall.copy(
        fontFamily = UnboundedFontFamily,
        fontSize = 12.sp
    ),
    labelLarge = DefaultTypography.labelLarge.copy(
        fontFamily = UnboundedFontFamily,
        fontSize = 14.sp
    ),
    labelMedium = DefaultTypography.labelMedium.copy(
        fontFamily = UnboundedFontFamily,
        fontSize = 12.sp
    ),
    labelSmall = DefaultTypography.labelSmall.copy(
        fontFamily = UnboundedFontFamily,
        fontSize = 11.sp
    )
)