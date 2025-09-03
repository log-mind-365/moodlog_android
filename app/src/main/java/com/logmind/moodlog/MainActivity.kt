package com.logmind.moodlog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.logmind.moodlog.presentation.navigation.MoodLogNavigation
import com.logmind.moodlog.ui.theme.MoodLogTheme
import com.logmind.moodlog.utils.toLocale
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle(MainAppState())

            LaunchedEffect(uiState.languageCode) {
                val langTag = uiState.languageCode.toLocale().toLanguageTag()
                val localeList = LocaleListCompat.forLanguageTags(langTag)
                AppCompatDelegate.setApplicationLocales(localeList)
            }

            MoodLogTheme(
                themeMode = uiState.themeMode,
                fontFamily = uiState.fontFamily
            ) {
                MoodLogNavigation()
            }
        }
    }
}