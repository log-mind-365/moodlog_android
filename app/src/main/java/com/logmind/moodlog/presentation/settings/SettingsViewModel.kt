package com.logmind.moodlog.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.logmind.moodlog.domain.entities.*
import com.logmind.moodlog.domain.repositories.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val languageCode: LanguageCode = LanguageCode.KO,
    val colorTheme: ColorTheme = ColorTheme.BLUE,
    val fontFamily: FontFamily = FontFamily.PRETENDARD,
    val textAlign: SimpleTextAlign = SimpleTextAlign.LEFT,
    val aiPersonality: AiPersonality = AiPersonality.BALANCED,
    val hasNotificationEnabled: Boolean = true,
    val hasAutoSyncEnabled: Boolean = true,
    val appInfo: AppInfo = AppInfo(
        appName = "MoodLog",
        packageName = "com.logmind.moodlog",
        version = "1.0.0",
        buildNumber = "1"
    ),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    fun updateThemeMode(themeMode: ThemeMode) {
        viewModelScope.launch {
            try {
                settingsRepository.updateThemeMode(themeMode)
                _uiState.update { it.copy(themeMode = themeMode) }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(errorMessage = "테마 변경 실패: ${e.message}")
                }
            }
        }
    }

    fun updateLanguage(languageCode: LanguageCode) {
        viewModelScope.launch {
            try {
                settingsRepository.updateLanguage(languageCode)
                _uiState.update { it.copy(languageCode = languageCode) }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(errorMessage = "언어 변경 실패: ${e.message}")
                }
            }
        }
    }

    fun updateColorTheme(colorTheme: ColorTheme) {
        viewModelScope.launch {
            try {
                settingsRepository.updateColorTheme(colorTheme)
                _uiState.update { it.copy(colorTheme = colorTheme) }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(errorMessage = "컬러 테마 변경 실패: ${e.message}")
                }
            }
        }
    }

    fun updateFontFamily(fontFamily: FontFamily) {
        viewModelScope.launch {
            try {
                settingsRepository.updateFontFamily(fontFamily)
                _uiState.update { it.copy(fontFamily = fontFamily) }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(errorMessage = "폰트 변경 실패: ${e.message}")
                }
            }
        }
    }

    fun updateTextAlign(textAlign: SimpleTextAlign) {
        viewModelScope.launch {
            try {
                settingsRepository.updateTextAlign(textAlign)
                _uiState.update { it.copy(textAlign = textAlign) }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(errorMessage = "텍스트 정렬 변경 실패: ${e.message}")
                }
            }
        }
    }

    fun updateAiPersonality(aiPersonality: AiPersonality) {
        viewModelScope.launch {
            try {
                settingsRepository.updateAiPersonality(aiPersonality)
                _uiState.update { it.copy(aiPersonality = aiPersonality) }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(errorMessage = "AI 성격 변경 실패: ${e.message}")
                }
            }
        }
    }

    fun updateNotificationEnabled(enabled: Boolean) {
        viewModelScope.launch {
            try {
                settingsRepository.updateNotificationEnabled(enabled)
                _uiState.update { it.copy(hasNotificationEnabled = enabled) }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(errorMessage = "알림 설정 변경 실패: ${e.message}")
                }
            }
        }
    }

    fun updateAutoSyncEnabled(enabled: Boolean) {
        viewModelScope.launch {
            try {
                settingsRepository.updateAutoSyncEnabled(enabled)
                _uiState.update { it.copy(hasAutoSyncEnabled = enabled) }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(errorMessage = "자동 동기화 설정 변경 실패: ${e.message}")
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun loadSettings() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                
                val themeMode = settingsRepository.getThemeMode()
                val languageCode = settingsRepository.getLanguageCode()
                val colorTheme = settingsRepository.getColorTheme()
                val fontFamily = settingsRepository.getFontFamily()
                val textAlign = settingsRepository.getTextAlign()
                val aiPersonality = settingsRepository.getAiPersonality()
                val hasNotificationEnabled = settingsRepository.getHasNotificationEnabled()
                val hasAutoSyncEnabled = settingsRepository.getHasAutoSyncEnabled()
                val appInfo = settingsRepository.getAppInfo()

                _uiState.update {
                    it.copy(
                        themeMode = themeMode,
                        languageCode = languageCode,
                        colorTheme = colorTheme,
                        fontFamily = fontFamily,
                        textAlign = textAlign,
                        aiPersonality = aiPersonality,
                        hasNotificationEnabled = hasNotificationEnabled,
                        hasAutoSyncEnabled = hasAutoSyncEnabled,
                        appInfo = appInfo,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        errorMessage = "설정 로드 실패: ${e.message}"
                    )
                }
            }
        }
    }
}