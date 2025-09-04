package com.logmind.moodlog.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.logmind.moodlog.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val JostFont = FontFamily(
    Font(R.font.jost_light, FontWeight.Light),
    Font(R.font.jost_normal, FontWeight.Normal),
    Font(R.font.jost_medium, FontWeight.Medium),
    Font(R.font.jost_bold, FontWeight.Bold)
)

val baseline = Typography()

val AppTypography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = JostFont),
    displayMedium = baseline.displayMedium.copy(fontFamily = JostFont),
    displaySmall = baseline.displaySmall.copy(fontFamily = JostFont),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = JostFont),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = JostFont),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = JostFont),
    titleLarge = baseline.titleLarge.copy(fontFamily = JostFont),
    titleMedium = baseline.titleMedium.copy(fontFamily = JostFont),
    titleSmall = baseline.titleSmall.copy(fontFamily = JostFont),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = JostFont),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = JostFont),
    bodySmall = baseline.bodySmall.copy(fontFamily = JostFont),
    labelLarge = baseline.labelLarge.copy(fontFamily = JostFont),
    labelMedium = baseline.labelMedium.copy(fontFamily = JostFont),
    labelSmall = baseline.labelSmall.copy(fontFamily = JostFont),
)
