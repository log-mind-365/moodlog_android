package com.logmind.moodlog.presentation.settings.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.logmind.moodlog.domain.entities.LanguageCode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSection(
    currentLanguage: LanguageCode,
    onLanguageChange: (LanguageCode) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    SectionCard(
        title = "언어 설정",
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