package com.pragament.buttonmapper.di

import android.content.Context
import androidx.room.Room
import com.pragament.buttonmapper.data.db.AppDatabase
import com.pragament.buttonmapper.data.db.ButtonMappingDao
import com.pragament.buttonmapper.data.repository.ButtonMappingRepository
import com.pragament.buttonmapper.data.settings.AppSettings
import com.pragament.buttonmapper.service.ActionExecutor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "button_mapper_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideButtonMappingDao(database: AppDatabase): ButtonMappingDao {
        return database.buttonMappingDao()
    }

    @Provides
    @Singleton
    fun provideButtonMappingRepository(dao: ButtonMappingDao): ButtonMappingRepository {
        return ButtonMappingRepository(dao)
    }

    @Provides
    @Singleton
    fun provideAppSettings(@ApplicationContext context: Context): AppSettings {
        return AppSettings(context)
    }

    @Provides
    @Singleton
    fun provideActionExecutor(@ApplicationContext context: Context): ActionExecutor {
        return ActionExecutor(context)
    }
}
