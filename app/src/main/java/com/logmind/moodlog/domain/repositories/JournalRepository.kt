package com.logmind.moodlog.domain.repositories

import com.logmind.moodlog.domain.entities.CreateJournalDto
import com.logmind.moodlog.domain.entities.Journal
import com.logmind.moodlog.domain.entities.UpdateJournalDto
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface JournalRepository {
    val journalStream: Flow<List<Journal>>

    suspend fun getAllJournals(): List<Journal>

    suspend fun getJournalsByMonth(date: LocalDateTime): List<Journal>

    suspend fun getJournalsByDate(date: LocalDateTime): List<Journal>

    suspend fun getJournalsByDateRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<Journal>

    suspend fun getJournalById(id: Int): Journal

    suspend fun addJournal(dto: CreateJournalDto): Map<String, Any>

    suspend fun updateJournal(dto: UpdateJournalDto): Int

    suspend fun deleteJournalById(id: Int)

    fun clearCache()
}