package com.vikaspogu.count2date.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.vikaspogu.count2date.data.model.Event
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao{
    @Query("SELECT * FROM events ORDER BY event_date ASC")
    fun getAllEvents(): Flow<List<Event>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(event: Event)

    @Update
    suspend fun update(event: Event)

    @Query("DELETE FROM events WHERE id = :id")
    suspend fun delete(id: Int)
}