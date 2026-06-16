package com.kotoba.takarabako.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density

val LocalAppColors = compositionLocalOf { appColorsForTheme("gold") }
val LocalFontScale = compositionLocalOf { 1.0f }

@Composable
fun KotobaTheme(
    theme: String = "gold",
    fontScale: Float = 1.0f,
    content: @Composable () -> Unit
) {
    val colors = appColorsForTheme(theme)
    val baseDensity = LocalDensity.current

    val materialColors = if (theme == "paper" || theme == "sky") {
        lightColorScheme(
            background = colors.bg,
            surface = colors.surface,
            primary = colors.accent,
            onBackground = colors.text,
            onSurface = colors.text,
        )
    } else {
        darkColorScheme(
            background = colors.bg,
            surface = colors.surface,
            primary = colors.accent,
            onBackground = colors.text,
            onSurface = colors.text,
        )
    }

    CompositionLocalProvider(
        LocalAppColors provides colors,
        LocalFontScale provides fontScale,
        LocalDensity provides Density(baseDensity.density, baseDensity.fontScale * fontScale)
    ) {
        MaterialTheme(
            colorScheme = materialColors,
            typography = KotobaTypography,
            content = content
        )
    }
}
