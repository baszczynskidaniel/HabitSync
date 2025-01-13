package org.example.project.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import org.example.project.core.presentation.ui.AppTheme
import org.example.project.settings.presentation.language.LanguageScreen
import org.example.project.settings.presentation.language.LanguageViewModel
import org.example.project.settings.presentation.settings_list.SettingsScreen
import org.example.project.settings.presentation.settings_list.SettingsViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(

) {

    val appViewModel = koinViewModel<AppViewModel>()
    val appState = appViewModel.state.collectAsState()

    AppTheme(
        language = appState.value.language,
        darkTheme = appState.value.darkTheme,
        dynamicColor = appState.value.dynamicColor

    ) {
        Surface(
            Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        ) {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Route.AppGraph
            ) {
                navigation<Route.AppGraph>(
                    startDestination = Route.Home
                ) {
                    composable<Route.Home> {
                        val viewModel = koinViewModel<SettingsViewModel>()
                        val state = viewModel.state.collectAsState()
                        SettingsScreen(
                            onBack = {
                                navController.navigateUp()
                            },
                            state = state.value,
                            onAction = viewModel::onAction,
                            onLanguage = {
                                navController.navigate(Route.SettingsLanguage)
                            }
                        )
                    }
                    composable<Route.Settings> {
                        val viewModel = koinViewModel<SettingsViewModel>()
                        val state = viewModel.state.collectAsState()
                        SettingsScreen(
                            onBack = {
                                navController.navigateUp()
                            },
                            state = state.value,
                            onAction = viewModel::onAction,
                            onLanguage = {
                                navController.navigate(Route.SettingsLanguage)
                            }
                        )

                    }

                    composable<Route.SettingsLanguage> {
                        val viewModel = koinViewModel<LanguageViewModel>()
                        val state = viewModel.state.collectAsState()
                        LanguageScreen(
                            onBack = {
                                navController.navigateUp()
                            },
                            state = state.value,
                            onAction = viewModel::onAction,

                            )
                    }
                }
            }
        }
    }
}

@Composable
private inline fun <reified T: ViewModel> NavBackStackEntry.sharedKoinViewModel(
    navController: NavController
): T {
    val navGraphRoute = destination.parent?.route ?: return koinViewModel<T>()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return koinViewModel(
        viewModelStoreOwner = parentEntry
    )
}