package com.logmind.moodlog.domain.entities

data class Settings(
    val hasNotificationEnabled: Boolean = false,
    val hasAutoSyncEnabled: Boolean = false,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val colorTheme: ColorTheme = ColorTheme.BLUE,
    val languageCode: LanguageCode = LanguageCode.KO,
    val aiPersonality: AiPersonality = AiPersonality.BALANCED,
    val fontFamily: FontFamily = FontFamily.RESTART,
    val textAlign: SimpleTextAlign = SimpleTextAlign.LEFT,
    val onboardedLoginTypes: List<String>? = null
)