package com.logmind.moodlog.presentation.settings.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.logmind.moodlog.domain.entities.LanguageCode

@Composable
fun LanguageSection(
    currentLanguage: LanguageCode,
    onLanguageChange: (LanguageCode) -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    SettingsButton(
        title = "언어",
        subtitle = getLanguageDisplayName(currentLanguage),
        onClick = { showBottomSheet = true }
    )

    SelectionBottomSheet(
        isVisible = showBottomSheet,
        title = "언어 선택",
        options = LanguageCode.entries,
        selectedOption = currentLanguage,
        onOptionSelected = onLanguageChange,
        onDismiss = { showBottomSheet = false },
        optionDisplayName = ::getLanguageDisplayName
    )
}

private fun getLanguageDisplayName(languageCode: LanguageCode): String {
    return when (languageCode) {
        LanguageCode.KO -> "한국어"
        LanguageCode.EN -> "English"
        LanguageCode.JA -> "日本語"
        LanguageCode.ZH -> "中文"
        LanguageCode.ES -> "Español"
        LanguageCode.IT -> "Italiano"
        LanguageCode.FR -> "Français"
        LanguageCode.VI -> "Tiếng Việt"
        LanguageCode.TH -> "ไทย"
    }
}