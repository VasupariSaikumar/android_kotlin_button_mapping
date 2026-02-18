package com.pragament.buttonmapper.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pragament.buttonmapper.data.settings.AppSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val longPressDuration: Int = AppSettings.DEFAULT_LONG_PRESS_DURATION,
    val doubleTapDuration: Int = AppSettings.DEFAULT_DOUBLE_TAP_DURATION,
    val delayInitialPress: Boolean = false,
    val darkTheme: Boolean = true,
    val hapticFeedback: Boolean = true
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val application: Application,
    private val appSettings: AppSettings
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                appSettings.longPressDuration,
                appSettings.doubleTapDuration,
                appSettings.delayInitialPress,
                appSettings.darkTheme,
                appSettings.hapticFeedback
            ) { longPress, doubleTap, delay, dark, haptic ->
                SettingsUiState(
                    longPressDuration = longPress,
                    doubleTapDuration = doubleTap,
                    delayInitialPress = delay,
                    darkTheme = dark,
                    hapticFeedback = haptic
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun setLongPressDuration(duration: Int) {
        viewModelScope.launch {
            appSettings.setLongPressDuration(duration)
        }
    }

    fun setDoubleTapDuration(duration: Int) {
        viewModelScope.launch {
            appSettings.setDoubleTapDuration(duration)
        }
    }

    fun setDelayInitialPress(delay: Boolean) {
        viewModelScope.launch {
            appSettings.setDelayInitialPress(delay)
        }
    }

    fun setDarkTheme(dark: Boolean) {
        viewModelScope.launch {
            appSettings.setDarkTheme(dark)
        }
    }

    fun setHapticFeedback(enabled: Boolean) {
        viewModelScope.launch {
            appSettings.setHapticFeedback(enabled)
        }
    }
}
