package com.logmind.moodlog.data.repositories

import com.logmind.moodlog.data.database.dao.JournalDao
import com.logmind.moodlog.data.database.dao.TagDao
import com.logmind.moodlog.data.mappers.toDomainModel
import com.logmind.moodlog.data.mappers.toEntity
import com.logmind.moodlog.domain.common.Result
import com.logmind.moodlog.domain.entities.CreateJournalDto
import com.logmind.moodlog.domain.entities.Journal
import com.logmind.moodlog.domain.entities.UpdateJournalDto
import com.logmind.moodlog.domain.repositories.JournalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import com.google.gson.Gson
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

    override suspend fun getAllJournals(): Result<List<Journal>> {
        return try {
            val entities = journalDao.getAllJournals()
            val journals = entities.map { entity ->
                val tags = tagDao.getTagsByJournalId(entity.id).map { it.toDomainModel() }
                entity.toDomainModel(tags)
            }
            Result.success(journals)
        } catch (e: Throwable) {
            Result.error(e)
        }
    }

    override suspend fun getJournalsByMonth(date: LocalDateTime): Result<List<Journal>> {
        return try {
            val entities = journalDao.getJournalsByMonth(date)
            val journals = entities.map { entity ->
                val tags = tagDao.getTagsByJournalId(entity.id).map { it.toDomainModel() }
                entity.toDomainModel(tags)
            }
            Result.success(journals)
        } catch (e: Throwable) {
            Result.error(e)
        }
    }

    override suspend fun getJournalsByDate(date: LocalDateTime): Result<List<Journal>> {
        return try {
            val entities = journalDao.getJournalsByDate(date)
            val journals = entities.map { entity ->
                val tags = tagDao.getTagsByJournalId(entity.id).map { it.toDomainModel() }
                entity.toDomainModel(tags)
            }
            Result.success(journals)
        } catch (e: Throwable) {
            Result.error(e)
        }
    }

    override suspend fun getJournalById(id: Int): Result<Journal> {
        return try {
            val entity = journalDao.getJournalById(id)
                ?: throw NoSuchElementException("Journal with id $id not found")
            val tags = tagDao.getTagsByJournalId(entity.id).map { it.toDomainModel() }
            val journal = entity.toDomainModel(tags)
            Result.success(journal)
        } catch (e: Throwable) {
            Result.error(e)
        }
    }

    override suspend fun addJournal(dto: CreateJournalDto): Result<Map<String, Any>> {
        return try {
            val entity = dto.toEntity()
            val journalId = journalDao.insertJournal(entity)
            val result = mapOf("id" to journalId.toInt())
            Result.success(result)
        } catch (e: Throwable) {
            Result.error(e)
        }
    }

    override suspend fun updateJournal(dto: UpdateJournalDto): Result<Int> {
        return try {
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
            
            val result = journalDao.updateJournal(updatedEntity)
            Result.success(result)
        } catch (e: Throwable) {
            Result.error(e)
        }
    }

    override suspend fun deleteJournalById(id: Int): Result<Unit> {
        return try {
            journalDao.deleteJournalById(id)
            Result.success(Unit)
        } catch (e: Throwable) {
            Result.error(e)
        }
    }

    override fun clearCache() {
        // Room doesn't require explicit cache clearing
    }
}