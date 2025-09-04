package com.logmind.moodlog.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.logmind.moodlog.domain.entities.Journal
import com.logmind.moodlog.domain.entities.MoodType
import com.logmind.moodlog.domain.usecases.JournalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class HomeUiState(
    val selectedDateJournals: List<Journal> = emptyList(),
    val monthlyJournals: Map<LocalDateTime, List<Journal>> = emptyMap(),
    val representativeMood: MoodType? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val journalUseCase: JournalUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _selectedDate = MutableStateFlow(LocalDateTime.now())
    val selectedDate: StateFlow<LocalDateTime> = _selectedDate.asStateFlow()

    init {
        loadJournalsByDate()
        loadMonthlyJournals()
    }

    fun selectDate(date: LocalDateTime) {
        _selectedDate.value = date
        loadJournalsByDate()
    }

    private fun loadJournalsByDate() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            journalUseCase.getJournalsByDate(_selectedDate.value)
                .onSuccess { journals ->
                    _uiState.update {
                        it.copy(
                            selectedDateJournals = journals,
                            isLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            selectedDateJournals = emptyList(),
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
        }
    }

    private fun loadMonthlyJournals() {
        viewModelScope.launch {
            journalUseCase.getJournalsByMonth(_selectedDate.value)
                .onSuccess { journals ->
                    val monthlyMap = journals.groupBy { journal ->
                        LocalDateTime.of(
                            journal.createdAt.year,
                            journal.createdAt.month,
                            journal.createdAt.dayOfMonth,
                            0, 0
                        )
                    }
                    _uiState.update { it.copy(monthlyJournals = monthlyMap) }
                }

        }
    }

    fun deleteJournal(journalId: Int) {
        viewModelScope.launch {
            journalUseCase.deleteJournalById(journalId)
                .onSuccess {
                    loadJournalsByDate()
                    loadMonthlyJournals()
                }
        }
    }
}
