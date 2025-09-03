package com.logmind.moodlog.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.logmind.moodlog.R
import com.logmind.moodlog.presentation.settings.components.FontSection
import com.logmind.moodlog.presentation.settings.components.LanguageSection
import com.logmind.moodlog.presentation.settings.components.ThemeSection
import com.logmind.moodlog.ui.components.MdlAppBar
import com.logmind.moodlog.ui.components.MdlAvatar
import com.logmind.moodlog.ui.components.MdlScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavHostController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()


    MdlScaffold(
        navController = navController,
        topBar = {
            MdlAppBar(
                title = stringResource(R.string.nav_settings),
                actions = {
                    MdlAvatar()
                }
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(dimensionResource(R.dimen.screen_padding)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_l))
        ) {
            item {
                com.logmind.moodlog.presentation.settings.components.SectionCard(
                    title = "기본 설정"
                ) {
                    ThemeSection(
                        currentTheme = uiState.themeMode,
                        onThemeChange = viewModel::updateThemeMode
                    )

                    androidx.compose.material3.HorizontalDivider(
                        modifier = androidx.compose.ui.Modifier.padding(vertical = 8.dp),
                        color = androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant
                    )

                    LanguageSection(
                        currentLanguage = uiState.languageCode,
                        onLanguageChange = viewModel::updateLanguage
                    )

                    androidx.compose.material3.HorizontalDivider(
                        modifier = androidx.compose.ui.Modifier.padding(vertical = 8.dp),
                        color = androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant
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
