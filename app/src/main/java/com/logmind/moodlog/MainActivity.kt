package com.logmind.moodlog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.logmind.moodlog.presentation.navigation.MoodLogNavigation
import com.logmind.moodlog.ui.theme.MoodLogTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoodLogTheme {
                MoodLogNavigation()
            }
        }
    }
}