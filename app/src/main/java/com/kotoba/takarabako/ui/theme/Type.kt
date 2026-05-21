package com.kotoba.takarabako.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// NotoSerifJP 폰트 설정
// 빌드하기 전에 res/font/noto_serif_jp.ttf 파일을 추가하고 아래 주석 해제:
// import androidx.compose.ui.text.font.Font
// import com.kotoba.takarabako.R
// val NotoSerifJP = FontFamily(
//     Font(R.font.noto_serif_jp, FontWeight.Normal),
//     Font(R.font.noto_serif_jp, FontWeight.Medium),
//     Font(R.font.noto_serif_jp, FontWeight.Bold)
// )
// 폰트 파일 다운로드: https://fonts.google.com/noto/specimen/Noto+Serif+JP
val NotoSerifJP: FontFamily = FontFamily.Serif

val KotobaTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    titleMedium = TextStyle(
        fontFamily = NotoSerifJP,
        fontWeight = FontWeight.Medium,
        fontSize = 17.sp,
        lineHeight = 24.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 14.sp
    )
)
