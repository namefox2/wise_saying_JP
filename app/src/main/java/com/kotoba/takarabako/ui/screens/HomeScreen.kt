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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.kotoba.takarabako.ui.components.BlurReveal
import com.kotoba.takarabako.ui.components.FuriganaText
import com.kotoba.takarabako.ui.theme.LocalAppColors
import com.kotoba.takarabako.ui.theme.NotoSerifJP
import com.kotoba.takarabako.util.authorDisplay
import com.kotoba.takarabako.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    vm: HomeViewModel = viewModel()
) {
    val colors = LocalAppColors.current
    val context = LocalContext.current
    val todayQuote by vm.todayQuote.collectAsState()
    val homeCatTab by vm.homeCatTab.collectAsState()
    val categoryCounts by vm.categoryCounts.collectAsState()

    var quoteStepFurigana by remember { mutableStateOf(false) }
    var quoteStepKorean by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bg)
    ) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        // 헤더
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Text(
                text = "言葉の宝箱",
                fontFamily = NotoSerifJP,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = colors.accent,
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(colors.greenDot)
            )
        }

        // 오늘의 명언 카드
        todayQuote?.let { quote ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(BorderStroke(1.dp, colors.border), RoundedCornerShape(20.dp))
                    .background(colors.surface)
                    .padding(18.dp)
            ) {
                Text(
                    text = "오늘의 명언",
                    fontSize = 10.sp,
                    color = colors.textDim,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                FuriganaText(
                    segments = quote.segments,
                    fontSize = 15.sp,
                    showFurigana = false,
                    textColor = colors.text
                )
                Text(
                    text = "— ${authorDisplay(quote.author)}",
                    fontSize = 11.sp,
                    color = colors.textDim,
                    modifier = Modifier.padding(top = 6.dp, bottom = 10.dp)
                )
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    BlurReveal(
                        label = "후리가나",
                        isRevealed = quoteStepFurigana,
                        onToggle = { quoteStepFurigana = !quoteStepFurigana }
                    ) {
                        FuriganaText(
                            segments = quote.segments,
                            fontSize = 14.sp,
                            showFurigana = true,
                            textColor = colors.text
                        )
                    }
                    BlurReveal(
                        label = "한국어",
                        isRevealed = quoteStepKorean,
                        onToggle = { quoteStepKorean = !quoteStepKorean }
                    ) {
                        Text(text = quote.korean, fontSize = 13.sp, color = colors.textMid)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 배너 광고
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            factory = { ctx ->
                AdView(ctx).apply {
                    setAdSize(AdSize.BANNER)
                    adUnitId = "ca-app-pub-9392221797396472/3762447349"
                    loadAd(AdRequest.Builder().build())
                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 탭 (명言 / JLPT)
        TabRow(
            selectedTabIndex = if (homeCatTab == "quote") 0 else 1,
            containerColor = colors.bg,
            contentColor = colors.accent,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(selected = homeCatTab == "quote", onClick = { vm.setHomeCatTab("quote") }) {
                Text(
                    text = "명언",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (homeCatTab == "quote") colors.accent else colors.textDim,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }
            Tab(selected = homeCatTab == "jlpt", onClick = { vm.setHomeCatTab("jlpt") }) {
                Text(
                    text = "JLPT",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (homeCatTab == "jlpt") colors.accent else colors.textDim,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }
        }

        if (homeCatTab == "quote") {
            QuoteCategoryGrid(navController = navController, categoryCounts = categoryCounts)
        } else {
            JlptLevelGrid(navController = navController)
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
    } // Box
}

private val quoteCategoryItems = listOf(
    Pair("📚", "전체"),
    Pair("💪", "노력"),
    Pair("🏆", "성공"),
    Pair("❤️", "사랑"),
    Pair("🌱", "인생"),
    Pair("📖", "학습"),
    Pair("🌸", "마음"),
    Pair("💬", "속담"),
    Pair("✨", "기타")
)

@Composable
private fun QuoteCategoryGrid(navController: NavController, categoryCounts: Map<String, Int>) {
    val colors = LocalAppColors.current
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        quoteCategoryItems.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { (icon, name) ->
                    val count = categoryCounts[name]
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .border(BorderStroke(1.dp, colors.border), RoundedCornerShape(12.dp))
                            .background(colors.surface)
                            .clickable { navController.navigate("quote/$name") }
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        Text(text = icon, fontSize = 16.sp)
                        Column(modifier = Modifier.padding(start = 8.dp)) {
                            Text(text = name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = colors.text)
                            Text(
                                text = if (count != null) "${count}개" else "",
                                fontSize = 10.sp,
                                color = colors.textDim
                            )
                        }
                    }
                }
                if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

private val jlptLevelItems = listOf(
    Triple("📚", "전체", "750"),
    Triple("🔴", "N1", "150"),
    Triple("🟠", "N2", "150"),
    Triple("🟡", "N3", "150"),
    Triple("🔵", "N4", "150"),
    Triple("🟢", "N5", "150")
)

@Composable
private fun JlptLevelGrid(navController: NavController) {
    val colors = LocalAppColors.current
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        jlptLevelItems.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { (icon, name, count) ->
                    val route = if (name == "전체") "jlpt/all" else "jlpt/$name"
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .border(BorderStroke(1.dp, colors.border), RoundedCornerShape(12.dp))
                            .background(colors.surface)
                            .clickable { navController.navigate(route) }
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        Text(text = icon, fontSize = 16.sp)
                        Column(modifier = Modifier.padding(start = 8.dp)) {
                            Text(text = name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = colors.text)
                            Text(text = "${count}개", fontSize = 10.sp, color = colors.textDim)
                        }
                    }
                }
                if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}
