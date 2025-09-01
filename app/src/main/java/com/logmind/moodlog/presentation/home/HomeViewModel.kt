package com.logmind.moodlog.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.logmind.moodlog.domain.entities.Journal
import com.logmind.moodlog.domain.entities.MoodType
import com.logmind.moodlog.domain.repositories.JournalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val journalRepository: JournalRepository
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
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            journalRepository.getJournalsByDate(_selectedDate.value).onSuccess { journals ->
                _uiState.value = _uiState.value.copy(
                    selectedDateJournals = journals,
                    isLoading = false
                )
            }.onError { error ->
                _uiState.value = _uiState.value.copy(
                    selectedDateJournals = emptyList(),
                    isLoading = false,
                    error = error.message
                )
            }
        }
    }

    private fun loadMonthlyJournals() {
        viewModelScope.launch {
            journalRepository.getJournalsByMonth(_selectedDate.value).onSuccess { journals ->
                val monthlyMap = journals.groupBy { journal ->
                    LocalDateTime.of(
                        journal.createdAt.year,
                        journal.createdAt.month,
                        journal.createdAt.dayOfMonth,
                        0, 0
                    )
                }
                _uiState.value = _uiState.value.copy(monthlyJournals = monthlyMap)
            }
        }
    }

    fun deleteJournal(journalId: Int) {
        viewModelScope.launch {
            journalRepository.deleteJournalById(journalId).onSuccess {
                loadJournalsByDate()
                loadMonthlyJournals()
            }
        }
    }
}

data class HomeUiState(
    val selectedDateJournals: List<Journal> = emptyList(),
    val monthlyJournals: Map<LocalDateTime, List<Journal>> = emptyMap(),
    val representativeMood: MoodType? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)