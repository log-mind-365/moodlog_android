package com.logmind.moodlog.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun MdlCard(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.large,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    elevation: CardElevation = CardDefaults.cardElevation(),
    border: BorderStroke? = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceContainerHigh),
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth(),
        shape = shape,
        border = border,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = elevation,
    ) {
        content()
    }
}

@Composable
fun MdlCard(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.large,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    elevation: CardElevation = CardDefaults.cardElevation(),
    border: BorderStroke? = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceContainerHigh),
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = shape,
        border = border,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = elevation,
    ) {
        content()
    }
}