package com.kotoba.takarabako.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotoba.takarabako.data.model.QuoteSegment
import com.kotoba.takarabako.ui.theme.LocalAppColors
import com.kotoba.takarabako.ui.theme.NotoSerifJP

private val tightTextStyle = TextStyle(
    platformStyle = PlatformTextStyle(includeFontPadding = false),
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.Both
    )
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FuriganaText(
    segments: List<QuoteSegment>,
    fontSize: TextUnit,
    showFurigana: Boolean,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    val furiganaAlpha by animateFloatAsState(
        targetValue = if (showFurigana) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "furiganaAlpha"
    )
    val furiganaSize = (fontSize.value * 0.48f).sp

    FlowRow(modifier = modifier) {
        segments.forEach { segment ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy((-4).dp)
            ) {
                Text(
                    text = segment.reading.ifEmpty { " " },
                    fontSize = furiganaSize,
                    lineHeight = furiganaSize,
                    color = if (segment.reading.isEmpty()) Color.Transparent
                            else colors.furigana.copy(alpha = furiganaAlpha),
                    style = tightTextStyle
                )
                Text(
                    text = segment.kanji,
                    fontFamily = NotoSerifJP,
                    fontSize = fontSize,
                    lineHeight = fontSize,
                    color = textColor,
                    style = tightTextStyle
                )
            }
        }
    }
}
