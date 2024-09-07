package com.vikaspogu.count2date.data.di

import android.content.Context
import androidx.room.Room
import com.vikaspogu.count2date.data.Count2DateDatabase
import com.vikaspogu.count2date.data.dao.EventDao
import com.vikaspogu.count2date.data.repository.EventRepository
import com.vikaspogu.count2date.data.repository.OfflineEventRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCount2DateDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        Count2DateDatabase::class.java,
        Count2DateDatabase.DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun providesEventDao(database: Count2DateDatabase): EventDao = database.eventDao()

    @Singleton
    @Provides
    fun providesEventRepository(eventDao: EventDao): EventRepository = OfflineEventRepository(eventDao)

}