package com.logmind.moodlog.presentation.home

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.logmind.moodlog.R
import com.logmind.moodlog.domain.entities.Journal
import com.logmind.moodlog.ui.components.SurfaceCard
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.horizontal_padding))
    ) {
        // Header
        Text(
            text = "MoodLog",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Modern Date Header
        ModernDateHeader(selectedDate = selectedDate)

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_l)))

        // Modern Monthly Calendar
        ModernMonthlyCalendar(
            selectedDate = selectedDate,
            monthlyJournals = uiState.monthlyJournals,
            onDateSelected = viewModel::selectDate
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_l)))

        // Today's Journals
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "오늘의 기록",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )

        }

        Spacer(modifier = Modifier.height(12.dp))

        // Journal List
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(uiState.selectedDateJournals) { journal ->
                    JournalCard(
                        journal = journal,
                        onDelete = { viewModel.deleteJournal(journal.id) }
                    )
                }

                if (uiState.selectedDateJournals.isEmpty()) {
                    item {
                        SurfaceCard {
                            Text(
                                text = "아직 기록이 없어요\n첫 번째 무드를 기록해보세요!",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ModernDateHeader(selectedDate: LocalDateTime) {
    SurfaceCard(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()

                .padding(20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
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
            }
        }
    }
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

    SurfaceCard {
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

@Composable
fun JournalCard(
    journal: Journal,
    onDelete: () -> Unit
) {
    SurfaceCard {
        Column(
            modifier = Modifier.padding(dimensionResource(R.dimen.screen_padding))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = journal.moodType.emoji,
                        fontSize = 24.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    Text(
                        text = journal.createdAt.format(DateTimeFormatter.ofPattern("HH:mm")),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            if (!journal.content.isNullOrBlank()) {
                Text(
                    text = journal.content,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}