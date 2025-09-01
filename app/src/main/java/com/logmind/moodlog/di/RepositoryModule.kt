package com.logmind.moodlog.di

import com.logmind.moodlog.data.repositories.JournalRepositoryImpl
import com.logmind.moodlog.data.repositories.TagRepositoryImpl
import com.logmind.moodlog.domain.repositories.JournalRepository
import com.logmind.moodlog.domain.repositories.TagRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindJournalRepository(
        journalRepositoryImpl: JournalRepositoryImpl
    ): JournalRepository

    @Binds
    @Singleton
    abstract fun bindTagRepository(
        tagRepositoryImpl: TagRepositoryImpl
    ): TagRepository
}