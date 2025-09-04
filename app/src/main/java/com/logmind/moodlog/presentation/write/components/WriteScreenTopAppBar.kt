package com.logmind.moodlog.presentation.write.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.logmind.moodlog.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteScreenTopAppBar(
    onNavigateBack: () -> Unit,
    canSave: Boolean,
    saveJournal: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        title = {},
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = stringResource(R.string.content_desc_close)
                )
            }
        },
        actions = {
            TextButton(
                onClick = saveJournal,
                enabled = canSave
            ) {
                Text(stringResource(R.string.save))
            }
        }
    )
}