package com.pragament.buttonmapper.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.pragament.buttonmapper.data.model.ActionType
import com.pragament.buttonmapper.data.model.ButtonMapping
import com.pragament.buttonmapper.data.repository.ButtonMappingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ButtonConfigUiState(
    val mapping: ButtonMapping? = null,
    val isLoading: Boolean = true,
    val singlePressAction: ActionType = ActionType.DEFAULT,
    val doublePressAction: ActionType = ActionType.NONE,
    val longPressAction: ActionType = ActionType.NONE
)

@HiltViewModel
class ButtonConfigViewModel @Inject constructor(
    private val application: Application,
    private val repository: ButtonMappingRepository,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val mappingId: Long = savedStateHandle.get<String>("mappingId")?.toLongOrNull() ?: 0L

    private val _uiState = MutableStateFlow(ButtonConfigUiState())
    val uiState: StateFlow<ButtonConfigUiState> = _uiState.asStateFlow()

    init {
        loadMapping()
    }

    private fun loadMapping() {
        viewModelScope.launch {
            repository.getMappingByIdFlow(mappingId).collect { mapping ->
                if (mapping != null) {
                    val singleAction = try { ActionType.valueOf(mapping.singlePressActionType) } catch (e: Exception) { ActionType.DEFAULT }
                    val doubleAction = try { ActionType.valueOf(mapping.doublePressActionType) } catch (e: Exception) { ActionType.NONE }
                    val longAction = try { ActionType.valueOf(mapping.longPressActionType) } catch (e: Exception) { ActionType.NONE }

                    _uiState.update { state ->
                        state.copy(
                            mapping = mapping,
                            isLoading = false,
                            singlePressAction = singleAction,
                            doublePressAction = doubleAction,
                            longPressAction = longAction
                        )
                    }
                }
            }
        }
    }

    fun toggleEnabled(enabled: Boolean) {
        viewModelScope.launch {
            val mapping = _uiState.value.mapping ?: return@launch
            repository.updateMapping(mapping.copy(isEnabled = enabled))
        }
    }

    fun toggleVibration(enabled: Boolean) {
        viewModelScope.launch {
            val mapping = _uiState.value.mapping ?: return@launch
            repository.updateMapping(mapping.copy(vibrationEnabled = enabled))
        }
    }

    fun updateAction(pressType: String, actionType: ActionType, actionData: String = "") {
        viewModelScope.launch {
            val mapping = _uiState.value.mapping ?: return@launch
            val updatedMapping = when (pressType) {
                "single" -> mapping.copy(
                    singlePressActionType = actionType.name,
                    singlePressActionData = actionData
                )
                "double" -> mapping.copy(
                    doublePressActionType = actionType.name,
                    doublePressActionData = actionData
                )
                "long" -> mapping.copy(
                    longPressActionType = actionType.name,
                    longPressActionData = actionData
                )
                else -> mapping
            }
            repository.updateMapping(updatedMapping)
        }
    }
}
