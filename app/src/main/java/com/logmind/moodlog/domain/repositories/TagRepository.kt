package com.logmind.moodlog.domain.repositories

import com.logmind.moodlog.domain.common.Result
import com.logmind.moodlog.domain.entities.Tag

interface TagRepository {
    suspend fun getAllTags(): Result<List<Tag>>
    suspend fun getTagById(id: Int): Result<Tag?>
    suspend fun getTagsByJournalId(journalId: Int): Result<List<Tag>>
    suspend fun addTag(name: String, color: String?): Result<Int>
    suspend fun updateTag(id: Int, name: String, color: String?): Result<Unit>
    suspend fun deleteTag(id: Int): Result<Unit>
    suspend fun addTagToJournal(journalId: Int, tagId: Int): Result<Unit>
    suspend fun removeTagFromJournal(journalId: Int, tagId: Int): Result<Unit>
    suspend fun updateJournalTags(journalId: Int, tagIds: List<Int>): Result<Unit>
}