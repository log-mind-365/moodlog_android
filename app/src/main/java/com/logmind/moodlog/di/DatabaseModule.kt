package com.logmind.moodlog.di

import android.content.Context
import androidx.room.Room
import com.logmind.moodlog.data.database.MoodLogDatabase
import com.logmind.moodlog.data.database.dao.JournalDao
import com.logmind.moodlog.data.database.dao.TagDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMoodLogDatabase(@ApplicationContext context: Context): MoodLogDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            MoodLogDatabase::class.java,
            MoodLogDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideJournalDao(database: MoodLogDatabase): JournalDao {
        return database.journalDao()
    }

    @Provides
    fun provideTagDao(database: MoodLogDatabase): TagDao {
        return database.tagDao()
    }
}