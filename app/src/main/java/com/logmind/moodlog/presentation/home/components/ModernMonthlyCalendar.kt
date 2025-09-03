package com.logmind.moodlog.presentation.home.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.logmind.moodlog.R
import com.logmind.moodlog.domain.entities.Journal
import com.logmind.moodlog.ui.components.MdlCard
import java.time.LocalDateTime
import java.time.YearMonth

sealed class CalendarDay {
    object Empty : CalendarDay()
    data class Day(
        val date: LocalDateTime,
        val day: Int,
        val isSelected: Boolean,
        val hasJournals: Boolean,
        val isToday: Boolean
    ) : CalendarDay()
}

@Composable
fun ModernMonthlyCalendar(
    selectedDate: LocalDateTime,
    monthlyJournals: Map<LocalDateTime, List<Journal>>,
    onDateSelected: (LocalDateTime) -> Unit
) {
    val yearMonth = YearMonth.of(selectedDate.year, selectedDate.month)
    val firstDayOfMonth = yearMonth.atDay(1)
    val lastDayOfMonth = yearMonth.atEndOfMonth()
    val daysInMonth = yearMonth.lengthOfMonth()

    // Create calendar grid data
    val calendarDays = mutableListOf<CalendarDay>()

    // Add empty cells for days before the first day of month
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
    repeat(firstDayOfWeek) {
        calendarDays.add(CalendarDay.Empty)
    }

    // Add actual days
    (1..daysInMonth).forEach { day ->
        val date = selectedDate.withDayOfMonth(day)
        val normalizedDate = date.withHour(0).withMinute(0).withSecond(0).withNano(0)
        val hasJournals = monthlyJournals.containsKey(normalizedDate)
        val isSelected = selectedDate.dayOfMonth == day
        val isToday = date.toLocalDate() == LocalDateTime.now().toLocalDate()

        calendarDays.add(
            CalendarDay.Day(
                date = date,
                day = day,
                isSelected = isSelected,
                hasJournals = hasJournals,
                isToday = isToday
            )
        )
    }

    MdlCard {
        Column(
            modifier = Modifier.padding(dimensionResource(R.dimen.card_padding))
        ) {
            // Week headers
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf(
                    stringResource(R.string.day_sunday),
                    stringResource(R.string.day_monday),
                    stringResource(R.string.day_tuesday),
                    stringResource(R.string.day_wednesday),
                    stringResource(R.string.day_thursday),
                    stringResource(R.string.day_friday),
                    stringResource(R.string.day_saturday)
                ).forEach { dayName ->
                    Text(
                        text = dayName,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_l)))

            // Calendar grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(calendarDays) { calendarDay ->
                    when (calendarDay) {
                        is CalendarDay.Empty -> {
                            Spacer(modifier = Modifier.aspectRatio(1f))
                        }

                        is CalendarDay.Day -> {
                            ModernCalendarDayItem(
                                calendarDay = calendarDay,
                                onClick = { onDateSelected(calendarDay.date) }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ModernCalendarDayItem(
    calendarDay: CalendarDay.Day,
    onClick: () -> Unit
) {

    val backgroundColor by animateColorAsState(
        targetValue = when {
            calendarDay.isSelected -> MaterialTheme.colorScheme.primary
            calendarDay.isToday -> MaterialTheme.colorScheme.secondaryContainer
            else -> Color.Transparent
        },
        animationSpec = tween(300),
        label = "background_color"
    )

    val borderColor = when {
        calendarDay.isToday && !calendarDay.isSelected -> MaterialTheme.colorScheme.primary
        else -> Color.Transparent
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(
                width = if (borderColor != Color.Transparent) 2.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .semantics {
                contentDescription = if (calendarDay.isSelected) {
                    "선택된 날짜: ${calendarDay.day}일"
                } else if (calendarDay.isToday) {
                    "오늘: ${calendarDay.day}일"
                } else {
                    "${calendarDay.day}일"
                }
                role = Role.Button
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = calendarDay.day.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = when {
                    calendarDay.isSelected -> MaterialTheme.colorScheme.onPrimary
                    calendarDay.isToday -> MaterialTheme.colorScheme.onSecondaryContainer
                    else -> MaterialTheme.colorScheme.onSurface
                },
                fontWeight = when {
                    calendarDay.isSelected || calendarDay.isToday -> FontWeight.Bold
                    else -> FontWeight.Normal
                }
            )

            if (calendarDay.hasJournals) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                calendarDay.isSelected -> MaterialTheme.colorScheme.onPrimary
                                calendarDay.isToday -> MaterialTheme.colorScheme.primary
                                else -> MaterialTheme.colorScheme.primary
                            }
                        )
                )
            }
        }
    }
}
