package com.pragament.buttonmapper.data.model

import android.view.KeyEvent

data class ButtonInfo(
    val keyCode: Int,
    val name: String,
    val iconName: String
) {
    companion object {
        val DEFAULT_BUTTONS = listOf(
            ButtonInfo(KeyEvent.KEYCODE_VOLUME_UP, "Volume Up", "volume_up"),
            ButtonInfo(KeyEvent.KEYCODE_VOLUME_DOWN, "Volume Down", "volume_down"),
        )

        val OPTIONAL_BUTTONS = listOf(
            ButtonInfo(KeyEvent.KEYCODE_HOME, "Home", "home"),
            ButtonInfo(KeyEvent.KEYCODE_BACK, "Back", "arrow_back"),
            ButtonInfo(KeyEvent.KEYCODE_APP_SWITCH, "Recent Apps", "view_carousel"),
            ButtonInfo(KeyEvent.KEYCODE_MENU, "Menu", "menu"),
            ButtonInfo(KeyEvent.KEYCODE_CAMERA, "Camera", "camera"),
            ButtonInfo(KeyEvent.KEYCODE_HEADSETHOOK, "Headset", "headphones"),
            ButtonInfo(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, "Media Button", "play_arrow"),
        )
    }
}
