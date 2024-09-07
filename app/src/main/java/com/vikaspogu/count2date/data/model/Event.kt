package com.vikaspogu.count2date.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val description: String,
    @ColumnInfo(name = "event_date")
    val eventDate: Long = 0L,
)
