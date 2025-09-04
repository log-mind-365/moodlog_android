package com.logmind.moodlog.domain.usecases

import com.logmind.moodlog.domain.entities.CreateJournalDto
import com.logmind.moodlog.domain.entities.Journal
import com.logmind.moodlog.domain.entities.UpdateJournalDto
import com.logmind.moodlog.domain.repositories.JournalRepository
import java.time.LocalDateTime
import javax.inject.Inject

class JournalUseCase @Inject constructor(
    private val journalRepository: JournalRepository
) {
    suspend fun addJournal(dto: CreateJournalDto): Result<Map<String, Any>> {
        return runCatching { journalRepository.addJournal(dto) }
    }

    suspend fun getJournals(): Result<List<Journal>> {
        return runCatching { journalRepository.getAllJournals() }
    }

    suspend fun getJournalById(id: Int): Result<Journal> {
        return runCatching { journalRepository.getJournalById(id) }
    }

    suspend fun getJournalsByMonth(date: LocalDateTime): Result<List<Journal>> {
        return runCatching { journalRepository.getJournalsByMonth(date) }
    }

    suspend fun getJournalsByDate(date: LocalDateTime): Result<List<Journal>> {
        return runCatching { journalRepository.getJournalsByDate(date) }
    }

    suspend fun deleteJournalById(id: Int): Result<Unit> {
        return runCatching { journalRepository.deleteJournalById(id) }
    }

    suspend fun updateJournal(dto: UpdateJournalDto): Result<Int> {
        return runCatching { journalRepository.updateJournal(dto) }
    }

    suspend fun getJournalsByDateRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Result<List<Journal>> {
        return runCatching { journalRepository.getJournalsByDateRange(startDate, endDate) }
    }
}