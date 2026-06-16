package com.kotoba.takarabako.ui.components

import android.os.Build
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
                .clip(RoundedCornerShape(4.dp))
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // API 31+: 실제 Gaussian blur, 탭 시 애니메이션으로 사라짐
                val blurRadius by animateDpAsState(
                    targetValue = if (isRevealed) 0.dp else 20.dp,
                    animationSpec = tween(durationMillis = 350),
                    label = "blur"
                )
                Box(modifier = if (blurRadius > 0.dp) Modifier.blur(blurRadius) else Modifier) {
                    content()
                }
            } else {
                // API 26-30: blur 미지원 → alpha 페이드 + 반투명 오버레이
                val alpha by animateFloatAsState(
                    targetValue = if (isRevealed) 1f else 0f,
                    animationSpec = tween(durationMillis = 300),
                    label = "alpha"
                )
                Box(modifier = Modifier.alpha(alpha)) {
                    content()
                }
                if (!isRevealed) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(colors.border.copy(alpha = 0.4f))
                    )
                }
            }
        }
    }
}
