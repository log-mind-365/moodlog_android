package com.logmind.moodlog.presentation.settings.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.logmind.moodlog.domain.entities.FontFamily

@Composable
fun FontSection(
    currentFont: FontFamily,
    onFontChange: (FontFamily) -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    SettingsButton(
        title = "폰트",
        subtitle = getFontDisplayName(currentFont),
        onClick = { showBottomSheet = true }
    )
    
    SelectionBottomSheet(
        isVisible = showBottomSheet,
        title = "폰트 선택",
        options = FontFamily.entries,
        selectedOption = currentFont,
        onOptionSelected = onFontChange,
        onDismiss = { showBottomSheet = false },
        optionDisplayName = ::getFontDisplayName
    )
}

private fun getFontDisplayName(fontFamily: FontFamily): String {
    return when (fontFamily) {
        FontFamily.PRETENDARD -> "프리텐다드"
        FontFamily.LEE_SEOYUN -> "이서윤체"
        FontFamily.ORBIT_OF_THE_MOON -> "달의궤도체"
        FontFamily.RESTART -> "리스타트체"
        FontFamily.OVERCOME -> "오버컴체"
        FontFamily.SYSTEM -> "시스템"
    }
}
