package com.logmind.moodlog.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage
import com.logmind.moodlog.R
import com.logmind.moodlog.ui.theme.MoodLogTheme

@Composable
fun MdlAvatar(
    modifier: Modifier = Modifier,
    photoUrl: String? = null,
    size: Dp = dimensionResource(R.dimen.icon_size_lg),
    onClick: (() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary, CircleShape)
            .then(
                if (onClick != null) {
                    Modifier.clickable { onClick() }
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (!photoUrl.isNullOrEmpty()) {
            AsyncImage(
                model = photoUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
//                onLoading = { loading ->
//                    CircularProgressIndicator(
//                        modifier = Modifier.size(size * 0.6f),
//                        strokeWidth = 2.dp
//                    )
//                },
//                onEerror = {
//                    Icon(
//                        imageVector = Icons.Default.Person,
//                        contentDescription = null,
//                        modifier = Modifier.size(size * 0.6f)
//                    )
//                }
            )
        } else {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(size * 0.8f),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MdlAvatarPreview() {
    MoodLogTheme {
        MdlAvatar(onClick = {})
    }

}