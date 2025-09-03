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
import com.logmind.moodlog.domain.entities.FontFamily


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FontSection(
    currentFont: FontFamily,
    onFontChange: (FontFamily) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    SectionCard(
        title = "폰트 설정",
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
