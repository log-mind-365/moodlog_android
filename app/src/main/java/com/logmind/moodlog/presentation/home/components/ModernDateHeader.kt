package com.logmind.moodlog.presentation.home.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import com.logmind.moodlog.R
import com.logmind.moodlog.domain.entities.Journal
import com.logmind.moodlog.ui.components.MdlCard
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Composable
fun ModernDateHeader(
    selectedDate: LocalDateTime,
    monthlyJournals: Map<LocalDateTime, List<Journal>>,
    onDateSelected: (LocalDateTime) -> Unit
) {

    MdlCard(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onSecondary,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.card_padding))
        ) {
            Text(
                text = selectedDate.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = selectedDate.format(DateTimeFormatter.ofPattern("EEEE, d일")),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.spacing_lg)))
            Row(Modifier.horizontalScroll(rememberScrollState())) {
                mockList.forEach {
                    Column(
                        modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.spacing_md)),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            it.day, style = MaterialTheme.typography.titleMedium.copy(
                                color = MaterialTheme.colorScheme.surface,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            it.date.toString(),
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = MaterialTheme.colorScheme.surface,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}


private data class DayItem(
    val day: String,
    val date: Int
)

private val mockList = listOf(
    DayItem("Mon", 1),
    DayItem("Tue", 2),
    DayItem("Wed", 3),
    DayItem("Thu", 4),
    DayItem("Fri", 5),
    DayItem("Sat", 6),
    DayItem("Sun", 7),
    DayItem("Mon", 8),
    DayItem("Tue", 9),
    DayItem("Wed", 10),
    DayItem("Thu", 11),
    DayItem("Fri", 12),
    DayItem("Sat", 13),
    DayItem("Sun", 14),
    DayItem("Mon", 15),
    DayItem("Tue", 16),
    DayItem("Wed", 17),
    DayItem("Thu", 18),
    DayItem("Fri", 19),
    DayItem("Sat", 20),
    DayItem("Sun", 21),
    DayItem("Mon", 22),
    DayItem("Tue", 23),
    DayItem("Wed", 24),
    DayItem("Thu", 25),
    DayItem("Fri", 26),
    DayItem("Sat", 27),
    DayItem("Sun", 28),
    DayItem("Mon", 29),
    DayItem("Tue", 30),
)
