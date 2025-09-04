package com.logmind.moodlog.presentation.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.logmind.moodlog.presentation.settings.components.FontSection
import com.logmind.moodlog.presentation.settings.components.LanguageSection
import com.logmind.moodlog.presentation.settings.components.SectionCard
import com.logmind.moodlog.presentation.settings.components.ThemeSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier,
    topAppBar: @Composable () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column {
        topAppBar()
        LazyColumn(
            modifier = modifier
        ) {
            item {
                SectionCard(
                    title = "기본 설정"
                ) {
                    ThemeSection(
                        currentTheme = uiState.themeMode,
                        onThemeChange = viewModel::updateThemeMode
                    )

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.surfaceVariant
                    )

                    LanguageSection(
                        currentLanguage = uiState.languageCode,
                        onLanguageChange = viewModel::updateLanguage
                    )

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.surfaceVariant
                    )

                    FontSection(
                        currentFont = uiState.fontFamily,
                        onFontChange = viewModel::updateFontFamily
                    )
                }
            }
        }
    }
}
