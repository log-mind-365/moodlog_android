package com.logmind.moodlog.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class StatisticCard(
    val title: String,
    val value: String,
    val emoji: String,
    val subtitle: String? = null,
    val color: Color
)

@Composable
fun StatisticsCards(
    averageMood: Float,
    totalEntries: Int,
    streakDays: Int,
    bestMoodDay: String?,
    modifier: Modifier = Modifier
) {
    var animationTrigger by remember { mutableStateOf(false) }
    
    LaunchedEffect(totalEntries) {
        animationTrigger = true
    }
    
    val cards = listOf(
        StatisticCard(
            title = "평균 감정",
            value = getMoodEmoji(averageMood),
            emoji = "📊",
            subtitle = getMoodText(averageMood),
            color = getMoodCardColor(averageMood)
        ),
        StatisticCard(
            title = "총 기록",
            value = totalEntries.toString(),
            emoji = "📝",
            subtitle = "${totalEntries}개의 일기",
            color = Color(0xFF2196F3)
        ),
        StatisticCard(
            title = "연속 기록",
            value = "${streakDays}일",
            emoji = "🔥",
            subtitle = if (streakDays > 0) "계속 기록 중!" else "새로 시작해보세요",
            color = if (streakDays > 0) Color(0xFFFF9800) else Color(0xFF757575)
        ),
        StatisticCard(
            title = "최고의 날",
            value = bestMoodDay ?: "없음",
            emoji = "⭐",
            subtitle = if (bestMoodDay != null) "가장 좋았던 날" else "기록해보세요",
            color = Color(0xFF4CAF50)
        )
    )
    
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // First row with first two cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AnimatedStatisticCard(
                card = cards[0],
                animationDelay = 0,
                isVisible = animationTrigger,
                modifier = Modifier.weight(1f)
            )
            AnimatedStatisticCard(
                card = cards[1],
                animationDelay = 100,
                isVisible = animationTrigger,
                modifier = Modifier.weight(1f)
            )
        }
        
        // Second row with remaining two cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AnimatedStatisticCard(
                card = cards[2],
                animationDelay = 200,
                isVisible = animationTrigger,
                modifier = Modifier.weight(1f)
            )
            AnimatedStatisticCard(
                card = cards[3],
                animationDelay = 300,
                isVisible = animationTrigger,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun AnimatedStatisticCard(
    card: StatisticCard,
    animationDelay: Int,
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    var startAnimation by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 400,
            delayMillis = animationDelay
        ),
        label = "card_scale"
    )
    
    LaunchedEffect(isVisible) {
        if (isVisible) {
            startAnimation = true
        }
    }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            card.color.copy(alpha = 0.1f),
                            card.color.copy(alpha = 0.05f)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = card.title,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Text(
                        text = card.emoji,
                        fontSize = 20.sp
                    )
                }
                
                Text(
                    text = card.value,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = card.color
                )
                
                card.subtitle?.let { subtitle ->
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

private fun getMoodEmoji(averageMood: Float): String {
    return when {
        averageMood >= 3.5f -> "😄"
        averageMood >= 2.5f -> "😊"
        averageMood >= 1.5f -> "😐"
        averageMood >= 0.5f -> "😔"
        else -> "😢"
    }
}

private fun getMoodText(averageMood: Float): String {
    return when {
        averageMood >= 3.5f -> "매우 좋음"
        averageMood >= 2.5f -> "좋음"
        averageMood >= 1.5f -> "보통"
        averageMood >= 0.5f -> "나쁨"
        else -> "매우 나쁨"
    }
}

private fun getMoodCardColor(averageMood: Float): Color {
    return when {
        averageMood >= 3.5f -> Color(0xFF4CAF50) // Green
        averageMood >= 2.5f -> Color(0xFF8BC34A) // Light Green
        averageMood >= 1.5f -> Color(0xFFFFEB3B) // Yellow
        averageMood >= 0.5f -> Color(0xFFFF9800) // Orange
        else -> Color(0xFFE91E63) // Pink
    }
}