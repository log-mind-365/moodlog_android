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
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.logmind.moodlog.R
import com.logmind.moodlog.domain.entities.MoodType
import com.logmind.moodlog.domain.entities.Tag
import com.logmind.moodlog.presentation.components.ImagePicker
import com.logmind.moodlog.presentation.components.TagPicker
import com.logmind.moodlog.ui.components.SurfaceCard

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
                title = { Text(stringResource(R.string.write_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.Close, contentDescription = stringResource(R.string.content_desc_close))
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(dimensionResource(R.dimen.screen_padding))
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_xxl))
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
    SurfaceCard {
        Column(
            modifier = Modifier.padding(dimensionResource(R.dimen.card_padding)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_l))
        ) {
            Text(
                text = stringResource(R.string.write_mood_question),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val emojiScale by animateFloatAsState(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 300),
                    label = "emoji_scale"
                )
                
                Text(
                    text = selectedMood.emoji,
                    fontSize = 48.sp,
                    modifier = Modifier
                        .scale(emojiScale)
                        .semantics {
                            contentDescription = when (selectedMood) {
                                MoodType.VERY_HAPPY -> "매우 기쁜 감정"
                                MoodType.HAPPY -> "기쁜 감정" 
                                MoodType.NEUTRAL -> "보통 감정"
                                MoodType.SAD -> "슬픈 감정"
                                MoodType.VERY_SAD -> "매우 슬픈 감정"
                            }
                        }
                )

                Text(
                    text = when (selectedMood) {
                        MoodType.VERY_HAPPY -> stringResource(R.string.mood_very_happy)
                        MoodType.HAPPY -> stringResource(R.string.mood_happy)
                        MoodType.NEUTRAL -> stringResource(R.string.mood_neutral)
                        MoodType.SAD -> stringResource(R.string.mood_sad)
                        MoodType.VERY_SAD -> stringResource(R.string.mood_very_sad)
                    },
                    style = MaterialTheme.typography.labelLarge,
                    color = selectedMood.color
                )
            }

            val sliderContentDescription = stringResource(R.string.content_desc_slider)
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
                ),
                modifier = Modifier.semantics {
                    contentDescription = sliderContentDescription
                }
            )
        }
    }
}

@Composable
private fun ContentInput(
    content: String,
    onContentChange: (String) -> Unit
) {
    SurfaceCard {
        Column(
            modifier = Modifier.padding(dimensionResource(R.dimen.card_padding)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m))
        ) {
            Text(
                text = stringResource(R.string.write_content_question),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            OutlinedTextField(
                value = content,
                onValueChange = onContentChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                placeholder = { Text(stringResource(R.string.write_content_hint)) },
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
    SurfaceCard {
        Column(
            modifier = Modifier.padding(dimensionResource(R.dimen.card_padding)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_m))
        ) {
            Text(
                text = stringResource(R.string.write_add_image),
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
    SurfaceCard {
        Box(
            modifier = Modifier.padding(dimensionResource(R.dimen.card_padding))
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