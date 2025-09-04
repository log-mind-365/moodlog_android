package com.logmind.moodlog.data.repositories

import com.google.gson.Gson
import com.logmind.moodlog.data.database.dao.JournalDao
import com.logmind.moodlog.data.database.dao.TagDao
import com.logmind.moodlog.data.mappers.toDomainModel
import com.logmind.moodlog.data.mappers.toEntity
import com.logmind.moodlog.domain.entities.CreateJournalDto
import com.logmind.moodlog.domain.entities.Journal
import com.logmind.moodlog.domain.entities.UpdateJournalDto
import com.logmind.moodlog.domain.repositories.JournalRepository
import com.logmind.moodlog.utils.execute
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JournalRepositoryImpl @Inject constructor(
    private val journalDao: JournalDao,
    private val tagDao: TagDao
) : JournalRepository {

    private val gson = Gson()

    override val journalStream: Flow<List<Journal>> =
        journalDao.getAllJournalsFlow().transform { entities ->
            val journals = entities.map { entity ->
                entity.toDomainModel(emptyList()) // Load tags separately if needed
            }
            emit(journals)
        }

    override suspend fun getAllJournals(): List<Journal> {
        return execute("getAllJournals") {
            val entities = journalDao.getAllJournals()
            entities.map { entity ->
                val tags = tagDao.getTagsByJournalId(entity.id).map { it.toDomainModel() }
                entity.toDomainModel(tags)
            }
        }
    }

    override suspend fun getJournalsByMonth(date: LocalDateTime): List<Journal> {
        return execute("getJournalsByMonth") {
            val entities = journalDao.getJournalsByMonth(date)
            entities.map { entity ->
                val tags = tagDao.getTagsByJournalId(entity.id).map { it.toDomainModel() }
                entity.toDomainModel(tags)
            }
        }
    }

    override suspend fun getJournalsByDate(date: LocalDateTime): List<Journal> {
        return execute("getJournalsByDate") {
            val entities = journalDao.getJournalsByDate(date)
            entities.map { entity ->
                val tags = tagDao.getTagsByJournalId(entity.id).map { it.toDomainModel() }
                entity.toDomainModel(tags)
            }
        }
    }

    override suspend fun getJournalById(id: Int): Journal {
        return execute("getJournalById") {
            val entity = journalDao.getJournalById(id)
                ?: throw NoSuchElementException("Journal with id $id not found")
            val tags = tagDao.getTagsByJournalId(entity.id).map { it.toDomainModel() }
            entity.toDomainModel(tags)
        }
    }

    override suspend fun addJournal(dto: CreateJournalDto): Map<String, Any> {
        return execute("addJournal") {
            val entity = dto.toEntity()
            val journalId = journalDao.insertJournal(entity)
            mapOf("id" to journalId.toInt())
        }
    }

    override suspend fun updateJournal(dto: UpdateJournalDto): Int {
        return execute("updateJournal") {
            val existingEntity = journalDao.getJournalById(dto.id)
                ?: throw NoSuchElementException("Journal with id ${dto.id} not found")

            val imageUrisJson = dto.imageUris?.takeIf { it.isNotEmpty() }?.let { gson.toJson(it) }

            val updatedEntity = existingEntity.copy(
                content = dto.content ?: existingEntity.content,
                imageUris = imageUrisJson ?: existingEntity.imageUris,
                aiResponse = dto.aiResponse ?: existingEntity.aiResponse,
                latitude = dto.latitude ?: existingEntity.latitude,
                longitude = dto.longitude ?: existingEntity.longitude,
                address = dto.address ?: existingEntity.address
            )

            journalDao.updateJournal(updatedEntity)
        }
    }

    override suspend fun getJournalsByDateRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<Journal> {
        return execute("getJournalsByDateRange") {
            val entities = journalDao.getJournalsByDateRange(startDate, endDate)
            entities.map { entity ->
                val tags = tagDao.getTagsByJournalId(entity.id).map { it.toDomainModel() }
                entity.toDomainModel(tags)
            }
        }
    }

    override suspend fun deleteJournalById(id: Int) {
        return execute("deleteJournalById") {
            journalDao.deleteJournalById(id)
        }
    }

    override fun clearCache() {
        // Room doesn't require explicit cache clearing
    }
}