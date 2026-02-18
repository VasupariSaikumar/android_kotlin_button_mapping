package com.pragament.buttonmapper.data.model

enum class ActionType(val displayName: String, val iconName: String, val category: String) {
    // Default / No action
    DEFAULT("Default", "radio_button_unchecked", "System"),
    NONE("No Action", "block", "System"),
    DISABLED("Disable Button", "do_not_disturb", "System"),

    // Apps
    LAUNCH_APP("Launch App", "apps", "Apps"),
    LAUNCH_SHORTCUT("Launch Shortcut", "shortcut", "Apps"),

    // System Actions
    HOME("Home", "home", "System"),
    BACK("Back", "arrow_back", "System"),
    RECENTS("Recent Apps", "view_carousel", "System"),
    SCREEN_OFF("Turn Screen Off", "screen_lock_portrait", "System"),
    POWER_DIALOG("Power Dialog", "power_settings_new", "System"),
    SCREENSHOT("Take Screenshot", "screenshot", "System"),
    NOTIFICATIONS("Show Notifications", "notifications", "System"),
    QUICK_SETTINGS("Quick Settings", "settings", "System"),
    SPLIT_SCREEN("Split Screen", "splitscreen", "System"),
    CLEAR_NOTIFICATIONS("Clear Notifications", "notifications_off", "System"),
    LAST_APP("Last App", "swap_horiz", "System"),
    MENU("Menu", "menu", "System"),

    // Media
    MEDIA_PLAY_PAUSE("Play/Pause", "play_arrow", "Media"),
    MEDIA_NEXT("Next Track", "skip_next", "Media"),
    MEDIA_PREVIOUS("Previous Track", "skip_previous", "Media"),
    VOLUME_UP("Volume Up", "volume_up", "Media"),
    VOLUME_DOWN("Volume Down", "volume_down", "Media"),
    MUTE("Mute", "volume_off", "Media"),

    // Toggles
    FLASHLIGHT("Toggle Flashlight", "flashlight_on", "Toggles"),
    WIFI("Toggle WiFi", "wifi", "Toggles"),
    BLUETOOTH("Toggle Bluetooth", "bluetooth", "Toggles"),
    ROTATION("Toggle Rotation", "screen_rotation", "Toggles"),
    DO_NOT_DISTURB("Toggle Do Not Disturb", "do_not_disturb_on", "Toggles"),

    // Display
    BRIGHTNESS_UP("Brightness Up", "brightness_high", "Display"),
    BRIGHTNESS_DOWN("Brightness Down", "brightness_low", "Display"),

    // Camera
    CAMERA_SHUTTER("Camera Shutter", "camera", "Camera");

    companion object {
        fun getCategories(): List<String> = listOf("System", "Apps", "Media", "Toggles", "Display", "Camera")

        fun getByCategory(category: String): List<ActionType> =
            entries.filter { it.category == category && it != DEFAULT }
    }
}
