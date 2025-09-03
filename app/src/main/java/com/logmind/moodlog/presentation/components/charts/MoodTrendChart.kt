package com.logmind.moodlog.presentation.components.charts

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.logmind.moodlog.presentation.statistics.MoodTrendPoint
import com.logmind.moodlog.ui.components.MdlCard

@Composable
fun MoodTrendChart(
    data: List<MoodTrendPoint>,
) {
    var animationProgress by remember { mutableStateOf(0f) }

    val animatedProgress by animateFloatAsState(
        targetValue = animationProgress,
        animationSpec = tween(durationMillis = 1000),
        label = "chart_animation"
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
                text = "감정 추이",
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    Canvas(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        drawMoodTrendChart(
                            data = data,
                            progress = animatedProgress,
                            canvasWidth = size.width,
                            canvasHeight = size.height
                        )
                    }

                    // Y-axis labels
                    MoodLabels(modifier = Modifier.align(Alignment.CenterStart))
                }
            }
        }
    }
}

@Composable
private fun MoodLabels(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        listOf("😄", "😊", "😐", "😔", "😢").forEach { emoji ->
            Text(
                text = emoji,
                fontSize = 12.sp,
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                        RoundedCornerShape(4.dp)
                    )
                    .padding(2.dp)
            )
        }
    }
}

private fun DrawScope.drawMoodTrendChart(
    data: List<MoodTrendPoint>,
    progress: Float,
    canvasWidth: Float,
    canvasHeight: Float
) {
    if (data.isEmpty() || canvasWidth <= 0 || canvasHeight <= 0) return

    val padding = 40f
    val chartWidth = (canvasWidth - padding * 2).coerceAtLeast(0f)
    val chartHeight = (canvasHeight - padding * 2).coerceAtLeast(0f)

    if (chartWidth <= 0 || chartHeight <= 0) return

    // Grid lines
    drawGrid(padding, chartWidth, chartHeight)

    // Data points and line
    val points = data.mapIndexed { index, point ->
        val x = padding + (index.toFloat() / (data.size - 1).coerceAtLeast(1)) * chartWidth
        val y = padding + chartHeight - (point.averageMood / 4f) * chartHeight
        Offset(x, y)
    }

    // Draw animated line
    if (points.size > 1) {
        drawAnimatedLine(points, progress)
    }

    // Draw animated points
    points.forEachIndexed { index, point ->
        val pointProgress = (progress * points.size - index).coerceIn(0f, 1f)
        if (pointProgress > 0f) {
            drawAnimatedPoint(point, pointProgress, data[index])
        }
    }
}

private fun DrawScope.drawGrid(
    padding: Float,
    chartWidth: Float,
    chartHeight: Float
) {
    val gridColor = Color.Gray.copy(alpha = 0.2f)
    val stroke = Stroke(width = 1.dp.toPx())

    // Horizontal grid lines (mood levels)
    for (i in 0..4) {
        val y = padding + (i.toFloat() / 4f) * chartHeight
        drawLine(
            color = gridColor,
            start = Offset(padding, y),
            end = Offset(padding + chartWidth, y),
            strokeWidth = stroke.width
        )
    }
}

private fun DrawScope.drawAnimatedLine(
    points: List<Offset>,
    progress: Float
) {
    val path = Path()

    if (points.isNotEmpty()) {
        path.moveTo(points[0].x, points[0].y)

        for (i in 1 until points.size) {
            val segmentProgress = ((progress * (points.size - 1)) - (i - 1)).coerceIn(0f, 1f)
            if (segmentProgress > 0f) {
                val startPoint = points[i - 1]
                val endPoint = points[i]
                val currentX = startPoint.x + (endPoint.x - startPoint.x) * segmentProgress
                val currentY = startPoint.y + (endPoint.y - startPoint.y) * segmentProgress
                path.lineTo(currentX, currentY)
            }
        }
    }

    drawPath(
        path = path,
        color = Color(0xFF2196F3),
        style = Stroke(
            width = 3.dp.toPx(),
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )
}

private fun DrawScope.drawAnimatedPoint(
    point: Offset,
    progress: Float,
    data: MoodTrendPoint
) {
    val radius = 6.dp.toPx() * progress
    val moodColor = getMoodColor(data.averageMood)

    // Outer circle
    drawCircle(
        color = moodColor.copy(alpha = 0.3f),
        radius = radius * 1.5f,
        center = point
    )

    // Inner circle
    drawCircle(
        color = moodColor,
        radius = radius,
        center = point
    )
}

private fun getMoodColor(averageMood: Float): Color {
    return when {
        averageMood >= 3.5f -> Color(0xFF4CAF50) // Very Happy - Green
        averageMood >= 2.5f -> Color(0xFF8BC34A) // Happy - Light Green  
        averageMood >= 1.5f -> Color(0xFFFFEB3B) // Neutral - Yellow
        averageMood >= 0.5f -> Color(0xFFFF9800) // Sad - Orange
        else -> Color(0xFFE91E63) // Very Sad - Pink
    }
}