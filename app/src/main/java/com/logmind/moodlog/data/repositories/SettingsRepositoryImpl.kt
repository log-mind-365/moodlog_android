package com.logmind.moodlog.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.logmind.moodlog.domain.entities.AiPersonality
import com.logmind.moodlog.domain.entities.AppInfo
import com.logmind.moodlog.domain.entities.ColorTheme
import com.logmind.moodlog.domain.entities.FontFamily
import com.logmind.moodlog.domain.entities.LanguageCode
import com.logmind.moodlog.domain.entities.LoginType
import com.logmind.moodlog.domain.entities.SimpleTextAlign
import com.logmind.moodlog.domain.entities.ThemeMode
import com.logmind.moodlog.domain.repositories.SettingsRepository
import kotlinx.coroutines.flow.Flow
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

    override fun getThemeMode(): Flow<ThemeMode> {
        return dataStore.data.map { preferences ->
            preferences[THEME_MODE_KEY]?.let {
                ThemeMode.fromString(it)
            } ?: ThemeMode.SYSTEM
        }
    }

    override fun getLanguageCode(): Flow<LanguageCode> {
        return dataStore.data.map { preferences ->
            preferences[LANGUAGE_CODE_KEY]?.let {
                LanguageCode.fromString(it)
            } ?: LanguageCode.KO
        }
    }

    override suspend fun getAiPersonality(): AiPersonality {
        return dataStore.data.map { preferences ->
            preferences[AI_PERSONALITY_KEY]?.let {
                try {
                    AiPersonality.valueOf(it)
                } catch (e: Exception) {
                    AiPersonality.BALANCED
                }
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

    override fun getFontFamily(): Flow<FontFamily> {
        return dataStore.data.map { preferences ->
            preferences[FONT_FAMILY_KEY]?.let {
                FontFamily.fromString(it)
            } ?: FontFamily.PRETENDARD
        }
    }

    override suspend fun getTextAlign(): SimpleTextAlign {
        return dataStore.data.map { preferences ->
            preferences[TEXT_ALIGN_KEY]?.let {
                SimpleTextAlign.fromString(it)
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
            preferences[THEME_MODE_KEY] = themeMode.value
        }
    }

    override suspend fun updateLanguage(languageCode: LanguageCode) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE_CODE_KEY] = languageCode.value
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
            preferences[COLOR_THEME_KEY] = colorTheme.value
        }
    }

    override suspend fun updateFontFamily(fontFamily: FontFamily) {
        dataStore.edit { preferences ->
            preferences[FONT_FAMILY_KEY] = fontFamily.value
        }
    }

    override suspend fun updateTextAlign(textAlign: SimpleTextAlign) {
        dataStore.edit { preferences ->
            preferences[TEXT_ALIGN_KEY] = textAlign.value
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