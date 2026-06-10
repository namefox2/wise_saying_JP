package com.kotoba.takarabako.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotoba.takarabako.ui.theme.LocalAppColors

@Composable
fun BlurReveal(
    label: String,
    isRevealed: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    val colors = LocalAppColors.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .border(BorderStroke(1.dp, colors.border3), RoundedCornerShape(10.dp))
            .background(colors.surface2)
            .clickable { onToggle() }
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 10.sp,
                color = colors.textDim,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = if (isRevealed) "숨기기" else "탭하여 보기",
                fontSize = 10.sp,
                color = colors.accent
            )
        }
        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .then(if (!isRevealed) Modifier.blur(12.dp) else Modifier)
        ) {
            content()
        }
    }
}
