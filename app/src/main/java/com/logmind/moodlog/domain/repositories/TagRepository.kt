package com.logmind.moodlog.domain.repositories

import com.logmind.moodlog.domain.entities.Tag

interface TagRepository {
    suspend fun getAllTags(): List<Tag>
    suspend fun getTagById(id: Int): Tag?
    suspend fun getTagsByJournalId(journalId: Int): List<Tag>
    suspend fun addTag(name: String, color: String?): Int
    suspend fun updateTag(id: Int, name: String, color: String?)
    suspend fun deleteTag(id: Int)
    suspend fun addTagToJournal(journalId: Int, tagId: Int)
    suspend fun removeTagFromJournal(journalId: Int, tagId: Int)
    suspend fun updateJournalTags(journalId: Int, tagIds: List<Int>)
}