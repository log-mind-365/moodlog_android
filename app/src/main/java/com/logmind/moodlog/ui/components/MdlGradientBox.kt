package com.logmind.moodlog.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.logmind.moodlog.ui.theme.MoodLogTheme

@Composable
fun MdlGradientBox(
    modifier: Modifier = Modifier,
    firstColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.85f),
    secondColor: Color = MaterialTheme.colorScheme.primary,
    content: @Composable () -> Unit
) {
    MdlCard {
        Box(
            modifier
                .background(
                    brush = Brush.linearGradient(
                        colorStops = arrayOf(
                            0.2f to firstColor,
                            0.5f to secondColor,
                        )
                    )
                ),
        ) {
            content()
        }
    }
}

@Preview
@Composable
fun MdlGradientBoxPreview() {
    MoodLogTheme {
        MdlGradientBox {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )
        }
    }
}
