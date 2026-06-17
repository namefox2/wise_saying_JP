package com.kotoba.takarabako.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kotoba.takarabako.ui.components.BlurReveal
import com.kotoba.takarabako.ui.components.cardSwipe
import com.kotoba.takarabako.ui.components.DictionarySelectionContainer
import com.kotoba.takarabako.ui.components.FuriganaText
import com.kotoba.takarabako.ui.components.HeartButton
import com.kotoba.takarabako.ui.components.KotobaProgressBar
import com.kotoba.takarabako.ui.theme.LocalAppColors
import com.kotoba.takarabako.ui.theme.NotoSerifJP
import com.kotoba.takarabako.util.authorDisplay
import com.kotoba.takarabako.viewmodel.QuoteViewModel
import com.kotoba.takarabako.viewmodel.SettingsViewModel
import kotlinx.coroutines.delay

@Composable
fun QuoteCardScreen(
    navController: NavController,
    category: String,
    vm: QuoteViewModel = viewModel(),
    settingsVm: SettingsViewModel = viewModel()
) {
    val colors = LocalAppColors.current
    val quotes by vm.quotes.collectAsState()
    val currentIndex by vm.currentIndex.collectAsState()
    val likedIds by vm.likedIds.collectAsState()
    val bookmarkedId by vm.bookmarkedId.collectAsState()
    val isShuffled by vm.isShuffled.collectAsState()
    val autoBlur by settingsVm.autoBlur.collectAsState()
    val autoPlay by settingsVm.autoPlay.collectAsState()

    var stepFurigana by remember { mutableStateOf(false) }
    var stepKorean by remember { mutableStateOf(false) }

    LaunchedEffect(category) { vm.loadByCategory(category) }

    LaunchedEffect(currentIndex, autoPlay) {
        if (autoPlay) {
            delay(5000)
            vm.next()
        }
    }

    LaunchedEffect(currentIndex) {
        stepFurigana = false
        stepKorean = false
        if (autoBlur) {
            delay(3000)
            stepFurigana = true
            delay(3000)
            stepKorean = true
        }
    }

    val quote = quotes.getOrNull(currentIndex)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bg)
    ) {
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
                fontFamily = NotoSerifJP,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = colors.text,
                modifier = Modifier.weight(1f)
            )
            if (isShuffled) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(colors.surface2)
                        .border(BorderStroke(1.dp, colors.border), RoundedCornerShape(8.dp))
                        .clickable { vm.resetOrder() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(text = "초기화", fontSize = 12.sp, color = colors.textMid, fontWeight = FontWeight.Medium)
                }
                Spacer(modifier = Modifier.width(6.dp))
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(colors.accentBg)
                    .border(BorderStroke(1.dp, colors.accentBorder), RoundedCornerShape(8.dp))
                    .clickable { vm.shuffle() }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Shuffle, contentDescription = "섞기", tint = colors.accent, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "섞기", fontSize = 12.sp, color = colors.accent, fontWeight = FontWeight.Medium)
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(colors.greenDot)
            )
            Spacer(modifier = Modifier.size(12.dp))
        }

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

        quote?.let { q ->
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(BorderStroke(1.dp, colors.border), RoundedCornerShape(20.dp))
                    .background(colors.surface)
                    .cardSwipe(vm, onPrev = { vm.prev() }, onNext = { vm.next() })
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
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

                DictionarySelectionContainer {
                    FuriganaText(
                        segments = q.segments,
                        fontSize = 18.sp,
                        showFurigana = false,
                        textColor = colors.text
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    BlurReveal("후리가나", stepFurigana, { stepFurigana = !stepFurigana }) {
                        FuriganaText(
                            segments = q.segments,
                            fontSize = 16.sp,
                            showFurigana = true,
                            textColor = colors.text
                        )
                    }
                    BlurReveal("한국어", stepKorean, { stepKorean = !stepKorean }) {
                        Text(text = q.korean, fontSize = 13.sp, color = colors.textMid)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "— ${authorDisplay(q.author)}",
                        fontSize = 11.sp,
                        color = colors.textDim,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { vm.toggleBookmark() }, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Filled.Bookmark,
                            contentDescription = "책갈피",
                            tint = if (bookmarkedId == q.id) colors.accent else colors.border,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(2.dp))
                    HeartButton(isLiked = q.id in likedIds, onToggle = { vm.toggleLike(q.id) })
                }

            }
        }

        Spacer(modifier = Modifier.height(12.dp))

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
