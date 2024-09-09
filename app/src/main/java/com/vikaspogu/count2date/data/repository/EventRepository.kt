package com.vikaspogu.count2date.data.repository

import com.vikaspogu.count2date.data.model.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository{
    fun getAllEvents(): Flow<List<Event>>
    fun getAllEventsByDate(date: Long): Flow<List<Event>>
    suspend fun insertEvent(event: Event)
    suspend fun updateEvent(event: Event)
    suspend fun deleteEvent(id: Int)
}
