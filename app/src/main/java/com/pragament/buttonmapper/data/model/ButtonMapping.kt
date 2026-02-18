package com.pragament.buttonmapper.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "button_mappings")
data class ButtonMapping(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val buttonKeyCode: Int,
    val buttonName: String,
    val buttonIcon: String = "",
    val isEnabled: Boolean = true,
    val singlePressActionType: String = ActionType.DEFAULT.name,
    val singlePressActionData: String = "",
    val doublePressActionType: String = ActionType.NONE.name,
    val doublePressActionData: String = "",
    val longPressActionType: String = ActionType.NONE.name,
    val longPressActionData: String = "",
    val vibrationEnabled: Boolean = false,
    val isCustomButton: Boolean = false
)
