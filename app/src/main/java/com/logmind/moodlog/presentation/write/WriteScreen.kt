package com.logmind.moodlog.presentation.write

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.logmind.moodlog.domain.entities.MoodType
import com.logmind.moodlog.domain.entities.Tag
import com.logmind.moodlog.presentation.components.ImagePicker
import com.logmind.moodlog.presentation.components.TagPicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteScreen(
    onNavigateBack: () -> Unit,
    onImagePick: () -> Unit = {},
    onCameraTake: () -> Unit = {},
    viewModel: WriteViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("일기 작성") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.Close, contentDescription = "닫기")
                    }
                },
                actions = {
                    TextButton(
                        onClick = { viewModel.saveJournal() },
                        enabled = uiState.canSave
                    ) {
                        Text("저장")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            MoodSlider(
                selectedMood = uiState.selectedMood,
                onMoodChange = viewModel::updateMood
            )

            ContentInput(
                content = uiState.content,
                onContentChange = viewModel::updateContent
            )

            ModernImageSection(
                images = uiState.imageUris,
                onImagesChanged = viewModel::updateImages
            )

            ModernTagSection(
                availableTags = uiState.availableTags,
                selectedTags = uiState.selectedTags,
                onTagToggle = viewModel::toggleTag,
                onNewTagCreate = viewModel::createNewTag
            )
        }
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    uiState.errorMessage?.let { message ->
        LaunchedEffect(message) {
            // Show snackbar or error dialog
        }
    }
    
    // Handle navigation after successful save
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onNavigateBack()
        }
    }
}

@Composable
private fun MoodSlider(
    selectedMood: MoodType,
    onMoodChange: (MoodType) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "오늘의 기분",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = selectedMood.emoji,
                    fontSize = 48.sp
                )

                Text(
                    text = when (selectedMood) {
                        MoodType.VERY_HAPPY -> "매우 좋음"
                        MoodType.HAPPY -> "좋음"
                        MoodType.NEUTRAL -> "보통"
                        MoodType.SAD -> "나쁨"
                        MoodType.VERY_SAD -> "매우 나쁨"
                    },
                    style = MaterialTheme.typography.labelLarge,
                    color = selectedMood.color
                )
            }

            Slider(
                value = selectedMood.sliderValue,
                onValueChange = { value ->
                    onMoodChange(MoodType.fromSlider(value))
                },
                valueRange = 0f..4f,
                steps = 3,
                colors = SliderDefaults.colors(
                    thumbColor = selectedMood.color,
                    activeTrackColor = selectedMood.color
                )
            )
        }
    }
}

@Composable
private fun ContentInput(
    content: String,
    onContentChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "오늘 하루는 어땠나요?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            OutlinedTextField(
                value = content,
                onValueChange = onContentChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                placeholder = { Text("자유롭게 작성해보세요...") },
                maxLines = 5
            )
        }
    }
}

@Composable
private fun ModernImageSection(
    images: List<Uri>,
    onImagesChanged: (List<Uri>) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "사진 추가",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            ImagePicker(
                images = images,
                onImagesChanged = onImagesChanged
            )
        }
    }
}

@Composable
private fun ModernTagSection(
    availableTags: List<Tag>,
    selectedTags: Set<Int>,
    onTagToggle: (Int) -> Unit,
    onNewTagCreate: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.padding(16.dp)
        ) {
            TagPicker(
                availableTags = availableTags,
                selectedTags = selectedTags,
                onTagToggle = onTagToggle,
                onNewTagCreate = onNewTagCreate
            )
        }
    }
}