package com.pragament.buttonmapper.ui.screens

import android.app.Application
import android.view.KeyEvent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pragament.buttonmapper.data.model.ActionType
import com.pragament.buttonmapper.data.model.ButtonInfo
import com.pragament.buttonmapper.data.model.ButtonMapping
import com.pragament.buttonmapper.data.repository.ButtonMappingRepository
import com.pragament.buttonmapper.service.ButtonAccessibilityService
import com.pragament.buttonmapper.util.AccessibilityUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val mappings: List<ButtonMapping> = emptyList(),
    val isAccessibilityEnabled: Boolean = false,
    val isLoading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val application: Application,
    private val repository: ButtonMappingRepository
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        initializeDefaultMappings()
        observeMappings()
    }

    private fun initializeDefaultMappings() {
        viewModelScope.launch {
            val count = repository.getCount()
            if (count == 0) {
                // Insert default button mappings for volume buttons
                ButtonInfo.DEFAULT_BUTTONS.forEach { button ->
                    repository.insertMapping(
                        ButtonMapping(
                            buttonKeyCode = button.keyCode,
                            buttonName = button.name,
                            buttonIcon = button.iconName,
                            isEnabled = false,
                            singlePressActionType = ActionType.DEFAULT.name,
                            doublePressActionType = ActionType.NONE.name,
                            longPressActionType = ActionType.NONE.name
                        )
                    )
                }
            }
        }
    }

    private fun observeMappings() {
        viewModelScope.launch {
            repository.getAllMappings().collect { mappings ->
                _uiState.update { state ->
                    state.copy(
                        mappings = mappings,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun refreshAccessibilityStatus() {
        _uiState.update { state ->
            state.copy(
                isAccessibilityEnabled = AccessibilityUtil.isAccessibilityServiceEnabled(application)
            )
        }
    }

    fun addCustomButton(keyCode: Int, name: String) {
        viewModelScope.launch {
            val existing = repository.getMappingByKeyCode(keyCode)
            if (existing == null) {
                repository.insertMapping(
                    ButtonMapping(
                        buttonKeyCode = keyCode,
                        buttonName = name,
                        buttonIcon = "gamepad",
                        isEnabled = false,
                        isCustomButton = true,
                        singlePressActionType = ActionType.DEFAULT.name,
                        doublePressActionType = ActionType.NONE.name,
                        longPressActionType = ActionType.NONE.name
                    )
                )
            }
        }
    }

    fun deleteMapping(mapping: ButtonMapping) {
        viewModelScope.launch {
            repository.deleteMapping(mapping)
        }
    }

    fun toggleMapping(mapping: ButtonMapping) {
        viewModelScope.launch {
            repository.updateMapping(mapping.copy(isEnabled = !mapping.isEnabled))
        }
    }
}
