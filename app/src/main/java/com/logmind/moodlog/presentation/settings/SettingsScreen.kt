package com.logmind.moodlog.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.logmind.moodlog.R
import com.logmind.moodlog.domain.entities.ColorTheme
import com.logmind.moodlog.domain.entities.FontFamily
import com.logmind.moodlog.domain.entities.LanguageCode
import com.logmind.moodlog.domain.entities.ThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.screen_padding)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_l))
    ) {
        item {
            ThemeSection(
                currentTheme = uiState.themeMode,
                onThemeChange = viewModel::updateThemeMode
            )
        }

        item {
            LanguageSection(
                currentLanguage = uiState.languageCode,
                onLanguageChange = viewModel::updateLanguage
            )
        }

        item {
            FontSection(
                currentFont = uiState.fontFamily,
                onFontChange = viewModel::updateFontFamily
            )
        }
    }
}

@Composable
private fun ThemeSection(
    currentTheme: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit
) {
    SettingsCard(
        title = "테마 설정",
        icon = Icons.Default.Settings
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ThemeMode.entries.forEach { theme ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = currentTheme == theme,
                            onClick = { onThemeChange(theme) }
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = currentTheme == theme,
                        onClick = { onThemeChange(theme) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when (theme) {
                            ThemeMode.LIGHT -> "라이트 테마"
                            ThemeMode.DARK -> "다크 테마"
                            ThemeMode.SYSTEM -> "시스템 설정 따르기"
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LanguageSection(
    currentLanguage: LanguageCode,
    onLanguageChange: (LanguageCode) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    SettingsCard(
        title = "언어 설정",
        icon = Icons.Default.Settings
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = getLanguageDisplayName(currentLanguage),
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                LanguageCode.entries.forEach { language ->
                    DropdownMenuItem(
                        text = { Text(getLanguageDisplayName(language)) },
                        onClick = {
                            onLanguageChange(language)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FontSection(
    currentFont: FontFamily,
    onFontChange: (FontFamily) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    SettingsCard(
        title = "폰트 설정",
        icon = Icons.Default.Settings
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = getFontDisplayName(currentFont),
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                FontFamily.entries.forEach { fontFamily ->
                    DropdownMenuItem(
                        text = { Text(getFontDisplayName(fontFamily)) },
                        onClick = {
                            onFontChange(fontFamily)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsCard(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = stringResource(R.string.content_desc_color_theme),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            content()
        }
    }
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

private fun getColorThemePreviewColor(colorTheme: ColorTheme): Color {
    return when (colorTheme) {
        ColorTheme.RED -> Color(0xFFD32F2F)
        ColorTheme.PINK -> Color(0xFFE91E63)
        ColorTheme.PURPLE -> Color(0xFF7B1FA2)
        ColorTheme.DEEP_PURPLE -> Color(0xFF512DA8)
        ColorTheme.INDIGO -> Color(0xFF303F9F)
        ColorTheme.BLUE -> Color(0xFF1976D2)
        ColorTheme.LIGHT_BLUE -> Color(0xFF0288D1)
        ColorTheme.CYAN -> Color(0xFF0097A7)
        ColorTheme.TEAL -> Color(0xFF00796B)
        ColorTheme.GREEN -> Color(0xFF388E3C)
        ColorTheme.LIGHT_GREEN -> Color(0xFF689F38)
        ColorTheme.LIME -> Color(0xFF689F38)
        ColorTheme.YELLOW -> Color(0xFFFBC02D)
        ColorTheme.AMBER -> Color(0xFFFFA000)
        ColorTheme.ORANGE -> Color(0xFFFF9800)
        ColorTheme.DEEP_ORANGE -> Color(0xFFFF5722)
        ColorTheme.BROWN -> Color(0xFF5D4037)
        ColorTheme.GREY -> Color(0xFF757575)
        ColorTheme.BLUE_GREY -> Color(0xFF455A64)
    }
}