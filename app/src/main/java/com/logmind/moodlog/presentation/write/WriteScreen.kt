package com.logmind.moodlog.presentation.write

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
// import coil.compose.AsyncImage // TODO: Add Coil dependency
import com.logmind.moodlog.domain.entities.MoodType
import com.logmind.moodlog.domain.entities.Tag

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteScreen(
    onNavigateBack: () -> Unit,
    onImagePick: () -> Unit,
    onCameraTake: () -> Unit,
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
            
            ImageSection(
                imageUris = uiState.imageUris,
                onImagePick = onImagePick,
                onCameraTake = onCameraTake,
                onImageRemove = viewModel::removeImage
            )
            
            TagSection(
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
private fun ImageSection(
    imageUris: List<String>,
    onImagePick: () -> Unit,
    onCameraTake: () -> Unit,
    onImageRemove: (String) -> Unit
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
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onImagePick,
                            modifier = Modifier.size(80.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "갤러리")
                        }
                        Text(
                            text = "갤러리",
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.width(80.dp)
                        )
                    }
                }
                
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onCameraTake,
                            modifier = Modifier.size(80.dp)
                        ) {
                            Text("📷")
                        }
                        Text(
                            text = "카메라",
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.width(80.dp)
                        )
                    }
                }
                
                items(imageUris) { uri ->
                    Box(
                        modifier = Modifier.size(80.dp)
                    ) {
                        // TODO: Replace with AsyncImage when Coil is added
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Gray, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("📷", fontSize = 24.sp)
                        }
                        
                        IconButton(
                            onClick = { onImageRemove(uri) },
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "삭제",
                                modifier = Modifier
                                    .background(Color.Black.copy(0.5f), CircleShape)
                                    .padding(2.dp),
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TagSection(
    availableTags: List<Tag>,
    selectedTags: Set<Int>,
    onTagToggle: (Int) -> Unit,
    onNewTagCreate: (String) -> Unit
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "태그",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                
                TextButton(
                    onClick = { showCreateDialog = true }
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("추가")
                }
            }
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(availableTags) { tag ->
                    FilterChip(
                        selected = selectedTags.contains(tag.id),
                        onClick = { onTagToggle(tag.id) },
                        label = { Text(tag.name) }
                    )
                }
            }
        }
    }
    
    if (showCreateDialog) {
        CreateTagDialog(
            onDismiss = { showCreateDialog = false },
            onConfirm = { tagName ->
                onNewTagCreate(tagName)
                showCreateDialog = false
            }
        )
    }
}

@Composable
private fun CreateTagDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var tagName by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("새 태그 만들기") },
        text = {
            OutlinedTextField(
                value = tagName,
                onValueChange = { tagName = it },
                placeholder = { Text("태그 이름") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(tagName) },
                enabled = tagName.isNotBlank()
            ) {
                Text("생성")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}