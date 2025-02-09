package org.example.project.app

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object AddHabit: Route

    @Serializable
    data object Login: Route

    @Serializable
    data object AddCategory: Route

    @Serializable
    data object CategoriesList: Route

    @Serializable
    data object SignUp: Route

    @Serializable
    data class HabitDetail(val habitId: String): Route

    @Serializable
    data object Home: Route

    @Serializable
    data object Settings: Route

    @Serializable
    data object AppGraph: Route

    @Serializable
    data object SettingsLanguage: Route
}