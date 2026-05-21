package com.kotoba.takarabako.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import com.kotoba.takarabako.ui.theme.LocalAppColors

@Composable
fun KotobaProgressBar(
    current: Int,
    total: Int,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    val progress = if (total > 0) current.toFloat() / total.toFloat() else 0f

    LinearProgressIndicator(
        progress = { progress },
        modifier = modifier.fillMaxWidth(),
        color = colors.accent,
        trackColor = colors.border,
        strokeCap = StrokeCap.Round
    )
}
