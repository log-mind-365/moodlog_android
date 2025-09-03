package com.logmind.moodlog.presentation.statistics.components.charts

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.logmind.moodlog.R
import com.logmind.moodlog.presentation.statistics.MoodDistribution
import com.logmind.moodlog.ui.components.MdlCard

@Composable
fun MoodDistributionChart(
    data: List<MoodDistribution>,
) {
    var animationProgress by remember { mutableStateOf(0f) }

    val animatedProgress by animateFloatAsState(
        targetValue = animationProgress,
        animationSpec = tween(durationMillis = 1200),
        label = "donut_animation"
    )

    LaunchedEffect(data) {
        animationProgress = if (data.isNotEmpty()) 1f else 0f
    }

    MdlCard {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "감정 분포",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            if (data.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "표시할 데이터가 없습니다",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Donut Chart
                    Box(
                        modifier = Modifier
                            .size(160.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            drawDonutChart(
                                data = data,
                                progress = animatedProgress,
                                size = size
                            )
                        }

                        // Center text showing total
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${data.sumOf { it.count }}",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "총 기록",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Legend
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        data.forEach { distribution ->
                            MoodLegendItem(
                                distribution = distribution,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MoodLegendItem(
    distribution: MoodDistribution,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(getMoodColor(distribution.moodType.sliderValue))
        )

        Text(
            text = distribution.moodType.emoji,
            fontSize = 16.sp
        )

        Text(
            text = when (distribution.moodType.sliderValue.toInt()) {
                4 -> stringResource(R.string.mood_very_happy)
                3 -> stringResource(R.string.mood_happy)
                2 -> stringResource(R.string.mood_neutral)
                1 -> stringResource(R.string.mood_sad)
                0 -> stringResource(R.string.mood_very_sad)
                else -> stringResource(R.string.mood_neutral)
            },
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "${distribution.count}회",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = "${String.format("%.1f", distribution.percentage)}%",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun DrawScope.drawDonutChart(
    data: List<MoodDistribution>,
    progress: Float,
    size: Size
) {
    if (data.isEmpty() || size.width <= 0 || size.height <= 0) return

    val center = Offset(size.width / 2, size.height / 2)
    val radius = (size.minDimension / 2) * 0.8f
    val strokeWidth = (radius * 0.3f).coerceAtLeast(1f)
    val total = data.sumOf { it.count }.toFloat()

    if (total == 0f || radius <= 0) return

    var startAngle = -90f // Start from top

    data.forEach { distribution ->
        val sweepAngle = (distribution.count / total) * 360f * progress
        val color = getMoodColor(distribution.moodType.sliderValue)

        drawArc(
            color = color,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        startAngle += sweepAngle
    }
}

private fun getMoodColor(sliderValue: Float): Color {
    return when (sliderValue.toInt()) {
        4 -> Color(0xFF4CAF50) // Very Happy - Green
        3 -> Color(0xFF8BC34A) // Happy - Light Green  
        2 -> Color(0xFFFFEB3B) // Neutral - Yellow
        1 -> Color(0xFFFF9800) // Sad - Orange
        0 -> Color(0xFFE91E63) // Very Sad - Pink
        else -> Color(0xFFFFEB3B) // Default to Yellow
    }
}