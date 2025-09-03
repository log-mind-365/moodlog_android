package com.logmind.moodlog.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.logmind.moodlog.domain.entities.ColorTheme
import com.logmind.moodlog.domain.entities.FontFamily
import com.logmind.moodlog.domain.entities.LanguageCode
import com.logmind.moodlog.domain.entities.ThemeMode
import com.logmind.moodlog.domain.repositories.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val languageCode: LanguageCode = LanguageCode.KO,
    val colorTheme: ColorTheme = ColorTheme.BLUE,
    val fontFamily: FontFamily = FontFamily.PRETENDARD,
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
        // 설정 화면에 진입할 때만 현재 설정을 로드
        loadCurrentSettings()
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

    private fun loadCurrentSettings() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                val themeMode = settingsRepository.getThemeMode().first()
                val languageCode = settingsRepository.getLanguageCode().first()
                val fontFamily = settingsRepository.getFontFamily().first()

                _uiState.update {
                    it.copy(
                        themeMode = themeMode,
                        languageCode = languageCode,
                        fontFamily = fontFamily,
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