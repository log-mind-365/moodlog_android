package com.logmind.moodlog.presentation.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SelectionBottomSheet(
    isVisible: Boolean,
    title: String,
    options: List<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
    onDismiss: () -> Unit,
    optionDisplayName: (T) -> String
) {
    if (isVisible) {
        val sheetState = rememberModalBottomSheetState()
        
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                
                options.forEachIndexed { index, option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onOptionSelected(option)
                                onDismiss()
                            }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedOption == option,
                            onClick = {
                                onOptionSelected(option)
                                onDismiss()
                            }
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = optionDisplayName(option),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    
                    if (index < options.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 56.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}