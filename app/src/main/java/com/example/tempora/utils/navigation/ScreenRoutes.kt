package com.example.tempora.utils.navigation

import kotlinx.serialization.Serializable


sealed class ScreenRoutes(val route: String){
    @Serializable
    object Home: ScreenRoutes(route = "HomeScreen")
    @Serializable
    object Favourites: ScreenRoutes(route = "FavouritesScreen")
    @Serializable
    object Map: ScreenRoutes(route = "MapScreen")
    @Serializable
    object Alarms: ScreenRoutes(route = "AlarmsScreen")
    @Serializable
    object Settings: ScreenRoutes(route = "SettingsScreen")
}