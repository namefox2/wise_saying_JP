package com.kotoba.takarabako.ui.theme

import androidx.compose.ui.graphics.Color

// ── 골드 다크 (기본) ──────────────────────────────────────────────
object GoldTheme {
    val Bg        = Color(0xFF0A0A12)
    val Surface   = Color(0xFF12121E)
    val Surface2  = Color(0xFF0E0E1A)
    val Border    = Color(0xFF2A2A3A)
    val Border2   = Color(0xFF1E1E2E)
    val Border3   = Color(0xFF1A1A2A)
    val Accent    = Color(0xFFC8A96E)
    val AccentBg  = Color(0x1AC8A96E)
    val AccentBorder = Color(0x44C8A96E)
    val Text      = Color(0xFFE8E0D0)
    val TextMid   = Color(0xFFAAAAAA)
    val TextDim   = Color(0xFF555566)
    val Heart     = Color(0xFFFF4D6D)
    val Furigana  = Color(0x99C8A96E)
    val GreenDot  = Color(0xFF44CC88)
}

// ── 벚꽃 다크 (Sakura) ────────────────────────────────────────────
object SakuraTheme {
    val Bg        = Color(0xFF100A0F)
    val Surface   = Color(0xFF1E1018)
    val Surface2  = Color(0xFF160D14)
    val Border    = Color(0xFF3A2030)
    val Border2   = Color(0xFF2E1826)
    val Border3   = Color(0xFF2A1422)
    val Accent    = Color(0xFFE8829A)
    val AccentBg  = Color(0x1AE8829A)
    val AccentBorder = Color(0x44E8829A)
    val Text      = Color(0xFFF0DDE5)
    val TextMid   = Color(0xFFCCAAAA)
    val TextDim   = Color(0xFF665566)
    val Heart     = Color(0xFFFF4D6D)
    val Furigana  = Color(0x99E8829A)
    val GreenDot  = Color(0xFF44CC88)
}

// ── 오션 다크 (Ocean) ─────────────────────────────────────────────
object OceanTheme {
    val Bg        = Color(0xFF080E18)
    val Surface   = Color(0xFF0E1928)
    val Surface2  = Color(0xFF0A1422)
    val Border    = Color(0xFF1A3050)
    val Border2   = Color(0xFF142440)
    val Border3   = Color(0xFF101E38)
    val Accent    = Color(0xFF4A9EFF)
    val AccentBg  = Color(0x1A4A9EFF)
    val AccentBorder = Color(0x444A9EFF)
    val Text      = Color(0xFFD0E4F8)
    val TextMid   = Color(0xFFAABBCC)
    val TextDim   = Color(0xFF445566)
    val Heart     = Color(0xFFFF4D6D)
    val Furigana  = Color(0x994A9EFF)
    val GreenDot  = Color(0xFF44CC88)
}

// ── 화지 라이트 (Paper) ───────────────────────────────────────────
object PaperTheme {
    val Bg        = Color(0xFFF5F0E8)
    val Surface   = Color(0xFFEDE8DC)
    val Surface2  = Color(0xFFE8E0D0)
    val Border    = Color(0xFFCCC0A8)
    val Border2   = Color(0xFFD8D0C0)
    val Border3   = Color(0xFFDDD5C5)
    val Accent    = Color(0xFF6B5C3E)
    val AccentBg  = Color(0x1A6B5C3E)
    val AccentBorder = Color(0x446B5C3E)
    val Text      = Color(0xFF2C2416)
    val TextMid   = Color(0xFF665544)
    val TextDim   = Color(0xFF998877)
    val Heart     = Color(0xFFCC3355)
    val Furigana  = Color(0x996B5C3E)
    val GreenDot  = Color(0xFF2A8855)
}

// ── 스카이 라이트 (Sky) ───────────────────────────────────────────
object SkyTheme {
    val Bg           = Color(0xFFF3F8FE)
    val Surface      = Color(0xFFE8F2FC)
    val Surface2     = Color(0xFFDDECFA)
    val Border       = Color(0xFFBBD4EE)
    val Border2      = Color(0xFFCCDFF5)
    val Border3      = Color(0xFFD5E7F8)
    val Accent       = Color(0xFF1E6FCC)
    val AccentBg     = Color(0x1A1E6FCC)
    val AccentBorder = Color(0x441E6FCC)
    val Text         = Color(0xFF0C1E30)
    val TextMid      = Color(0xFF3D5570)
    val TextDim      = Color(0xFF7A9AB0)
    val Heart        = Color(0xFFCC3355)
    val Furigana     = Color(0x991E6FCC)
    val GreenDot     = Color(0xFF1A9955)
}

