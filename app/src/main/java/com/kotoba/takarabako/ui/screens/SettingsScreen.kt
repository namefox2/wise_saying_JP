package com.kotoba.takarabako.ui.screens

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kotoba.takarabako.ui.theme.LocalAppColors
import com.kotoba.takarabako.ui.theme.NotoSerifJP
import com.kotoba.takarabako.viewmodel.FavoritesViewModel
import com.kotoba.takarabako.viewmodel.SettingsViewModel
import kotlin.math.abs

private fun to24Hour(displayHour: Int, isPm: Boolean): Int = when {
    isPm && displayHour == 12 -> 12
    isPm -> displayHour + 12
    displayHour == 12 -> 0
    else -> displayHour
}

private data class ThemeOption(val key: String, val label: String, val emoji: String)

private val themeOptions = listOf(
    ThemeOption("gold", "골드 다크", "🌙"),
    ThemeOption("sakura", "벚꽃 다크", "🌸"),
    ThemeOption("ocean", "오션 다크", "🌊"),
    ThemeOption("paper", "화지 라이트", "📜"),
    ThemeOption("sky", "스카이 라이트", "☀️")
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

    val notifyPermLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> if (granted) vm.toggleNotify() }

    fun onToggleNotify() {
        if (notifyEnabled) { vm.toggleNotify(); return }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val perm = android.Manifest.permission.POST_NOTIFICATIONS
            if (ContextCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_GRANTED) {
                vm.toggleNotify()
            } else {
                notifyPermLauncher.launch(perm)
            }
        } else {
            vm.toggleNotify()
        }
    }
    val autoPlay by vm.autoPlay.collectAsState()
    val autoBlurDelay by vm.autoBlurDelay.collectAsState()
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            themeOptions.forEach { theme ->
                val isSelected = currentTheme == theme.key
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .border(
                            BorderStroke(if (isSelected) 2.dp else 1.dp, if (isSelected) colors.accent else colors.border),
                            RoundedCornerShape(10.dp)
                        )
                        .background(colors.surface)
                        .clickable { vm.setTheme(theme.key) }
                        .padding(vertical = 8.dp, horizontal = 2.dp)
                ) {
                    Text(text = theme.emoji, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = theme.label,
                        fontSize = 7.sp,
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
                onToggle = { onToggleNotify() }
            )
            if (notifyEnabled) {
                SettingsDivider(colors.border2)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val isPm = notifyHour >= 12
                    val displayHour = if (notifyHour == 0) 12 else if (notifyHour > 12) notifyHour - 12 else notifyHour

                    // 1행: 레이블 + 오전/오후 가로 토글
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "알림 시간", fontSize = 13.sp, color = colors.text)
                            Text(text = "매일 이 시간에 알림", fontSize = 11.sp, color = colors.textDim)
                        }
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .border(BorderStroke(1.dp, colors.border), RoundedCornerShape(8.dp))
                                .background(colors.surface2)
                                .padding(2.dp)
                        ) {
                            listOf("오전" to false, "오후" to true).forEach { (label, pm) ->
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isPm == pm) colors.accent else colors.surface2)
                                        .clickable {
                                            val newH = if (pm) {
                                                if (notifyHour == 0) 12 else if (notifyHour < 12) notifyHour + 12 else notifyHour
                                            } else {
                                                if (notifyHour == 12) 0 else if (notifyHour > 12) notifyHour - 12 else notifyHour
                                            }
                                            vm.setNotifyTime(newH, notifyMinute)
                                        }
                                        .padding(horizontal = 14.dp, vertical = 5.dp)
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (isPm == pm) colors.bg else colors.textDim
                                    )
                                }
                            }
                        }
                    }

                    // 2행: 시간 드래그 입력
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TimeDragNumber(
                            value = displayHour, minVal = 1, maxVal = 12,
                            hint = "1–12"
                        ) { vm.setNotifyTime(to24Hour(it, isPm), notifyMinute) }
                        Text(
                            text = ":",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.textMid,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                        TimeDragNumber(
                            value = notifyMinute, minVal = 0, maxVal = 59,
                            hint = "0–59"
                        ) { vm.setNotifyTime(notifyHour, it) }
                    }
                    Text(
                        text = "숫자를 탭하면 직접 입력 · 위아래로 드래그해서 조절",
                        fontSize = 10.sp,
                        color = colors.textDim
                    )
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        val am = context.getSystemService(android.app.AlarmManager::class.java)
                        if (!am.canScheduleExactAlarms()) {
                            Text(
                                text = "⚠️ 정확한 알림을 위해 탭해서 '알람 및 리마인더' 권한을 허용해 주세요",
                                fontSize = 10.sp,
                                color = colors.accent,
                                modifier = Modifier
                                    .padding(top = 6.dp)
                                    .clickable {
                                        context.startActivity(
                                            Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
                                                Uri.parse("package:${context.packageName}"))
                                        )
                                    }
                            )
                        }
                    }
                }
            }
            SettingsDivider(colors.border2)
            SettingsToggleRow(
                title = "카드 자동 오픈",
                description = "카드 진입 시 단계별 자동 공개 후 다음 카드로 이동",
                checked = autoBlur,
                onToggle = { vm.toggleAutoBlur() }
            )
            if (autoBlur) {
                SettingsDivider(colors.border2)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "공개 간격", fontSize = 13.sp, color = colors.text)
                        Text(text = "각 단계 사이의 시간", fontSize = 11.sp, color = colors.textDim)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf(1, 2, 3, 5).forEach { sec ->
                            val isSelected = autoBlurDelay == sec
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(BorderStroke(1.dp, if (isSelected) colors.accent else colors.border), RoundedCornerShape(8.dp))
                                    .background(if (isSelected) colors.accentBg else colors.surface)
                                    .clickable { vm.setAutoBlurDelay(sec) }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = "${sec}초",
                                    fontSize = 12.sp,
                                    color = if (isSelected) colors.accent else colors.textMid,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "일본어명언집 v1.0.7",
            fontSize = 11.sp,
            color = colors.textDim,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )
    }
}

