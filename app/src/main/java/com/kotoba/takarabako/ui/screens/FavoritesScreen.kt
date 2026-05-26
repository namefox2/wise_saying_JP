package com.kotoba.takarabako.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kotoba.takarabako.data.model.Quote
import com.kotoba.takarabako.data.model.Word
import com.kotoba.takarabako.ui.components.FuriganaText
import com.kotoba.takarabako.ui.components.HeartButton
import com.kotoba.takarabako.ui.theme.LocalAppColors
import com.kotoba.takarabako.ui.theme.NotoSerifJP
import com.kotoba.takarabako.util.authorDisplay
import com.kotoba.takarabako.viewmodel.FavoritesViewModel

@Composable
fun FavoritesScreen(
    navController: NavController,
    vm: FavoritesViewModel = viewModel()
) {
    val colors = LocalAppColors.current
    val favQuotes by vm.favQuotes.collectAsState()
    val favWords by vm.favWords.collectAsState()
    val activeTab by vm.activeTab.collectAsState()

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
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Text(
                text = "즐겨찾기",
                fontFamily = NotoSerifJP,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = colors.text
            )
        }

        // 탭
        TabRow(
            selectedTabIndex = if (activeTab == "quote") 0 else 1,
            containerColor = colors.bg,
            contentColor = colors.accent
        ) {
            Tab(selected = activeTab == "quote", onClick = { vm.setActiveTab("quote") }) {
                Text(
                    text = "명언 (${favQuotes.size})",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (activeTab == "quote") colors.accent else colors.textDim,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }
            Tab(selected = activeTab == "word", onClick = { vm.setActiveTab("word") }) {
                Text(
                    text = "단어 (${favWords.size})",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (activeTab == "word") colors.accent else colors.textDim,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }
        }

        if (activeTab == "quote") {
            if (favQuotes.isEmpty()) {
                EmptyState("아직 저장한 명언이 없어요")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(favQuotes, key = { it.id }) { quote ->
                        FavQuoteCard(quote = quote, onRemove = { vm.removeQuote(quote.id) })
                    }
                }
            }
        } else {
            if (favWords.isEmpty()) {
                EmptyState("아직 저장한 단어가 없어요")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(favWords, key = { it.id }) { word ->
                        FavWordCard(word = word, onRemove = { vm.removeWord(word.id) })
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyState(message: String) {
    val colors = LocalAppColors.current
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = message, fontSize = 14.sp, color = colors.textDim)
    }
}

@Composable
private fun FavQuoteCard(quote: Quote, onRemove: () -> Unit) {
    val colors = LocalAppColors.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(BorderStroke(1.dp, colors.border), RoundedCornerShape(12.dp))
            .background(colors.surface)
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            FuriganaText(
                segments = quote.segments,
                fontSize = 14.sp,
                showFurigana = true,
                textColor = colors.text,
                modifier = Modifier.weight(1f)
            )
            HeartButton(isLiked = true, onToggle = onRemove, size = 26.dp)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = quote.korean, fontSize = 12.sp, color = colors.textMid)
        Spacer(modifier = Modifier.height(4.dp))
        Row {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(colors.accentBg)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(text = quote.cat, fontSize = 9.sp, color = colors.accent)
            }
        }
        Text(text = "— ${authorDisplay(quote.author)}", fontSize = 10.sp, color = colors.textDim,
            modifier = Modifier.padding(top = 4.dp))
    }
}

@Composable
private fun FavWordCard(word: Word, onRemove: () -> Unit) {
    val colors = LocalAppColors.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(BorderStroke(1.dp, colors.border), RoundedCornerShape(12.dp))
            .background(colors.surface)
            .padding(14.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = word.kanji,
                fontFamily = NotoSerifJP,
                fontSize = 18.sp,
                color = colors.text
            )
            Text(text = word.reading, fontSize = 12.sp, color = colors.accent)
            Text(text = word.meaning, fontSize = 12.sp, color = colors.textMid)
        }
        HeartButton(isLiked = true, onToggle = onRemove, size = 26.dp)
    }
}
