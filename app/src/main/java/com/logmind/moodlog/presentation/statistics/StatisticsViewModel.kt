package com.logmind.moodlog.presentation.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.logmind.moodlog.domain.common.Result
import com.logmind.moodlog.domain.entities.Journal
import com.logmind.moodlog.domain.entities.MoodType
import com.logmind.moodlog.domain.usecases.JournalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

data class StatisticsUiState(
    val isLoading: Boolean = false,
    val journals: List<Journal> = emptyList(),
    val selectedPeriod: TimePeriod = TimePeriod.MONTH,
    val moodTrends: List<MoodTrendPoint> = emptyList(),
    val moodDistribution: List<MoodDistribution> = emptyList(),
    val averageMood: Float = 0f,
    val totalEntries: Int = 0,
    val streakDays: Int = 0,
    val bestMoodDay: String? = null,
    val errorMessage: String? = null
)

data class MoodTrendPoint(
    val date: LocalDateTime,
    val averageMood: Float,
    val entryCount: Int
)

data class MoodDistribution(
    val moodType: MoodType,
    val count: Int,
    val percentage: Float
)

enum class TimePeriod(val displayName: String, val days: Int) {
    WEEK("지난 7일", 7),
    MONTH("지난 30일", 30),
    THREE_MONTHS("지난 3개월", 90),
    YEAR("지난 1년", 365)
}

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val journalUseCase: JournalUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadStatistics()
    }

    fun selectPeriod(period: TimePeriod) {
        _uiState.update { it.copy(selectedPeriod = period) }
        loadStatistics()
    }

    private fun loadStatistics() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }

                val period = _uiState.value.selectedPeriod
                val startDate = LocalDateTime.now().minusDays(period.days.toLong())
                val endDate = LocalDateTime.now()

                when (val result = journalUseCase.getJournalsByDateRange(startDate, endDate)) {
                    is Result.Success -> {
                        val journals = result.data
                        val moodTrends = calculateMoodTrends(journals, period)
                        val moodDistribution = calculateMoodDistribution(journals)
                        val averageMood = calculateAverageMood(journals)
                        val streakDays = calculateStreakDays(journals)
                        val bestMoodDay = findBestMoodDay(journals)

                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                journals = journals,
                                moodTrends = moodTrends,
                                moodDistribution = moodDistribution,
                                averageMood = averageMood,
                                totalEntries = journals.size,
                                streakDays = streakDays,
                                bestMoodDay = bestMoodDay,
                                errorMessage = null
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.exception.message
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "통계 계산 중 오류가 발생했습니다: ${e.message}"
                    )
                }
            }
        }
    }

    private fun calculateMoodTrends(
        journals: List<Journal>,
        period: TimePeriod
    ): List<MoodTrendPoint> {
        if (journals.isEmpty()) return emptyList()

        val groupBy = when (period) {
            TimePeriod.WEEK -> ChronoUnit.DAYS
            TimePeriod.MONTH -> ChronoUnit.DAYS
            TimePeriod.THREE_MONTHS -> ChronoUnit.WEEKS
            TimePeriod.YEAR -> ChronoUnit.MONTHS
        }

        return journals
            .groupBy { journal ->
                when (groupBy) {
                    ChronoUnit.DAYS -> journal.createdAt.toLocalDate()
                    ChronoUnit.WEEKS -> journal.createdAt.toLocalDate().with(
                        java.time.DayOfWeek.MONDAY
                    )

                    ChronoUnit.MONTHS -> journal.createdAt.withDayOfMonth(1).toLocalDate()
                    else -> journal.createdAt.toLocalDate()
                }
            }
            .map { (date, dayJournals) ->
                val moodValues = dayJournals.map { it.moodType.sliderValue }
                val averageMood = if (moodValues.isNotEmpty()) {
                    moodValues.average().toFloat()
                } else {
                    2f // Default to neutral
                }
                MoodTrendPoint(
                    date = date.atStartOfDay(),
                    averageMood = averageMood,
                    entryCount = dayJournals.size
                )
            }
            .sortedBy { it.date }
    }

    private fun calculateMoodDistribution(journals: List<Journal>): List<MoodDistribution> {
        val total = journals.size.toFloat()
        if (total == 0f) return emptyList()

        return journals
            .groupBy { it.moodType }
            .map { (moodType, moodJournals) ->
                val count = moodJournals.size
                MoodDistribution(
                    moodType = moodType,
                    count = count,
                    percentage = (count / total) * 100f
                )
            }
            .sortedByDescending { it.count }
    }

    private fun calculateAverageMood(journals: List<Journal>): Float {
        if (journals.isEmpty()) return 2f // Neutral
        return journals.map { it.moodType.sliderValue }.average().toFloat()
    }

    private fun calculateStreakDays(journals: List<Journal>): Int {
        if (journals.isEmpty()) return 0

        val journalDates = journals
            .map { it.createdAt.toLocalDate() }
            .distinct()
            .sortedDescending()

        var streak = 0
        var currentDate = LocalDateTime.now().toLocalDate()

        for (date in journalDates) {
            if (date == currentDate || date == currentDate.minusDays(1)) {
                streak++
                currentDate = date.minusDays(1)
            } else {
                break
            }
        }

        return streak
    }

    private fun findBestMoodDay(journals: List<Journal>): String? {
        if (journals.isEmpty()) return null

        val bestEntry = journals
            .maxByOrNull { it.moodType.sliderValue }

        return bestEntry?.let {
            "${it.createdAt.monthValue}월 ${it.createdAt.dayOfMonth}일"
        }
    }

    fun refreshStatistics() {
        loadStatistics()
    }
}