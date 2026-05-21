package com.kotoba.takarabako.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kotoba.takarabako.ui.theme.LocalAppColors

@Composable
fun HeartButton(
    isLiked: Boolean,
    onToggle: () -> Unit,
    size: Dp = 30.dp
) {
    val colors = LocalAppColors.current
    var triggered by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (triggered) 1.3f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        finishedListener = { triggered = false },
        label = "heartScale"
    )

    val borderColor = if (isLiked) colors.heart.copy(alpha = 0.55f) else colors.border
    val bgColor = if (isLiked) colors.heart.copy(alpha = 0.1f) else Color.Transparent
    val iconTint = if (isLiked) colors.heart else colors.textDim

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(size)
            .scale(scale)
            .clip(RoundedCornerShape(8.dp))
            .border(BorderStroke(1.dp, borderColor), RoundedCornerShape(8.dp))
            .background(bgColor)
            .clickable {
                triggered = true
                onToggle()
            }
    ) {
        Icon(
            imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = if (isLiked) "즐겨찾기 해제" else "즐겨찾기",
            tint = iconTint,
            modifier = Modifier.size(size * 0.55f)
        )
    }
}
