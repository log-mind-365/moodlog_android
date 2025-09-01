package com.logmind.moodlog.domain.usecases

import com.logmind.moodlog.domain.common.Result
import com.logmind.moodlog.domain.entities.CreateJournalDto
import com.logmind.moodlog.domain.entities.UpdateJournalDto
import com.logmind.moodlog.domain.repositories.JournalRepository
import javax.inject.Inject

class JournalUseCase @Inject constructor(
    private val journalRepository: JournalRepository
) {
    suspend fun addJournal(dto: CreateJournalDto): Result<Map<String, Any>> {
        return journalRepository.addJournal(dto)
    }

    suspend fun deleteJournal(id: Int): Result<Unit> {
        return journalRepository.deleteJournalById(id)
    }

    suspend fun updateJournal(dto: UpdateJournalDto): Result<Int> {
        return journalRepository.updateJournal(dto)
    }
}