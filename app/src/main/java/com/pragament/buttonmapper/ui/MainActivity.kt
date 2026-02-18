package com.pragament.buttonmapper.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.pragament.buttonmapper.ui.navigation.NavGraph
import com.pragament.buttonmapper.ui.screens.HomeViewModel
import com.pragament.buttonmapper.ui.screens.SettingsViewModel
import com.pragament.buttonmapper.ui.theme.ButtonMapperTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val settingsState by settingsViewModel.uiState.collectAsState()

            ButtonMapperTheme(darkTheme = settingsState.darkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val homeViewModel: HomeViewModel = hiltViewModel()

                    NavGraph(
                        navController = navController,
                        onAddCustomButton = { keyCode, name ->
                            homeViewModel.addCustomButton(keyCode, name)
                        }
                    )
                }
            }
        }
    }
}
