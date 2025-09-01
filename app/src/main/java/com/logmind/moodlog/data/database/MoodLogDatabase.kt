package com.logmind.moodlog.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.logmind.moodlog.data.database.converters.Converters
import com.logmind.moodlog.data.database.dao.JournalDao
import com.logmind.moodlog.data.database.dao.TagDao
import com.logmind.moodlog.data.database.entities.JournalEntity
import com.logmind.moodlog.data.database.entities.JournalTagCrossRef
import com.logmind.moodlog.data.database.entities.TagEntity

@Database(
    entities = [
        JournalEntity::class,
        TagEntity::class,
        JournalTagCrossRef::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class MoodLogDatabase : RoomDatabase() {
    
    abstract fun journalDao(): JournalDao
    abstract fun tagDao(): TagDao
    
    companion object {
        const val DATABASE_NAME = "moodlog_database"
        
        @Volatile
        private var INSTANCE: MoodLogDatabase? = null
        
        fun getDatabase(context: Context): MoodLogDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MoodLogDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}