package com.pragament.buttonmapper.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pragament.buttonmapper.data.model.ActionType
import com.pragament.buttonmapper.ui.screens.*

@Composable
fun NavGraph(
    navController: NavHostController,
    onAddCustomButton: (Int, String) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onButtonClick = { mappingId ->
                    navController.navigate(Screen.ButtonConfig.createRoute(mappingId))
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                },
                onAddCustomButton = {
                    navController.navigate(Screen.CustomButton.route)
                }
            )
        }

        composable(
            route = Screen.ButtonConfig.route,
            arguments = listOf(
                navArgument("mappingId") { type = NavType.StringType }
            )
        ) {
            ButtonConfigScreen(
                onBack = { navController.popBackStack() },
                onSelectAction = { mappingId, pressType ->
                    navController.navigate(Screen.ActionPicker.createRoute(mappingId, pressType))
                }
            )
        }

        composable(
            route = Screen.ActionPicker.route,
            arguments = listOf(
                navArgument("mappingId") { type = NavType.StringType },
                navArgument("pressType") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val mappingId = backStackEntry.arguments?.getString("mappingId")?.toLongOrNull() ?: 0L
            val pressType = backStackEntry.arguments?.getString("pressType") ?: "single"

            val buttonConfigViewModel: ButtonConfigViewModel = hiltViewModel(
                navController.getBackStackEntry(Screen.ButtonConfig.createRoute(mappingId))
            )

            ActionPickerScreen(
                pressType = pressType,
                onBack = { navController.popBackStack() },
                onActionSelected = { actionType ->
                    buttonConfigViewModel.updateAction(pressType, actionType)
                    navController.popBackStack()
                },
                onAppPickerRequested = {
                    navController.navigate(Screen.AppPicker.createRoute(mappingId, pressType))
                }
            )
        }

        composable(
            route = Screen.AppPicker.route,
            arguments = listOf(
                navArgument("mappingId") { type = NavType.StringType },
                navArgument("pressType") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val mappingId = backStackEntry.arguments?.getString("mappingId")?.toLongOrNull() ?: 0L
            val pressType = backStackEntry.arguments?.getString("pressType") ?: "single"

            val buttonConfigViewModel: ButtonConfigViewModel = hiltViewModel(
                navController.getBackStackEntry(Screen.ButtonConfig.createRoute(mappingId))
            )

            AppPickerScreen(
                onBack = { navController.popBackStack() },
                onAppSelected = { packageName ->
                    buttonConfigViewModel.updateAction(pressType, ActionType.LAUNCH_APP, packageName)
                    // Pop back to button config screen
                    navController.popBackStack(Screen.ButtonConfig.createRoute(mappingId), false)
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onExcludedAppsClick = {
                    navController.navigate(Screen.ExcludedApps.route)
                }
            )
        }

        composable(Screen.ExcludedApps.route) {
            // Reuse app picker as excluded apps selector
            AppPickerScreen(
                onBack = { navController.popBackStack() },
                onAppSelected = { _ ->
                    // TODO: Add to excluded apps
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.CustomButton.route) {
            CustomButtonScreen(
                onBack = { navController.popBackStack() },
                onButtonAdded = { keyCode, name ->
                    onAddCustomButton(keyCode, name)
                }
            )
        }
    }
}
