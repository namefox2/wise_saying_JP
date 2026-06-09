package com.kotoba.takarabako.ui.screens

import android.content.Intent
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kotoba.takarabako.ui.theme.LocalAppColors
import com.kotoba.takarabako.ui.theme.NotoSerifJP
import com.kotoba.takarabako.viewmodel.FavoritesViewModel
import com.kotoba.takarabako.viewmodel.SettingsViewModel

private data class ThemeOption(val key: String, val label: String, val emoji: String)

private val themeOptions = listOf(
    ThemeOption("gold", "골드 다크", "🌙"),
    ThemeOption("sakura", "벚꽃 다크", "🌸"),
    ThemeOption("ocean", "오션 다크", "🌊"),
    ThemeOption("paper", "화지 라이트", "📜")
)

private val fontScaleOptions = listOf(
    Triple(0.85f, "작게", "가"),
    Triple(1.0f, "보통", "가"),
    Triple(1.2f, "크게", "가"),
    Triple(1.4f, "매우 크게", "가")
)

@Composable
fun SettingsScreen(
    navController: NavController,
    vm: SettingsViewModel = viewModel(),
    favVm: FavoritesViewModel = viewModel()
) {
    val colors = LocalAppColors.current
    val context = LocalContext.current
    val currentTheme by vm.currentTheme.collectAsState()
    val notifyEnabled by vm.notifyEnabled.collectAsState()
    val autoBlur by vm.autoBlur.collectAsState()
    val autoPlay by vm.autoPlay.collectAsState()
    val loginStreak by vm.loginStreak.collectAsState()
    val fontScale by vm.fontScale.collectAsState()
    val notifyHour by vm.notifyHour.collectAsState()
    val notifyMinute by vm.notifyMinute.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bg)
            .verticalScroll(rememberScrollState())
    ) {
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

        // 테마
        SectionHeader(title = "테마")
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(themeOptions) { theme ->
                val isSelected = currentTheme == theme.key
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            BorderStroke(if (isSelected) 2.dp else 1.dp, if (isSelected) colors.accent else colors.border),
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

        // 글씨 크기
        SectionHeader(title = "글씨 크기")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            fontScaleOptions.forEach { (scale, label, sample) ->
                val isSelected = fontScale == scale
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            BorderStroke(if (isSelected) 2.dp else 1.dp, if (isSelected) colors.accent else colors.border),
                            RoundedCornerShape(12.dp)
                        )
                        .background(colors.surface)
                        .clickable { vm.setFontScale(scale) }
                        .padding(vertical = 10.dp, horizontal = 4.dp)
                ) {
                    Text(
                        text = sample,
                        fontSize = (14 * scale).sp,
                        color = if (isSelected) colors.accent else colors.text,
                        fontFamily = NotoSerifJP
                    )
                    Text(
                        text = label,
                        fontSize = 9.sp,
                        color = if (isSelected) colors.accent else colors.textDim,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 기능
        SectionHeader(title = "기능")
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(14.dp))
                .border(BorderStroke(1.dp, colors.border), RoundedCornerShape(14.dp))
                .background(colors.surface)
        ) {
            SettingsToggleRow(
                title = "연속 읽음",
                description = "자동으로 다음 카드로 이동",
                checked = autoPlay,
                onToggle = { vm.toggleAutoPlay() }
            )
            SettingsDivider(colors.border2)
            SettingsFeatureRow(
                icon = "🔊",
                title = "즐겨찾기 내보내기",
                description = "즐겨찾기 명언 및 단어 저장",
                hasChevron = true,
                onClick = {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, "言葉の宝箱 즐겨찾기")
                        putExtra(Intent.EXTRA_TEXT, favVm.buildExportText())
                    }
                    context.startActivity(Intent.createChooser(intent, "즐겨찾기 내보내기"))
                }
            )
            SettingsDivider(colors.border2)
            SettingsFeatureRow(
                icon = "📊",
                title = "학습기록",
                description = "${loginStreak}일 연속 접속 중",
                hasChevron = false
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 학습
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
                onToggle = { vm.toggleNotify() }
            )
            if (notifyEnabled) {
                SettingsDivider(colors.border2)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "알림 시간", fontSize = 13.sp, color = colors.text)
                            Text(text = "매일 이 시간에 알림", fontSize = 11.sp, color = colors.textDim)
                        }
                        // 시 조절
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TimeAdjustButton("-", colors) {
                                vm.setNotifyTime(if (notifyHour == 0) 23 else notifyHour - 1, notifyMinute)
                            }
                            Text(
                                text = "%02d".format(notifyHour),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.accent,
                                modifier = Modifier
                                    .padding(horizontal = 6.dp)
                                    .clickable {
                                        android.app.TimePickerDialog(
                                            context,
                                            { _, h, m -> vm.setNotifyTime(h, m) },
                                            notifyHour,
                                            notifyMinute,
                                            true
                                        ).show()
                                    }
                            )
                            TimeAdjustButton("+", colors) {
                                vm.setNotifyTime(if (notifyHour == 23) 0 else notifyHour + 1, notifyMinute)
                            }
                        }
                        Text(
                            text = ":",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.textMid,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                        // 분 조절
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TimeAdjustButton("-", colors) {
                                vm.setNotifyTime(notifyHour, if (notifyMinute == 0) 55 else notifyMinute - 5)
                            }
                            Text(
                                text = "%02d".format(notifyMinute),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.accent,
                                modifier = Modifier
                                    .padding(horizontal = 6.dp)
                                    .clickable {
                                        android.app.TimePickerDialog(
                                            context,
                                            { _, h, m -> vm.setNotifyTime(h, m) },
                                            notifyHour,
                                            notifyMinute,
                                            true
                                        ).show()
                                    }
                            )
                            TimeAdjustButton("+", colors) {
                                vm.setNotifyTime(notifyHour, if (notifyMinute >= 55) 0 else notifyMinute + 5)
                            }
                        }
                    }
                    Text(
                        text = "시간을 탭하면 직접 입력할 수 있어요",
                        fontSize = 10.sp,
                        color = colors.textDim,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            SettingsDivider(colors.border2)
            SettingsToggleRow(
                title = "블러 자동 해제",
                description = "카드 진입 시 스텝 자동 표시",
                checked = autoBlur,
                onToggle = { vm.toggleAutoBlur() }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

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
private fun TimeAdjustButton(
    label: String,
    colors: com.kotoba.takarabako.ui.theme.AppColors,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(colors.surface2)
            .border(BorderStroke(1.dp, colors.border), RoundedCornerShape(8.dp))
            .clickable { onClick() }
    ) {
        Text(text = label, fontSize = 16.sp, color = colors.accent, fontWeight = FontWeight.Bold)
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
private fun SettingsDivider(color: androidx.compose.ui.graphics.Color) {
    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(color))
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

@Composable
private fun SettingsFeatureRow(
    icon: String,
    title: String,
    description: String,
    hasChevron: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val colors = LocalAppColors.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(text = icon, fontSize = 18.sp, modifier = Modifier.padding(end = 12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 13.sp, color = colors.text)
            Text(text = description, fontSize = 11.sp, color = colors.textDim)
        }
        if (hasChevron) {
            Text(text = "›", fontSize = 20.sp, color = colors.textMid)
        }
    }
}
