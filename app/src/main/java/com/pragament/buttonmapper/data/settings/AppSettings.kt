package com.pragament.buttonmapper.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "button_mapper_settings")

@Singleton
class AppSettings @Inject constructor(
    private val context: Context
) {
    companion object {
        val LONG_PRESS_DURATION = intPreferencesKey("long_press_duration")
        val DOUBLE_TAP_DURATION = intPreferencesKey("double_tap_duration")
        val DELAY_INITIAL_PRESS = booleanPreferencesKey("delay_initial_press")
        val EXCLUDED_APPS = stringPreferencesKey("excluded_apps")
        val DARK_THEME = booleanPreferencesKey("dark_theme")
        val HAPTIC_FEEDBACK = booleanPreferencesKey("haptic_feedback")
        val SERVICE_ENABLED = booleanPreferencesKey("service_enabled")

        const val DEFAULT_LONG_PRESS_DURATION = 500
        const val DEFAULT_DOUBLE_TAP_DURATION = 400
    }

    val longPressDuration: Flow<Int> = context.dataStore.data
        .map { it[LONG_PRESS_DURATION] ?: DEFAULT_LONG_PRESS_DURATION }

    val doubleTapDuration: Flow<Int> = context.dataStore.data
        .map { it[DOUBLE_TAP_DURATION] ?: DEFAULT_DOUBLE_TAP_DURATION }

    val delayInitialPress: Flow<Boolean> = context.dataStore.data
        .map { it[DELAY_INITIAL_PRESS] ?: false }

    val excludedApps: Flow<Set<String>> = context.dataStore.data
        .map {
            val appsString = it[EXCLUDED_APPS] ?: ""
            if (appsString.isEmpty()) emptySet() else appsString.split(",").toSet()
        }

    val darkTheme: Flow<Boolean> = context.dataStore.data
        .map { it[DARK_THEME] ?: true }

    val hapticFeedback: Flow<Boolean> = context.dataStore.data
        .map { it[HAPTIC_FEEDBACK] ?: true }

    suspend fun setLongPressDuration(duration: Int) {
        context.dataStore.edit { it[LONG_PRESS_DURATION] = duration }
    }

    suspend fun setDoubleTapDuration(duration: Int) {
        context.dataStore.edit { it[DOUBLE_TAP_DURATION] = duration }
    }

    suspend fun setDelayInitialPress(delay: Boolean) {
        context.dataStore.edit { it[DELAY_INITIAL_PRESS] = delay }
    }

    suspend fun setExcludedApps(apps: Set<String>) {
        context.dataStore.edit { it[EXCLUDED_APPS] = apps.joinToString(",") }
    }

    suspend fun setDarkTheme(dark: Boolean) {
        context.dataStore.edit { it[DARK_THEME] = dark }
    }

    suspend fun setHapticFeedback(enabled: Boolean) {
        context.dataStore.edit { it[HAPTIC_FEEDBACK] = enabled }
    }
}
