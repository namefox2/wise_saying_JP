package com.kotoba.takarabako.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kotoba.takarabako.ui.components.FuriganaText
import com.kotoba.takarabako.ui.components.HeartButton
import com.kotoba.takarabako.ui.components.KotobaProgressBar
import com.kotoba.takarabako.ui.components.StepBlock
import com.kotoba.takarabako.ui.theme.LocalAppColors
import com.kotoba.takarabako.viewmodel.QuoteViewModel

@Composable
fun QuoteCardScreen(
    navController: NavController,
    category: String,
    vm: QuoteViewModel = viewModel()
) {
    val colors = LocalAppColors.current
    val quotes by vm.quotes.collectAsState()
    val currentIndex by vm.currentIndex.collectAsState()
    val likedIds by vm.likedIds.collectAsState()

    var stepFurigana by remember { mutableStateOf(false) }
    var stepKorean by remember { mutableStateOf(false) }
    var dragOffset by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(category) {
        vm.loadByCategory(category)
    }

    LaunchedEffect(currentIndex) {
        stepFurigana = false
        stepKorean = false
    }

    val quote = quotes.getOrNull(currentIndex)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bg)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        if (dragOffset < -80f) vm.next()
                        else if (dragOffset > 80f) vm.prev()
                        dragOffset = 0f
                    },
                    onHorizontalDrag = { _, delta -> dragOffset += delta }
                )
            }
    ) {
        // 헤더
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로", tint = colors.textMid)
            }
            Text(
                text = category,
                fontFamily = com.kotoba.takarabako.ui.theme.NotoSerifJP,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = colors.text,
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(androidx.compose.foundation.shape.CircleShape)
                    .background(colors.greenDot)
                    .padding(end = 12.dp)
            )
            Spacer(modifier = Modifier.size(12.dp))
        }

        // 진행 표시
        if (quotes.isNotEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${currentIndex + 1} / ${quotes.size}",
                    fontSize = 11.sp,
                    color = colors.textDim,
                    modifier = Modifier.padding(end = 8.dp)
                )
                KotobaProgressBar(
                    current = currentIndex + 1,
                    total = quotes.size,
                    modifier = Modifier.weight(1f).height(2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 카드
        quote?.let { q ->
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(BorderStroke(1.dp, colors.border), RoundedCornerShape(20.dp))
                    .background(colors.surface)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // 카테고리 배지
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .border(BorderStroke(1.dp, colors.accentBorder), RoundedCornerShape(8.dp))
                        .background(colors.accentBg)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(text = q.cat, fontSize = 10.sp, color = colors.accent)
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 한자 원문
                FuriganaText(
                    segments = q.segments,
                    fontSize = 18.sp,
                    showFurigana = false,
                    textColor = colors.text
                )

                Spacer(modifier = Modifier.height(14.dp))

                // 스텝 블록
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    StepBlock(
                        stepNumber = "①",
                        label = "후리가나",
                        isOpen = stepFurigana,
                        onToggle = { stepFurigana = !stepFurigana }
                    ) {
                        FuriganaText(
                            segments = q.segments,
                            fontSize = 16.sp,
                            showFurigana = true,
                            textColor = colors.text
                        )
                    }
                    StepBlock(
                        stepNumber = "②",
                        label = "한국어",
                        isOpen = stepKorean,
                        onToggle = { stepKorean = !stepKorean }
                    ) {
                        Text(text = q.korean, fontSize = 13.sp, color = colors.textMid)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 저자 + 하트
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "— ${q.author}",
                        fontSize = 11.sp,
                        color = colors.textDim,
                        modifier = Modifier.weight(1f)
                    )
                    HeartButton(
                        isLiked = q.id in likedIds,
                        onToggle = { vm.toggleLike(q.id) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 이전 / 다음 버튼
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(BorderStroke(1.dp, colors.border), RoundedCornerShape(12.dp))
                    .background(colors.surface)
                    .clickable { vm.prev() }
            ) {
                Text(text = "← 이전", fontSize = 13.sp, color = colors.textMid)
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(colors.accentBg)
                    .border(BorderStroke(1.dp, colors.accentBorder), RoundedCornerShape(12.dp))
                    .clickable { vm.next() }
            ) {
                Text(text = "다음 →", fontSize = 13.sp, color = colors.accent, fontWeight = FontWeight.Bold)
            }
        }
    }
}
