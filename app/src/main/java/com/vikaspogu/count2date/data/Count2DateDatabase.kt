package com.vikaspogu.count2date.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vikaspogu.count2date.data.dao.EventDao
import com.vikaspogu.count2date.data.model.Event

@Database(entities = [Event::class], version = 1, exportSchema = false)
abstract class Count2DateDatabase : RoomDatabase(){
    abstract fun eventDao(): EventDao
    companion object {
        const val DATABASE_NAME = "count2date_database"
    }
}