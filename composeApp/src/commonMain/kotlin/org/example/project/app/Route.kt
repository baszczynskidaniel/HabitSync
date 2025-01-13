package org.example.project.app

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object Home: Route

    @Serializable
    data object Settings: Route

    @Serializable
    data object AppGraph: Route

    @Serializable
    data object SettingsLanguage: Route
}