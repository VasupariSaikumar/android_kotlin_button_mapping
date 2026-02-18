package com.pragament.buttonmapper

import android.app.Application
import androidx.room.Room
import com.pragament.buttonmapper.data.db.AppDatabase
import com.pragament.buttonmapper.data.repository.ButtonMappingRepository
import com.pragament.buttonmapper.data.settings.AppSettings
import com.pragament.buttonmapper.service.ActionExecutor
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ButtonMapperApp : Application() {

    lateinit var database: AppDatabase
        private set
    lateinit var repository: ButtonMappingRepository
        private set
    lateinit var actionExecutor: ActionExecutor
        private set
    lateinit var appSettings: AppSettings
        private set

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "button_mapper_db"
        ).build()

        repository = ButtonMappingRepository(database.buttonMappingDao())
        actionExecutor = ActionExecutor(applicationContext)
        appSettings = AppSettings(applicationContext)
    }
}
