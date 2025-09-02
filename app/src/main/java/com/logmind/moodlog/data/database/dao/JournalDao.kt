package com.logmind.moodlog.data.database.dao

import androidx.room.*
import com.logmind.moodlog.data.database.entities.JournalEntity
import com.logmind.moodlog.data.database.entities.JournalTagCrossRef
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface JournalDao {
    
    @Query("SELECT * FROM journals ORDER BY created_at DESC")
    fun getAllJournalsFlow(): Flow<List<JournalEntity>>
    
    @Query("SELECT * FROM journals ORDER BY created_at DESC")
    suspend fun getAllJournals(): List<JournalEntity>
    
    @Query("""
        SELECT * FROM journals 
        WHERE DATE(created_at) = DATE(:date)
        ORDER BY created_at DESC
    """)
    suspend fun getJournalsByDate(date: LocalDateTime): List<JournalEntity>
    
    @Query("""
        SELECT * FROM journals 
        WHERE strftime('%Y-%m', created_at) = strftime('%Y-%m', :date)
        ORDER BY created_at DESC
    """)
    suspend fun getJournalsByMonth(date: LocalDateTime): List<JournalEntity>
    
    @Query("""
        SELECT * FROM journals 
        WHERE created_at BETWEEN :startDate AND :endDate
        ORDER BY created_at DESC
    """)
    suspend fun getJournalsByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): List<JournalEntity>
    
    @Query("SELECT * FROM journals WHERE id = :id")
    suspend fun getJournalById(id: Int): JournalEntity?
    
    @Insert
    suspend fun insertJournal(journal: JournalEntity): Long
    
    @Update
    suspend fun updateJournal(journal: JournalEntity): Int
    
    @Query("DELETE FROM journals WHERE id = :id")
    suspend fun deleteJournalById(id: Int)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJournalTagCrossRef(crossRef: JournalTagCrossRef)
    
    @Delete
    suspend fun deleteJournalTagCrossRef(crossRef: JournalTagCrossRef)
    
    @Query("DELETE FROM journal_tag_cross_ref WHERE journal_id = :journalId")
    suspend fun deleteAllTagsForJournal(journalId: Int)
    
    @Transaction
    suspend fun updateJournalTags(journalId: Int, tagIds: List<Int>) {
        deleteAllTagsForJournal(journalId)
        tagIds.forEach { tagId ->
            insertJournalTagCrossRef(JournalTagCrossRef(journalId, tagId))
        }
    }
}