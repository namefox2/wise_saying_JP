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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.kotoba.takarabako.ui.theme.LocalAppColors
import com.kotoba.takarabako.ui.theme.NotoSerifJP
import com.kotoba.takarabako.viewmodel.SettingsViewModel

private data class ThemeOption(val key: String, val label: String, val emoji: String)

@Composable
fun SettingsScreen(
    navController: NavController,
    vm: SettingsViewModel = viewModel()
) {
    val colors = LocalAppColors.current
    val currentTheme by vm.currentTheme.collectAsState()
    val notifyEnabled by vm.notifyEnabled.collectAsState()
    val autoBlur by vm.autoBlur.collectAsState()
    val lastUpdated by vm.lastUpdated.collectAsState()
    val isRefreshing by vm.isRefreshing.collectAsState()

    val themes = listOf(
        ThemeOption("gold", "골드 다크", "🌙"),
        ThemeOption("sakura", "벚꽃 다크", "🌸"),
        ThemeOption("ocean", "오션 다크", "🌊"),
        ThemeOption("paper", "화지 라이트", "📜")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bg)
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
                text = "설정",
                fontFamily = NotoSerifJP,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = colors.text
            )
        }

        // 테마 섹션
        SectionHeader(title = "테마")
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp)
        ) {
            items(themes) { theme ->
                val isSelected = currentTheme == theme.key
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            BorderStroke(
                                if (isSelected) 2.dp else 1.dp,
                                if (isSelected) colors.accent else colors.border
                            ),
                            RoundedCornerShape(12.dp)
                        )
                        .background(colors.surface)
                        .clickable { vm.setTheme(theme.key) }
                        .padding(vertical = 12.dp, horizontal = 8.dp)
                ) {
                    Text(text = theme.emoji, fontSize = 22.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = theme.label,
                        fontSize = 9.sp,
                        color = if (isSelected) colors.accent else colors.textMid,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 데이터 섹션
        SectionHeader(title = "데이터")
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(14.dp))
                .border(BorderStroke(1.dp, colors.border), RoundedCornerShape(14.dp))
                .background(colors.surface)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Text(text = "저장 용량", fontSize = 13.sp, color = colors.textMid, modifier = Modifier.weight(1f))
                Text(text = "약 3~6 MB", fontSize = 12.sp, color = colors.textDim)
            }
            Divider(colors.border2)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { if (!isRefreshing) vm.refreshData() }
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "데이터 새로고침", fontSize = 13.sp, color = colors.text)
                    if (lastUpdated.isNotEmpty()) {
                        Text(text = "마지막: $lastUpdated", fontSize = 10.sp, color = colors.textDim)
                    }
                }
                if (isRefreshing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = colors.accent,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(text = "↻", fontSize = 18.sp, color = colors.accent)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 학습 섹션
        SectionHeader(title = "학습")
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(14.dp))
                .border(BorderStroke(1.dp, colors.border), RoundedCornerShape(14.dp))
                .background(colors.surface)
        ) {
            SettingsToggleRow(
                title = "학습 알림",
                description = "매일 알림으로 학습 유지",
                checked = notifyEnabled,
                onToggle = { vm.toggleNotify() },
            )
            Divider(colors.border2)
            SettingsToggleRow(
                title = "블러 자동 해제",
                description = "카드 진입 시 스텝 자동 표시",
                checked = autoBlur,
                onToggle = { vm.toggleAutoBlur() }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 앱 정보
        Text(
            text = "言葉の宝箱 v1.0.0",
            fontSize = 11.sp,
            color = colors.textDim,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )
    }
}

@Composable
private fun SectionHeader(title: String) {
    val colors = LocalAppColors.current
    Text(
        text = title,
        fontSize = 11.sp,
        color = colors.textDim,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(start = 20.dp, bottom = 8.dp)
    )
}

@Composable
private fun Divider(color: androidx.compose.ui.graphics.Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(color)
    )
}

@Composable
private fun SettingsToggleRow(
    title: String,
    description: String,
    checked: Boolean,
    onToggle: () -> Unit
) {
    val colors = LocalAppColors.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 13.sp, color = colors.text)
            Text(text = description, fontSize = 11.sp, color = colors.textDim)
        }
        Switch(
            checked = checked,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = colors.bg,
                checkedTrackColor = colors.accent,
                uncheckedThumbColor = colors.textDim,
                uncheckedTrackColor = colors.surface2
            )
        )
    }
}
