package com.kotoba.takarabako.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotoba.takarabako.data.model.QuoteSegment
import com.kotoba.takarabako.ui.theme.LocalAppColors
import com.kotoba.takarabako.ui.theme.NotoSerifJP

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

    FlowRow(modifier = modifier) {
        segments.forEach { segment ->
            if (segment.reading.isEmpty()) {
                Text(
                    text = segment.kanji,
                    fontFamily = NotoSerifJP,
                    fontSize = fontSize,
                    color = textColor,
                    lineHeight = fontSize * 1.9
                )
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (showFurigana) {
                        Text(
                            text = segment.reading,
                            fontSize = (fontSize.value * 0.48f).sp,
                            color = colors.furigana,
                            modifier = Modifier
                                .alpha(furiganaAlpha)
                                .padding(bottom = 1.dp)
                        )
                    }
                    Text(
                        text = segment.kanji,
                        fontFamily = NotoSerifJP,
                        fontSize = fontSize,
                        color = textColor,
                        lineHeight = fontSize * 1.9
                    )
                }
            }
        }
    }
}
