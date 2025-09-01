package com.logmind.moodlog.domain.repositories

import com.logmind.moodlog.domain.entities.*
import java.time.LocalDateTime

interface SettingsRepository {
    suspend fun getThemeMode(): ThemeMode

    suspend fun getLanguageCode(): LanguageCode

    suspend fun getAiPersonality(): AiPersonality

    suspend fun getHasNotificationEnabled(): Boolean

    suspend fun getHasAutoSyncEnabled(): Boolean

    suspend fun getColorTheme(): ColorTheme

    suspend fun getFontFamily(): FontFamily

    suspend fun getTextAlign(): SimpleTextAlign

    suspend fun getOnboardedLoginTypes(): List<String>?

    suspend fun getAppInfo(): AppInfo

    suspend fun updateAiPersonality(aiPersonality: AiPersonality)

    suspend fun updateThemeMode(themeMode: ThemeMode)

    suspend fun updateLanguage(languageCode: LanguageCode)

    suspend fun updateNotificationEnabled(enabled: Boolean)

    suspend fun updateAutoSyncEnabled(enabled: Boolean)

    suspend fun updateColorTheme(colorTheme: ColorTheme)

    suspend fun updateFontFamily(fontFamily: FontFamily)

    suspend fun updateTextAlign(textAlign: SimpleTextAlign)

    suspend fun updateOnboardedLoginTypes(loginType: LoginType)

    suspend fun getLastAiUsageDate(): LocalDateTime?

    suspend fun updateLastAiUsageDate(date: LocalDateTime)

    suspend fun clearSharedPreferences()
}