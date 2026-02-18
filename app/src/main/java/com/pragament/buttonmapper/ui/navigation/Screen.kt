package com.pragament.buttonmapper.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object ButtonConfig : Screen("button_config/{mappingId}") {
        fun createRoute(mappingId: Long) = "button_config/$mappingId"
    }
    data object ActionPicker : Screen("action_picker/{mappingId}/{pressType}") {
        fun createRoute(mappingId: Long, pressType: String) = "action_picker/$mappingId/$pressType"
    }
    data object AppPicker : Screen("app_picker/{mappingId}/{pressType}") {
        fun createRoute(mappingId: Long, pressType: String) = "app_picker/$mappingId/$pressType"
    }
    data object Settings : Screen("settings")
    data object CustomButton : Screen("custom_button")
    data object ExcludedApps : Screen("excluded_apps")
}
