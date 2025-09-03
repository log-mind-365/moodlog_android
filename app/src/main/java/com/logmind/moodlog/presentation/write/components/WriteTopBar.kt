package com.logmind.moodlog.presentation.write.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.logmind.moodlog.R
import com.logmind.moodlog.presentation.write.WriteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteTopBar(
    onNavigateBack: () -> Unit,
    viewModel: WriteViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TopAppBar(
        title = { Text(stringResource(R.string.write_title)) },
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
                onClick = { viewModel.saveJournal() },
                enabled = uiState.canSave
            ) {
                Text(stringResource(R.string.save))
            }
        }
    )
}