data class AppColors(
    val bg: Color,
    val surface: Color,
    val surface2: Color,
    val border: Color,
    val border2: Color,
    val border3: Color,
    val accent: Color,
    val accentBg: Color,
    val accentBorder: Color,
    val text: Color,
    val textMid: Color,
    val textDim: Color,
    val heart: Color,
    val furigana: Color,
    val greenDot: Color
)

fun appColorsForTheme(theme: String): AppColors = when (theme) {
    "sakura" -> AppColors(
        bg = SakuraTheme.Bg, surface = SakuraTheme.Surface, surface2 = SakuraTheme.Surface2,
        border = SakuraTheme.Border, border2 = SakuraTheme.Border2, border3 = SakuraTheme.Border3,
        accent = SakuraTheme.Accent, accentBg = SakuraTheme.AccentBg, accentBorder = SakuraTheme.AccentBorder,
        text = SakuraTheme.Text, textMid = SakuraTheme.TextMid, textDim = SakuraTheme.TextDim,
        heart = SakuraTheme.Heart, furigana = SakuraTheme.Furigana, greenDot = SakuraTheme.GreenDot
    )
    "ocean" -> AppColors(
        bg = OceanTheme.Bg, surface = OceanTheme.Surface, surface2 = OceanTheme.Surface2,
        border = OceanTheme.Border, border2 = OceanTheme.Border2, border3 = OceanTheme.Border3,
        accent = OceanTheme.Accent, accentBg = OceanTheme.AccentBg, accentBorder = OceanTheme.AccentBorder,
        text = OceanTheme.Text, textMid = OceanTheme.TextMid, textDim = OceanTheme.TextDim,
        heart = OceanTheme.Heart, furigana = OceanTheme.Furigana, greenDot = OceanTheme.GreenDot
    )
    "paper" -> AppColors(
        bg = PaperTheme.Bg, surface = PaperTheme.Surface, surface2 = PaperTheme.Surface2,
        border = PaperTheme.Border, border2 = PaperTheme.Border2, border3 = PaperTheme.Border3,
        accent = PaperTheme.Accent, accentBg = PaperTheme.AccentBg, accentBorder = PaperTheme.AccentBorder,
        text = PaperTheme.Text, textMid = PaperTheme.TextMid, textDim = PaperTheme.TextDim,
        heart = PaperTheme.Heart, furigana = PaperTheme.Furigana, greenDot = PaperTheme.GreenDot
    )
    "sky" -> AppColors(
        bg = SkyTheme.Bg, surface = SkyTheme.Surface, surface2 = SkyTheme.Surface2,
        border = SkyTheme.Border, border2 = SkyTheme.Border2, border3 = SkyTheme.Border3,
        accent = SkyTheme.Accent, accentBg = SkyTheme.AccentBg, accentBorder = SkyTheme.AccentBorder,
        text = SkyTheme.Text, textMid = SkyTheme.TextMid, textDim = SkyTheme.TextDim,
        heart = SkyTheme.Heart, furigana = SkyTheme.Furigana, greenDot = SkyTheme.GreenDot
    )
    else -> AppColors( // gold (default)
        bg = GoldTheme.Bg, surface = GoldTheme.Surface, surface2 = GoldTheme.Surface2,
        border = GoldTheme.Border, border2 = GoldTheme.Border2, border3 = GoldTheme.Border3,
        accent = GoldTheme.Accent, accentBg = GoldTheme.AccentBg, accentBorder = GoldTheme.AccentBorder,
        text = GoldTheme.Text, textMid = GoldTheme.TextMid, textDim = GoldTheme.TextDim,
        heart = GoldTheme.Heart, furigana = GoldTheme.Furigana, greenDot = GoldTheme.GreenDot
    )
}
