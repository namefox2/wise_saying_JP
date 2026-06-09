package com.kotoba.takarabako.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kotoba.takarabako.ui.components.DictionarySelectionContainer
import com.kotoba.takarabako.ui.components.FuriganaText
import com.kotoba.takarabako.ui.components.HeartButton
import com.kotoba.takarabako.ui.components.KotobaProgressBar
import com.kotoba.takarabako.ui.components.StepBlock
import com.kotoba.takarabako.ui.theme.LocalAppColors
import com.kotoba.takarabako.ui.theme.NotoSerifJP
import com.kotoba.takarabako.viewmodel.JlptViewModel
import com.kotoba.takarabako.viewmodel.SettingsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull

private val jlptLevelTabs = listOf("all", "N5", "N4", "N3", "N2", "N1")
private val jlptLevelLabels = mapOf("all" to "전체", "N1" to "N1", "N2" to "N2", "N3" to "N3", "N4" to "N4", "N5" to "N5")

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
    vm: JlptViewModel = viewModel(),
    settingsVm: SettingsViewModel = viewModel()
) {
    val colors = LocalAppColors.current
    val words by vm.words.collectAsState()
    val currentLevel by vm.currentLevel.collectAsState()
    val currentIndex by vm.currentIndex.collectAsState()
    val likedWordIds by vm.likedWordIds.collectAsState()
    val autoBlur by settingsVm.autoBlur.collectAsState()
    val autoPlay by settingsVm.autoPlay.collectAsState()

    var stepHiragana by remember { mutableStateOf(false) }
    var stepMeaning by remember { mutableStateOf(false) }
    var stepExFurigana by remember { mutableStateOf(false) }
    var stepExKorean by remember { mutableStateOf(false) }

    LaunchedEffect(level) { vm.setLevel(level) }

    LaunchedEffect(currentIndex, autoPlay) {
        if (autoPlay) {
            delay(5000)
            vm.next()
        }
    }

    LaunchedEffect(currentIndex) {
        stepHiragana = false
        stepMeaning = false
        stepExFurigana = false
        stepExKorean = false
        if (autoBlur) {
            delay(3000)
            stepHiragana = true
            delay(3000)
            stepMeaning = true
        }
    }

    val word = words.getOrNull(currentIndex)
    val levelTabs = jlptLevelTabs
    val levelLabels = jlptLevelLabels

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
                        .border(BorderStroke(1.dp, if (isSelected) badgeColor else colors.border), RoundedCornerShape(8.dp))
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

        // 진행 표시
        if (words.isNotEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "${currentIndex + 1} / ${words.size}",
                    fontSize = 11.sp,
                    color = colors.textDim,
                    modifier = Modifier.padding(end = 8.dp)
                )
                KotobaProgressBar(
                    current = currentIndex + 1,
                    total = words.size,
                    modifier = Modifier.weight(1f).height(2.dp)
                )
            }
        }

        // 카드 영역 — 좌/우 탭으로 이전/다음
        word?.let { w ->
            val badgeColor = levelBadgeColor(w.level)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(BorderStroke(1.dp, colors.border), RoundedCornerShape(20.dp))
                    .background(colors.surface)
                    .pointerInput(vm) {
                        awaitEachGesture {
                            val down = awaitFirstDown(requireUnconsumed = false)
                            val up = withTimeoutOrNull(200L) { waitForUpOrCancellation() }
                            if (up != null) {
                                if (down.position.x < size.width / 2f) vm.prev() else vm.next()
                            }
                        }
                    }
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // 상단: 레벨 배지 + 품사 배지 + 하트
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(badgeColor.copy(alpha = 0.15f))
                            .border(BorderStroke(1.dp, badgeColor.copy(alpha = 0.5f)), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(text = w.level, fontSize = 11.sp, color = badgeColor, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(colors.surface2)
                            .border(BorderStroke(1.dp, colors.border3), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(text = w.pos, fontSize = 11.sp, color = colors.textMid)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    HeartButton(
                        isLiked = w.id in likedWordIds,
                        onToggle = { vm.toggleLike(w.id) },
                        size = 28.dp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 단어 (한자)
                DictionarySelectionContainer {
                    Text(
                        text = w.kanji,
                        fontFamily = NotoSerifJP,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Medium,
                        color = colors.text
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 단어 스텝
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    StepBlock("히라가나", stepHiragana, { stepHiragana = !stepHiragana }) {
                        Text(text = w.reading, fontSize = 16.sp, color = colors.accent)
                    }
                    StepBlock("한국어 뜻", stepMeaning, { stepMeaning = !stepMeaning }) {
                        Text(text = w.meaning, fontSize = 15.sp, color = colors.textMid)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 예시 문장
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .border(BorderStroke(1.dp, colors.border3), RoundedCornerShape(10.dp))
                        .background(colors.bg)
                        .padding(14.dp)
                ) {
                    Text(
                        text = "예시 문장",
                        fontSize = 10.sp,
                        color = colors.textDim,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    FuriganaText(
                        segments = w.exSegments,
                        fontSize = 15.sp,
                        showFurigana = false,
                        textColor = colors.text
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        StepBlock("후리가나", stepExFurigana, { stepExFurigana = !stepExFurigana }) {
                            FuriganaText(
                                segments = w.exSegments,
                                fontSize = 14.sp,
                                showFurigana = true,
                                textColor = colors.text
                            )
                        }
                        StepBlock("한국어 번역", stepExKorean, { stepExKorean = !stepExKorean }) {
                            Text(text = w.exKorean, fontSize = 13.sp, color = colors.textMid)
                        }
                    }
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
