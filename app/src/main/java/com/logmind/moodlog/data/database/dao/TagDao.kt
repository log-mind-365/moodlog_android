package com.logmind.moodlog.data.database.dao

import androidx.room.*
import com.logmind.moodlog.data.database.entities.TagEntity

@Dao
interface TagDao {
    
    @Query("SELECT * FROM tags ORDER BY created_at DESC")
    suspend fun getAllTags(): List<TagEntity>
    
    @Query("SELECT * FROM tags WHERE id = :id")
    suspend fun getTagById(id: Int): TagEntity?
    
    @Query("""
        SELECT t.* FROM tags t 
        INNER JOIN journal_tag_cross_ref jt ON t.id = jt.tag_id 
        WHERE jt.journal_id = :journalId
        ORDER BY t.created_at DESC
    """)
    suspend fun getTagsByJournalId(journalId: Int): List<TagEntity>
    
    @Insert
    suspend fun insertTag(tag: TagEntity): Long
    
    @Update
    suspend fun updateTag(tag: TagEntity)
    
    @Query("DELETE FROM tags WHERE id = :id")
    suspend fun deleteTag(id: Int)
}