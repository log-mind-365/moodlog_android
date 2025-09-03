package com.logmind.moodlog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.logmind.moodlog.domain.entities.FontFamily
import com.logmind.moodlog.domain.entities.LanguageCode
import com.logmind.moodlog.domain.entities.ThemeMode
import com.logmind.moodlog.domain.repositories.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class MainAppState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val languageCode: LanguageCode = LanguageCode.EN,
    val fontFamily: FontFamily = FontFamily.PRETENDARD
)

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    settingsRepository: SettingsRepository
) : ViewModel() {

    val uiState: Flow<MainAppState> = combine(
        settingsRepository.getThemeMode(),
        settingsRepository.getLanguageCode(),
        settingsRepository.getFontFamily()
    ) { themeMode, languageCode, fontFamily ->
        MainAppState(
            themeMode = themeMode,
            languageCode = languageCode,
            fontFamily = fontFamily
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = MainAppState(),
        started = SharingStarted.WhileSubscribed(5_000),
    )
}