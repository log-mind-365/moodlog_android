package com.logmind.moodlog.domain.repositories

import com.logmind.moodlog.domain.common.Result
import com.logmind.moodlog.domain.entities.CreateJournalDto
import com.logmind.moodlog.domain.entities.Journal
import com.logmind.moodlog.domain.entities.UpdateJournalDto
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface JournalRepository {
    val journalStream: Flow<List<Journal>>

    suspend fun getAllJournals(): Result<List<Journal>>

    suspend fun getJournalsByMonth(date: LocalDateTime): Result<List<Journal>>

    suspend fun getJournalsByDate(date: LocalDateTime): Result<List<Journal>>

    suspend fun getJournalById(id: Int): Result<Journal>

    suspend fun addJournal(dto: CreateJournalDto): Result<Map<String, Any>>

    suspend fun updateJournal(dto: UpdateJournalDto): Result<Int>

    suspend fun deleteJournalById(id: Int): Result<Unit>

    fun clearCache()
}