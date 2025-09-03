package com.logmind.moodlog.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.logmind.moodlog.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

// 기본 폰트 패밀리들
val pretendardFontFamily = FontFamily.Default // 시스템 기본 폰트로 대체
val systemFontFamily = FontFamily.Default

// Default Material 3 typography values
val baseline = Typography()

val AppTypography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = pretendardFontFamily),
    displayMedium = baseline.displayMedium.copy(fontFamily = pretendardFontFamily),
    displaySmall = baseline.displaySmall.copy(fontFamily = pretendardFontFamily),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = pretendardFontFamily),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = pretendardFontFamily),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = pretendardFontFamily),
    titleLarge = baseline.titleLarge.copy(fontFamily = pretendardFontFamily),
    titleMedium = baseline.titleMedium.copy(fontFamily = pretendardFontFamily),
    titleSmall = baseline.titleSmall.copy(fontFamily = pretendardFontFamily),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = pretendardFontFamily),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = pretendardFontFamily),
    bodySmall = baseline.bodySmall.copy(fontFamily = pretendardFontFamily),
    labelLarge = baseline.labelLarge.copy(fontFamily = pretendardFontFamily),
    labelMedium = baseline.labelMedium.copy(fontFamily = pretendardFontFamily),
    labelSmall = baseline.labelSmall.copy(fontFamily = pretendardFontFamily),
)

@Composable
fun getTypographyForFont(fontFamily: com.logmind.moodlog.domain.entities.FontFamily): Typography {
    val selectedFontFamily = when (fontFamily) {
        com.logmind.moodlog.domain.entities.FontFamily.PRETENDARD -> pretendardFontFamily
        com.logmind.moodlog.domain.entities.FontFamily.LEE_SEOYUN -> pretendardFontFamily // 실제 폰트 리소스로 대체 필요
        com.logmind.moodlog.domain.entities.FontFamily.ORBIT_OF_THE_MOON -> pretendardFontFamily // 실제 폰트 리소스로 대체 필요
        com.logmind.moodlog.domain.entities.FontFamily.RESTART -> pretendardFontFamily // 실제 폰트 리소스로 대체 필요
        com.logmind.moodlog.domain.entities.FontFamily.OVERCOME -> pretendardFontFamily // 실제 폰트 리소스로 대체 필요
        com.logmind.moodlog.domain.entities.FontFamily.SYSTEM -> systemFontFamily
    }
    
    val baseFontSize = fontFamily.fixedFontSize?.sp ?: 16.sp
    
    return Typography(
        displayLarge = baseline.displayLarge.copy(
            fontFamily = selectedFontFamily,
            fontSize = (baseFontSize.value + 16).sp
        ),
        displayMedium = baseline.displayMedium.copy(
            fontFamily = selectedFontFamily,
            fontSize = (baseFontSize.value + 12).sp
        ),
        displaySmall = baseline.displaySmall.copy(
            fontFamily = selectedFontFamily,
            fontSize = (baseFontSize.value + 8).sp
        ),
        headlineLarge = baseline.headlineLarge.copy(
            fontFamily = selectedFontFamily,
            fontSize = (baseFontSize.value + 6).sp
        ),
        headlineMedium = baseline.headlineMedium.copy(
            fontFamily = selectedFontFamily,
            fontSize = (baseFontSize.value + 4).sp
        ),
        headlineSmall = baseline.headlineSmall.copy(
            fontFamily = selectedFontFamily,
            fontSize = (baseFontSize.value + 2).sp
        ),
        titleLarge = baseline.titleLarge.copy(
            fontFamily = selectedFontFamily,
            fontSize = (baseFontSize.value + 1).sp
        ),
        titleMedium = baseline.titleMedium.copy(
            fontFamily = selectedFontFamily,
            fontSize = baseFontSize
        ),
        titleSmall = baseline.titleSmall.copy(
            fontFamily = selectedFontFamily,
            fontSize = (baseFontSize.value - 1).sp
        ),
        bodyLarge = baseline.bodyLarge.copy(
            fontFamily = selectedFontFamily,
            fontSize = baseFontSize
        ),
        bodyMedium = baseline.bodyMedium.copy(
            fontFamily = selectedFontFamily,
            fontSize = (baseFontSize.value - 1).sp
        ),
        bodySmall = baseline.bodySmall.copy(
            fontFamily = selectedFontFamily,
            fontSize = (baseFontSize.value - 2).sp
        ),
        labelLarge = baseline.labelLarge.copy(
            fontFamily = selectedFontFamily,
            fontSize = (baseFontSize.value - 1).sp
        ),
        labelMedium = baseline.labelMedium.copy(
            fontFamily = selectedFontFamily,
            fontSize = (baseFontSize.value - 2).sp
        ),
        labelSmall = baseline.labelSmall.copy(
            fontFamily = selectedFontFamily,
            fontSize = (baseFontSize.value - 3).sp
        ),
    )
}