@Composable
private fun TimeDragNumber(
    value: Int,
    minVal: Int,
    maxVal: Int,
    hint: String,
    onValueChange: (Int) -> Unit
) {
    val colors = LocalAppColors.current
    var showDialog by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf("") }
    val currentValue by rememberUpdatedState(value)
    val currentOnChange by rememberUpdatedState(onValueChange)

    if (showDialog) {
        LaunchedEffect(Unit) { inputText = "%02d".format(currentValue) }
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = null,
            text = {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { s ->
                        if (s.length <= 2 && s.all { it.isDigit() }) inputText = s
                    },
                    placeholder = { Text(hint, color = colors.textDim) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    inputText.toIntOrNull()
                        ?.takeIf { it in minVal..maxVal }
                        ?.let(currentOnChange)
                    showDialog = false
                }) { Text("확인", color = colors.accent) }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("취소", color = colors.textMid)
                }
            }
        )
    }

    Text(
        text = "%02d".format(value),
        fontSize = 36.sp,
        fontWeight = FontWeight.Bold,
        color = colors.accent,
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .pointerInput(minVal, maxVal) {
                var accumulated = 0f
                awaitEachGesture {
                    awaitFirstDown(requireUnconsumed = false)
                    accumulated = 0f
                    var totalY = 0f
                    var dragging = false
                    while (true) {
                        val event = awaitPointerEvent()
                        val change = event.changes.firstOrNull() ?: break
                        if (!change.pressed) {
                            if (!dragging) showDialog = true
                            break
                        }
                        val dy = change.position.y - change.previousPosition.y
                        totalY += dy
                        if (abs(totalY) > 8f) dragging = true
                        if (dragging) {
                            change.consume()
                            accumulated += dy
                            while (accumulated <= -24f) {
                                accumulated += 24f
                                val next = if (currentValue + 1 > maxVal) minVal else currentValue + 1
                                currentOnChange(next)
                            }
                            while (accumulated >= 24f) {
                                accumulated -= 24f
                                val next = if (currentValue - 1 < minVal) maxVal else currentValue - 1
                                currentOnChange(next)
                            }
                        }
                    }
                }
            }
    )
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
