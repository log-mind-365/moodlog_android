package com.logmind.moodlog.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.logmind.moodlog.R
import com.logmind.moodlog.ui.components.SurfaceCard

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
) {
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
            value = bestMoodDay ?: stringResource(R.string.none),
            emoji = "⭐",
            subtitle = if (bestMoodDay != null) stringResource(R.string.statistics_best_day) else stringResource(R.string.statistics_record_now),
            color = Color(0xFF4CAF50)
        )
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // First row with first two cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatisticCardItem(card = cards[0], modifier = Modifier.weight(1f))
            StatisticCardItem(card = cards[1], modifier = Modifier.weight(1f))
        }

        // Second row with remaining two cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatisticCardItem(card = cards[2], modifier = Modifier.weight(1f))
            StatisticCardItem(card = cards[3], modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun StatisticCardItem(
    modifier: Modifier = Modifier,
    card: StatisticCard,
) {
    SurfaceCard(
        modifier = modifier.semantics {
            contentDescription = "${card.title}: ${card.value}. ${card.subtitle ?: ""}"
        }
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(R.dimen.card_padding)),
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
