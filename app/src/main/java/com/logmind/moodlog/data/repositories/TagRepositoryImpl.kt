package com.logmind.moodlog.data.repositories

import com.logmind.moodlog.data.database.dao.JournalDao
import com.logmind.moodlog.data.database.dao.TagDao
import com.logmind.moodlog.data.mappers.createTagEntity
import com.logmind.moodlog.data.mappers.toDomainModel
import com.logmind.moodlog.domain.common.Result
import com.logmind.moodlog.domain.entities.Tag
import com.logmind.moodlog.domain.repositories.TagRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TagRepositoryImpl @Inject constructor(
    private val tagDao: TagDao,
    private val journalDao: JournalDao
) : TagRepository {

    override suspend fun getAllTags(): Result<List<Tag>> {
        return try {
            val tags = tagDao.getAllTags().map { it.toDomainModel() }
            Result.success(tags)
        } catch (e: Throwable) {
            Result.error(e)
        }
    }

    override suspend fun getTagById(id: Int): Result<Tag?> {
        return try {
            val tag = tagDao.getTagById(id)?.toDomainModel()
            Result.success(tag)
        } catch (e: Throwable) {
            Result.error(e)
        }
    }

    override suspend fun getTagsByJournalId(journalId: Int): Result<List<Tag>> {
        return try {
            val tags = tagDao.getTagsByJournalId(journalId).map { it.toDomainModel() }
            Result.success(tags)
        } catch (e: Throwable) {
            Result.error(e)
        }
    }

    override suspend fun addTag(name: String, color: String?): Result<Int> {
        return try {
            val entity = createTagEntity(name, color)
            val tagId = tagDao.insertTag(entity).toInt()
            Result.success(tagId)
        } catch (e: Throwable) {
            Result.error(e)
        }
    }

    override suspend fun updateTag(id: Int, name: String, color: String?): Result<Unit> {
        return try {
            val existingTag = tagDao.getTagById(id)
                ?: throw NoSuchElementException("Tag with id $id not found")
            val updatedTag = existingTag.copy(name = name, color = color)
            tagDao.updateTag(updatedTag)
            Result.success(Unit)
        } catch (e: Throwable) {
            Result.error(e)
        }
    }

    override suspend fun deleteTag(id: Int): Result<Unit> {
        return try {
            tagDao.deleteTag(id)
            Result.success(Unit)
        } catch (e: Throwable) {
            Result.error(e)
        }
    }

    override suspend fun addTagToJournal(journalId: Int, tagId: Int): Result<Unit> {
        return try {
            // This should be handled through JournalDao
            // Implementation depends on your database schema
            Result.success(Unit)
        } catch (e: Throwable) {
            Result.error(e)
        }
    }

    override suspend fun removeTagFromJournal(journalId: Int, tagId: Int): Result<Unit> {
        return try {
            // This should be handled through JournalDao
            // Implementation depends on your database schema
            Result.success(Unit)
        } catch (e: Throwable) {
            Result.error(e)
        }
    }

    override suspend fun updateJournalTags(journalId: Int, tagIds: List<Int>): Result<Unit> {
        return try {
            journalDao.updateJournalTags(journalId, tagIds)
            Result.success(Unit)
        } catch (e: Throwable) {
            Result.error(e)
        }
    }
}