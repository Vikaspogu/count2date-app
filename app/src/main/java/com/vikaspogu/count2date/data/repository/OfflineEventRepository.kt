package com.vikaspogu.count2date.data.repository

import com.vikaspogu.count2date.data.dao.EventDao
import com.vikaspogu.count2date.data.model.Event
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineEventRepository @Inject constructor(private val eventDao: EventDao): EventRepository {
    override fun getAllEvents(): Flow<List<Event>> = eventDao.getAllEvents()

    override suspend fun insertEvent(event: Event) = eventDao.insert(event)

    override suspend fun updateEvent(event: Event) = eventDao.update(event)

    override suspend fun deleteEvent(id: Int) = eventDao.delete(id)
}