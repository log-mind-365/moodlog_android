package com.logmind.moodlog.presentation.write.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.logmind.moodlog.R
import com.logmind.moodlog.utils.ImagePickerHelper
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun ImagePicker(
    images: List<Uri>,
    onImagesChanged: (List<Uri>) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var showImageOptions by remember { mutableStateOf(false) }
    var currentPhotoFile by remember { mutableStateOf<File?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { originalUri ->
            coroutineScope.launch {
                val resizedUri = ImagePickerHelper.resizeImage(context, originalUri)
                resizedUri?.let {
                    onImagesChanged(images + it)
                }
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            currentPhotoFile?.let { file ->
                val uri = ImagePickerHelper.getImageUri(context, file)
                coroutineScope.launch {
                    val resizedUri = ImagePickerHelper.resizeImage(context, uri)
                    resizedUri?.let {
                        onImagesChanged(images + it)
                    }
                }
            }
        }
    }

    Column(modifier = modifier) {
        if (images.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(images) { uri ->
                    ImageItem(
                        uri = uri,
                        onRemove = {
                            onImagesChanged(images - uri)
                        }
                    )
                }

                item {
                    AddImageButton(
                        onClick = { showImageOptions = true }
                    )
                }
            }
        } else {
            AddImageButton(
                onClick = { showImageOptions = true },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    if (showImageOptions) {
        ImageOptionsDialog(
            onDismiss = { showImageOptions = false },
            onGalleryClick = {
                showImageOptions = false
                galleryLauncher.launch("image/*")
            },
            onCameraClick = {
                showImageOptions = false
                currentPhotoFile = ImagePickerHelper.createImageFile(context)
                currentPhotoFile?.let { file ->
                    val uri = ImagePickerHelper.getImageUri(context, file)
                    cameraLauncher.launch(uri)
                }
            }
        )
    }
}

@Composable
private fun ImageItem(
    uri: Uri,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(80.dp)
    ) {
        AsyncImage(
            model = uri,
            contentDescription = "Selected image",
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        IconButton(
            onClick = onRemove,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(24.dp)
                .background(
                    MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
                    RoundedCornerShape(12.dp)
                )
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove image",
                tint = MaterialTheme.colorScheme.onError,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun AddImageButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(80.dp)
            .width(80.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add image",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = stringResource(R.string.image_picker_photo),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Composable
private fun ImageOptionsDialog(
    onDismiss: () -> Unit,
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("사진 추가")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onGalleryClick() }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📷",
                        modifier = Modifier.padding(end = 16.dp)
                    )
                    Text("갤러리에서 선택")
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCameraClick() }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📸",
                        modifier = Modifier.padding(end = 16.dp)
                    )
                    Text("카메라로 촬영")
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}