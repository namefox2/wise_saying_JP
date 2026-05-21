package com.kotoba.takarabako.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kotoba.takarabako.data.model.Word
import com.kotoba.takarabako.ui.components.FuriganaText
import com.kotoba.takarabako.ui.components.HeartButton
import com.kotoba.takarabako.ui.components.StepBlock
import com.kotoba.takarabako.ui.theme.LocalAppColors
import com.kotoba.takarabako.ui.theme.NotoSerifJP
import com.kotoba.takarabako.viewmodel.JlptViewModel

private fun levelBadgeColor(level: String): Color = when (level) {
    "N1" -> Color(0xFFFF4D6D)
    "N2" -> Color(0xFFFF8C00)
    "N3" -> Color(0xFFC8A96E)
    "N4" -> Color(0xFF4A9EFF)
    "N5" -> Color(0xFF44CC88)
    else -> Color(0xFF888888)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun JlptScreen(
    navController: NavController,
    level: String,
    vm: JlptViewModel = viewModel()
) {
    val colors = LocalAppColors.current
    val words by vm.words.collectAsState()
    val currentLevel by vm.currentLevel.collectAsState()
    val likedWordIds by vm.likedWordIds.collectAsState()

    LaunchedEffect(level) {
        vm.setLevel(level)
    }

    val levelTabs = listOf("all", "N5", "N4", "N3", "N2", "N1")
    val levelLabels = mapOf("all" to "전체", "N1" to "N1", "N2" to "N2", "N3" to "N3", "N4" to "N4", "N5" to "N5")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bg)
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
                text = "JLPT 단어",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = colors.text,
                modifier = Modifier.weight(1f)
            )
        }

        // 레벨 탭
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            levelTabs.forEach { tab ->
                val isSelected = currentLevel == tab
                val badgeColor = if (tab == "all") colors.accent else levelBadgeColor(tab)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            BorderStroke(1.dp, if (isSelected) badgeColor else colors.border),
                            RoundedCornerShape(8.dp)
                        )
                        .background(if (isSelected) badgeColor.copy(alpha = 0.15f) else colors.surface)
                        .clickable { vm.setLevel(tab) }
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = levelLabels[tab] ?: tab,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) badgeColor else colors.textMid
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 단어 목록
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 16.dp, end = 16.dp, bottom = 16.dp
            )
        ) {
            items(words, key = { it.id }) { word ->
                WordCard(
                    word = word,
                    isLiked = word.id in likedWordIds,
                    onToggleLike = { vm.toggleLike(word.id) }
                )
            }
        }
    }
}

@Composable
private fun WordCard(
    word: Word,
    isLiked: Boolean,
    onToggleLike: () -> Unit
) {
    val colors = LocalAppColors.current
    val badgeColor = levelBadgeColor(word.level)

    var stepHiragana by remember(word.id) { mutableStateOf(false) }
    var stepMeaning by remember(word.id) { mutableStateOf(false) }
    var stepExFurigana by remember(word.id) { mutableStateOf(false) }
    var stepExKorean by remember(word.id) { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(BorderStroke(1.dp, colors.border), RoundedCornerShape(14.dp))
            .background(colors.surface)
            .padding(16.dp)
    ) {
        // 상단: 한자 + 레벨 배지 + 품사 배지 + 하트
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = word.kanji,
                fontFamily = NotoSerifJP,
                fontSize = 22.sp,
                color = colors.text,
                modifier = Modifier.weight(1f)
            )
            // 레벨 배지
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(badgeColor.copy(alpha = 0.15f))
                    .border(BorderStroke(1.dp, badgeColor.copy(alpha = 0.5f)), RoundedCornerShape(6.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(text = word.level, fontSize = 10.sp, color = badgeColor, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(6.dp))
            // 품사 배지
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(colors.surface2)
                    .border(BorderStroke(1.dp, colors.border3), RoundedCornerShape(6.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(text = word.pos, fontSize = 10.sp, color = colors.textMid)
            }
            Spacer(modifier = Modifier.width(6.dp))
            HeartButton(isLiked = isLiked, onToggle = onToggleLike, size = 28.dp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 단어 스텝 블록
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            StepBlock(
                stepNumber = "①",
                label = "히라가나",
                isOpen = stepHiragana,
                onToggle = { stepHiragana = !stepHiragana }
            ) {
                Text(text = word.reading, fontSize = 13.sp, color = colors.accent)
            }
            StepBlock(
                stepNumber = "②",
                label = "한국어 뜻",
                isOpen = stepMeaning,
                onToggle = { stepMeaning = !stepMeaning }
            ) {
                Text(text = word.meaning, fontSize = 12.sp, color = colors.textMid)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 예시 문장 박스
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .border(BorderStroke(1.dp, colors.border3), RoundedCornerShape(10.dp))
                .background(colors.bg)
                .padding(12.dp)
        ) {
            Text(
                text = "예시 문장",
                fontSize = 10.sp,
                color = colors.textDim,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            FuriganaText(
                segments = word.exSegments,
                fontSize = 13.sp,
                showFurigana = false,
                textColor = colors.text
            )

            Spacer(modifier = Modifier.height(6.dp))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                StepBlock(
                    stepNumber = "①",
                    label = "후리가나",
                    isOpen = stepExFurigana,
                    onToggle = { stepExFurigana = !stepExFurigana }
                ) {
                    FuriganaText(
                        segments = word.exSegments,
                        fontSize = 13.sp,
                        showFurigana = true,
                        textColor = colors.text
                    )
                }
                StepBlock(
                    stepNumber = "②",
                    label = "한국어 번역",
                    isOpen = stepExKorean,
                    onToggle = { stepExKorean = !stepExKorean }
                ) {
                    Text(text = word.exKorean, fontSize = 12.sp, color = colors.textMid)
                }
            }
        }
    }
}
