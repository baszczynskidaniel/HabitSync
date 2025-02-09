package org.example.project.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import org.example.project.authentication.presentation.log_in.LoginScreen
import org.example.project.authentication.presentation.log_in.LoginViewModel
import org.example.project.authentication.presentation.register.SignUpScreen
import org.example.project.authentication.presentation.register.SignUpViewModel
import org.example.project.core.presentation.ui.AppTheme
import org.example.project.habit.presentation.SelectedHabitViewModel
import org.example.project.habit.presentation.add_category.AddCategoryScreen
import org.example.project.habit.presentation.add_category.AddCategoryViewModel
import org.example.project.habit.presentation.add_habit.AddEditHabitScreen
import org.example.project.habit.presentation.add_habit.AddEditHabitViewModel
import org.example.project.habit.presentation.categories_list.CategoriesListScreen
import org.example.project.habit.presentation.categories_list.CategoriesListViewModel
import org.example.project.habit.presentation.habit_detail.HabitDetailAction
import org.example.project.habit.presentation.habit_detail.HabitDetailScreen
import org.example.project.habit.presentation.habit_detail.HabitDetailViewModel
import org.example.project.habit.presentation.habit_list.HabitListScreen
import org.example.project.habit.presentation.habit_list.HabitListViewModel
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

            LaunchedEffect(appState.value.userId) {
                val isLoggedIn = appState.value.userId.isNotBlank()
                if(isLoggedIn) {
                    navController.navigate(Route.Home)
                } else {
                    navController.navigate(Route.Login)
                }
            }


            NavHost(
                navController = navController,
                startDestination = Route.AppGraph
            ) {
                navigation<Route.AppGraph>(
                    startDestination = Route.Home
                ) {
                    composable<Route.Home> {
                        val viewModel = koinViewModel<HabitListViewModel>()
                        val state = viewModel.state.collectAsState()

                        val selectedHabitListViewModel =
                            it.sharedKoinViewModel<SelectedHabitViewModel>(navController)

                        LaunchedEffect(true) {
                            selectedHabitListViewModel.onSelectedHabitChange(null)
                        }
                        HabitListScreen(
                            state = state.value,
                            onAction = viewModel::onAction,
                            onAddHabit = {
                                navController.navigate(Route.AddHabit)
                            },
                            onHabitDetail = {
                                selectedHabitListViewModel.onSelectedHabitChange(it)
                                navController.navigate(Route.HabitDetail(it.id!!))
                            },
                            onSettings = {
                                navController.navigate(Route.Settings)
                            },
                            onCategories = {
                                navController.navigate(Route.CategoriesList)
                            }
                        )
                    }

                    composable<Route.CategoriesList> {
                        val viewModel = koinViewModel<CategoriesListViewModel>()
                        val state = viewModel.state.collectAsState()

                        CategoriesListScreen(
                            state = state.value,
                            onBack = {
                                navController.navigate(Route.Home) {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = true}
                                }
                            },
                            onAddCategory = {
                                navController.navigate(Route.AddCategory)
                            },
                            onAction = viewModel::onAction,
                            onSettings = {
                                navController.navigate(Route.Settings)
                            }
                        )
                    }

                    composable<Route.AddCategory> {
                        val viewModel = koinViewModel<AddCategoryViewModel>()
                        val state = viewModel.state.collectAsState()

                        AddCategoryScreen(
                            state = state.value,
                            onAction = viewModel::onAction,
                            onBack = {
                                navController.navigate(Route.CategoriesList) {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = true}
                                }
                            }
                        )
                    }

                    composable<Route.Login> {
                        val viewModel = koinViewModel<LoginViewModel>()
                        val state = viewModel.state.collectAsState()

                        LoginScreen(
                            state.value,
                            onAction = viewModel::onAction,
                            onSignUp = {
                                navController.navigate(Route.SignUp) {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = true}
                                }
                            }
                        )
                    }

                    composable<Route.SignUp> {
                        val viewModel = koinViewModel<SignUpViewModel>()
                        val state = viewModel.state.collectAsState()

                        SignUpScreen(
                            state.value,
                            onAction = viewModel::onAction,
                            onLogin = {
                                navController.navigate(Route.Login) {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = true}
                                }
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
                    composable<Route.HabitDetail> {
                        val selectedHabitViewModel =
                            it.sharedKoinViewModel<SelectedHabitViewModel>(navController)
                        val viewModel = koinViewModel<HabitDetailViewModel>()
                        val selectedHabit by selectedHabitViewModel.selectedHabit.collectAsStateWithLifecycle()
                        val state = viewModel.state.collectAsState()
                        LaunchedEffect(selectedHabit) {
                            selectedHabit?.let {
                                viewModel.onAction(HabitDetailAction.OnSelectedHabitChange(it))
                            }
                        }
                        HabitDetailScreen(
                            state = state.value,
                            onBack = {

                                navController.navigate(Route.Home) {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = true}
                                }
                            },
                            onEdit = {},
                            onAction = viewModel::onAction

                        )
                    }

                    composable<Route.AddHabit> {
                        val viewModel = koinViewModel<AddEditHabitViewModel>()
                        val state = viewModel.state.collectAsState()
                        AddEditHabitScreen(
                            onBack = {
                                navController.navigate(Route.Home) {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = true}
                                }
                            },
                            state = state.value,
                            onAction = viewModel::onAction,
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