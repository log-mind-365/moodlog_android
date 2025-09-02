package com.logmind.moodlog.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.logmind.moodlog.domain.entities.*
import com.logmind.moodlog.domain.repositories.SettingsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    companion object {
        private val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
        private val LANGUAGE_CODE_KEY = stringPreferencesKey("language_code")
        private val AI_PERSONALITY_KEY = stringPreferencesKey("ai_personality")
        private val NOTIFICATION_ENABLED_KEY = booleanPreferencesKey("notification_enabled")
        private val AUTO_SYNC_ENABLED_KEY = booleanPreferencesKey("auto_sync_enabled")
        private val COLOR_THEME_KEY = stringPreferencesKey("color_theme")
        private val FONT_FAMILY_KEY = stringPreferencesKey("font_family")
        private val TEXT_ALIGN_KEY = stringPreferencesKey("text_align")
        private val ONBOARDED_LOGIN_TYPES_KEY = stringSetPreferencesKey("onboarded_login_types")
        private val LAST_AI_USAGE_DATE_KEY = stringPreferencesKey("last_ai_usage_date")
        
        private val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    }

    override suspend fun getThemeMode(): ThemeMode {
        return dataStore.data.map { preferences ->
            preferences[THEME_MODE_KEY]?.let { 
                ThemeMode.valueOf(it) 
            } ?: ThemeMode.SYSTEM
        }.first()
    }

    override suspend fun getLanguageCode(): LanguageCode {
        return dataStore.data.map { preferences ->
            preferences[LANGUAGE_CODE_KEY]?.let { 
                LanguageCode.valueOf(it) 
            } ?: LanguageCode.KO
        }.first()
    }

    override suspend fun getAiPersonality(): AiPersonality {
        return dataStore.data.map { preferences ->
            preferences[AI_PERSONALITY_KEY]?.let { 
                AiPersonality.valueOf(it) 
            } ?: AiPersonality.BALANCED
        }.first()
    }

    override suspend fun getHasNotificationEnabled(): Boolean {
        return dataStore.data.map { preferences ->
            preferences[NOTIFICATION_ENABLED_KEY] ?: true
        }.first()
    }

    override suspend fun getHasAutoSyncEnabled(): Boolean {
        return dataStore.data.map { preferences ->
            preferences[AUTO_SYNC_ENABLED_KEY] ?: true
        }.first()
    }

    override suspend fun getColorTheme(): ColorTheme {
        return dataStore.data.map { preferences ->
            preferences[COLOR_THEME_KEY]?.let { 
                ColorTheme.valueOf(it) 
            } ?: ColorTheme.BLUE
        }.first()
    }

    override suspend fun getFontFamily(): FontFamily {
        return dataStore.data.map { preferences ->
            preferences[FONT_FAMILY_KEY]?.let { 
                FontFamily.valueOf(it) 
            } ?: FontFamily.PRETENDARD
        }.first()
    }

    override suspend fun getTextAlign(): SimpleTextAlign {
        return dataStore.data.map { preferences ->
            preferences[TEXT_ALIGN_KEY]?.let { 
                SimpleTextAlign.valueOf(it) 
            } ?: SimpleTextAlign.LEFT
        }.first()
    }

    override suspend fun getOnboardedLoginTypes(): List<String>? {
        return dataStore.data.map { preferences ->
            preferences[ONBOARDED_LOGIN_TYPES_KEY]?.toList()
        }.first()
    }

    override suspend fun getAppInfo(): AppInfo {
        // TODO: BuildConfig에서 실제 앱 정보 가져오기
        return AppInfo(
            appName = "MoodLog",
            packageName = "com.logmind.moodlog",
            version = "1.0.0",
            buildNumber = "1"
        )
    }

    override suspend fun updateAiPersonality(aiPersonality: AiPersonality) {
        dataStore.edit { preferences ->
            preferences[AI_PERSONALITY_KEY] = aiPersonality.name
        }
    }

    override suspend fun updateThemeMode(themeMode: ThemeMode) {
        dataStore.edit { preferences ->
            preferences[THEME_MODE_KEY] = themeMode.name
        }
    }

    override suspend fun updateLanguage(languageCode: LanguageCode) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE_CODE_KEY] = languageCode.name
        }
    }

    override suspend fun updateNotificationEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[NOTIFICATION_ENABLED_KEY] = enabled
        }
    }

    override suspend fun updateAutoSyncEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[AUTO_SYNC_ENABLED_KEY] = enabled
        }
    }

    override suspend fun updateColorTheme(colorTheme: ColorTheme) {
        dataStore.edit { preferences ->
            preferences[COLOR_THEME_KEY] = colorTheme.name
        }
    }

    override suspend fun updateFontFamily(fontFamily: FontFamily) {
        dataStore.edit { preferences ->
            preferences[FONT_FAMILY_KEY] = fontFamily.name
        }
    }

    override suspend fun updateTextAlign(textAlign: SimpleTextAlign) {
        dataStore.edit { preferences ->
            preferences[TEXT_ALIGN_KEY] = textAlign.name
        }
    }

    override suspend fun updateOnboardedLoginTypes(loginType: LoginType) {
        dataStore.edit { preferences ->
            val currentTypes = preferences[ONBOARDED_LOGIN_TYPES_KEY] ?: emptySet()
            preferences[ONBOARDED_LOGIN_TYPES_KEY] = currentTypes + loginType.name
        }
    }

    override suspend fun getLastAiUsageDate(): LocalDateTime? {
        return dataStore.data.map { preferences ->
            preferences[LAST_AI_USAGE_DATE_KEY]?.let { dateString ->
                try {
                    LocalDateTime.parse(dateString, dateTimeFormatter)
                } catch (e: Exception) {
                    null
                }
            }
        }.first()
    }

    override suspend fun updateLastAiUsageDate(date: LocalDateTime) {
        dataStore.edit { preferences ->
            preferences[LAST_AI_USAGE_DATE_KEY] = date.format(dateTimeFormatter)
        }
    }

    override suspend fun clearSharedPreferences() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}