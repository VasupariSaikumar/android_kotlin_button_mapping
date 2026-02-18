package com.pragament.buttonmapper.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import com.pragament.buttonmapper.data.model.ActionType
import com.pragament.buttonmapper.data.model.ButtonMapping
import com.pragament.buttonmapper.data.repository.ButtonMappingRepository
import com.pragament.buttonmapper.data.settings.AppSettings
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first

class ButtonAccessibilityService : AccessibilityService() {

    companion object {
        private const val TAG = "ButtonMapper"
        var instance: ButtonAccessibilityService? = null
            private set
        var isRunning = false
            private set
    }

    private var repository: ButtonMappingRepository? = null
    private var actionExecutor: ActionExecutor? = null
    private var appSettings: AppSettings? = null

    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val handler = Handler(Looper.getMainLooper())

    // Press detection state per key code
    private val pressStates = mutableMapOf<Int, PressState>()

    private data class PressState(
        var pressCount: Int = 0,
        var isLongPressDetected: Boolean = false,
        var lastDownTime: Long = 0,
        var pendingRunnable: Runnable? = null,
        var longPressRunnable: Runnable? = null
    )

    override fun onCreate() {
        super.onCreate()
        instance = this
        isRunning = true

        // Get dependencies from the Application
        val app = application as? com.pragament.buttonmapper.ButtonMapperApp
        if (app != null) {
            repository = app.repository
            actionExecutor = app.actionExecutor
            appSettings = app.appSettings
            actionExecutor?.setAccessibilityService(this)
        }

        Log.d(TAG, "ButtonAccessibilityService created")
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
        isRunning = false
        actionExecutor?.setAccessibilityService(null)
        serviceScope.cancel()
        // Clear all pending handlers
        pressStates.values.forEach { state ->
            state.pendingRunnable?.let { handler.removeCallbacks(it) }
            state.longPressRunnable?.let { handler.removeCallbacks(it) }
        }
        pressStates.clear()
        Log.d(TAG, "ButtonAccessibilityService destroyed")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // We don't need to process accessibility events, only key events
    }

    override fun onInterrupt() {
        Log.d(TAG, "ButtonAccessibilityService interrupted")
    }

    override fun onKeyEvent(event: KeyEvent): Boolean {
        val keyCode = event.keyCode

        // Only handle keys we're interested in
        val mapping = runBlocking {
            repository?.getMappingByKeyCode(keyCode)
        } ?: return super.onKeyEvent(event)

        // If the mapping is not enabled, pass through
        if (!mapping.isEnabled) return super.onKeyEvent(event)

        // Check if button has any custom action configured
        if (mapping.singlePressActionType == ActionType.DEFAULT.name &&
            mapping.doublePressActionType == ActionType.NONE.name &&
            mapping.longPressActionType == ActionType.NONE.name) {
            return super.onKeyEvent(event)
        }

        return handleKeyEvent(event, mapping)
    }

    private fun handleKeyEvent(event: KeyEvent, mapping: ButtonMapping): Boolean {
        val keyCode = event.keyCode
        val state = pressStates.getOrPut(keyCode) { PressState() }

        val longPressDuration = runBlocking {
            appSettings?.longPressDuration?.first() ?: AppSettings.DEFAULT_LONG_PRESS_DURATION
        }
        val doubleTapDuration = runBlocking {
            appSettings?.doubleTapDuration?.first() ?: AppSettings.DEFAULT_DOUBLE_TAP_DURATION
        }

        when (event.action) {
            KeyEvent.ACTION_DOWN -> {
                if (event.repeatCount == 0) {
                    state.lastDownTime = System.currentTimeMillis()
                    state.isLongPressDetected = false

                    // Check if long press action is configured
                    val longPressAction = try {
                        ActionType.valueOf(mapping.longPressActionType)
                    } catch (e: Exception) {
                        ActionType.NONE
                    }

                    if (longPressAction != ActionType.NONE) {
                        // Schedule long press detection
                        val longPressRunnable = Runnable {
                            state.isLongPressDetected = true
                            // Cancel any pending double tap
                            state.pendingRunnable?.let { handler.removeCallbacks(it) }
                            state.pressCount = 0

                            executeAction(mapping.longPressActionType, mapping.longPressActionData, mapping.vibrationEnabled)
                        }
                        state.longPressRunnable = longPressRunnable
                        handler.postDelayed(longPressRunnable, longPressDuration.toLong())
                    }
                }
                return true // Consume the key down event
            }

            KeyEvent.ACTION_UP -> {
                // Cancel long press if finger was released before threshold
                state.longPressRunnable?.let { handler.removeCallbacks(it) }

                if (state.isLongPressDetected) {
                    // Long press already handled
                    state.isLongPressDetected = false
                    return true
                }

                state.pressCount++

                val doublePressAction = try {
                    ActionType.valueOf(mapping.doublePressActionType)
                } catch (e: Exception) {
                    ActionType.NONE
                }

                if (doublePressAction != ActionType.NONE) {
                    // Double press is configured
                    if (state.pressCount == 1) {
                        // First press - wait for possible second press
                        state.pendingRunnable?.let { handler.removeCallbacks(it) }
                        val pendingRunnable = Runnable {
                            // No second press came - execute single press action
                            state.pressCount = 0
                            executeAction(mapping.singlePressActionType, mapping.singlePressActionData, mapping.vibrationEnabled)
                        }
                        state.pendingRunnable = pendingRunnable
                        handler.postDelayed(pendingRunnable, doubleTapDuration.toLong())
                    } else if (state.pressCount >= 2) {
                        // Second press - execute double press action
                        state.pendingRunnable?.let { handler.removeCallbacks(it) }
                        state.pressCount = 0
                        executeAction(mapping.doublePressActionType, mapping.doublePressActionData, mapping.vibrationEnabled)
                    }
                } else {
                    // No double press configured - execute single press immediately
                    state.pressCount = 0
                    executeAction(mapping.singlePressActionType, mapping.singlePressActionData, mapping.vibrationEnabled)
                }
                return true // Consume the key up event
            }
        }

        return super.onKeyEvent(event)
    }

    private fun executeAction(actionTypeStr: String, actionData: String, vibrate: Boolean) {
        try {
            val actionType = ActionType.valueOf(actionTypeStr)
            if (actionType == ActionType.DEFAULT || actionType == ActionType.NONE) return

            if (vibrate) {
                actionExecutor?.vibrate()
            }

            actionExecutor?.executeAction(actionType, actionData)
        } catch (e: Exception) {
            Log.e(TAG, "Error executing action: $actionTypeStr", e)
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        isRunning = true
        
        val info = serviceInfo
        info.flags = android.accessibilityservice.AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS or
                    android.accessibilityservice.AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS
        serviceInfo = info
        
        Log.d(TAG, "ButtonAccessibilityService connected with flags: ${info.flags}")
    }
}
