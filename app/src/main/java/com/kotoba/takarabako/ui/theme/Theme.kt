package com.kotoba.takarabako.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

val LocalAppColors = compositionLocalOf { appColorsForTheme("gold") }

@Composable
fun KotobaTheme(
    theme: String = "gold",
    content: @Composable () -> Unit
) {
    val colors = appColorsForTheme(theme)

    val materialColors = if (theme == "paper") {
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

    CompositionLocalProvider(LocalAppColors provides colors) {
        MaterialTheme(
            colorScheme = materialColors,
            typography = KotobaTypography,
            content = content
        )
    }
}
