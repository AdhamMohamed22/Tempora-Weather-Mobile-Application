package com.example.tempora.utils

import kotlinx.serialization.Serializable


sealed class ScreenRoutes(val route: String){
    @Serializable
    object Home: ScreenRoutes(route = "HomeScreen")
    @Serializable
    object Favourites: ScreenRoutes(route = "FavouritesScreen")
    @Serializable
    object Alarms: ScreenRoutes(route = "AlarmsScreen")
    @Serializable
    object Settings: ScreenRoutes(route = "SettingsScreen")
}