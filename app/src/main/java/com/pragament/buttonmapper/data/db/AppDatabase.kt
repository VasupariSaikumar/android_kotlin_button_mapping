package com.pragament.buttonmapper.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pragament.buttonmapper.data.model.ButtonMapping

@Database(entities = [ButtonMapping::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun buttonMappingDao(): ButtonMappingDao
}
