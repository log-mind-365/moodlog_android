package com.logmind.moodlog.presentation.settings.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.logmind.moodlog.domain.entities.ThemeMode

@Composable
fun ThemeSection(
    currentTheme: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    
    SettingsButton(
        title = "테마",
        subtitle = getThemeDisplayName(currentTheme),
        onClick = { showBottomSheet = true }
    )
    
    SelectionBottomSheet(
        isVisible = showBottomSheet,
        title = "테마 선택",
        options = ThemeMode.entries,
        selectedOption = currentTheme,
        onOptionSelected = onThemeChange,
        onDismiss = { showBottomSheet = false },
        optionDisplayName = ::getThemeDisplayName
    )
}

private fun getThemeDisplayName(theme: ThemeMode): String {
    return when (theme) {
        ThemeMode.LIGHT -> "라이트 테마"
        ThemeMode.DARK -> "다크 테마"
        ThemeMode.SYSTEM -> "시스템 설정 따르기"
    }
}