package com.logmind.moodlog.domain.usecases

import com.logmind.moodlog.domain.common.Result
import com.logmind.moodlog.domain.entities.Tag
import com.logmind.moodlog.domain.repositories.TagRepository
import javax.inject.Inject

class TagUseCase @Inject constructor(
    private val tagRepository: TagRepository
) {
    suspend fun addTag(name: String, color: String?): Result<Int> {
        return tagRepository.addTag(name, color)
    }

    suspend fun deleteTag(id: Int): Result<Unit> {
        return tagRepository.deleteTag(id)
    }

    suspend fun getAllTags(): Result<List<Tag>> {
        return tagRepository.getAllTags()
    }

    suspend fun getTagsByJournalId(journalId: Int): Result<List<Tag>> {
        return tagRepository.getTagsByJournalId(journalId)
    }

    suspend fun updateJournalTags(journalId: Int, tagIds: List<Int>): Result<Unit> {
        return tagRepository.updateJournalTags(journalId, tagIds)
    }

    suspend fun updateTag(id: Int, name: String, color: String?): Result<Unit> {
        return tagRepository.updateTag(id, name, color)
    }
}