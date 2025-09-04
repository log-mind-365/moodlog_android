package com.logmind.moodlog.data.repositories

import com.logmind.moodlog.data.database.dao.JournalDao
import com.logmind.moodlog.data.database.dao.TagDao
import com.logmind.moodlog.data.mappers.createTagEntity
import com.logmind.moodlog.data.mappers.toDomainModel
import com.logmind.moodlog.domain.entities.Tag
import com.logmind.moodlog.domain.repositories.TagRepository
import com.logmind.moodlog.utils.execute
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TagRepositoryImpl @Inject constructor(
    private val tagDao: TagDao,
    private val journalDao: JournalDao
) : TagRepository {

    override suspend fun getAllTags(): List<Tag> {
        return execute("getAllTags") {
            tagDao.getAllTags().map { it.toDomainModel() }
        }
    }

    override suspend fun getTagById(id: Int): Tag? {
        return execute("getTagById") {
            tagDao.getTagById(id)?.toDomainModel()
                ?: throw NoSuchElementException("Tag with id $id not found")
        }

    }

    override suspend fun getTagsByJournalId(journalId: Int): List<Tag> {
        return execute("getTagsByJournalId") {
            tagDao.getTagsByJournalId(journalId).map { it.toDomainModel() }
        }
    }

    override suspend fun addTag(name: String, color: String?): Int {
        return execute("addTag") {
            val entity = createTagEntity(name, color)
            tagDao.insertTag(entity).toInt()
        }
    }

    override suspend fun updateTag(id: Int, name: String, color: String?) {
        return execute("updateTag") {
            val existingTag = tagDao.getTagById(id)
                ?: throw NoSuchElementException("Tag with id $id not found")
            val updatedTag = existingTag.copy(name = name, color = color)
            tagDao.updateTag(updatedTag)
        }
    }

    override suspend fun deleteTag(id: Int) {
        return execute("deleteTag") {
            tagDao.deleteTag(id)
        }
    }

    override suspend fun addTagToJournal(journalId: Int, tagId: Int) {
        return execute("addTagToJournal") {
            // This should be handled through JournalDao
            // Implementation depends on your database schema
        }
    }

    override suspend fun removeTagFromJournal(journalId: Int, tagId: Int) {
        return execute("removeTagFromJournal") {
            // This should be handled through JournalDao
            // Implementation depends on your database schema
        }
    }

    override suspend fun updateJournalTags(journalId: Int, tagIds: List<Int>) {
        return execute("updateJournalTags") {
            journalDao.updateJournalTags(journalId, tagIds)
        }
    }


}