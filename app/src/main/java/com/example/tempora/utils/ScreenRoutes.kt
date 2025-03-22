package com.example.tempora.utils

import android.content.Context
import kotlinx.serialization.Serializable


sealed class ScreenRoutes(val route: String){
    @Serializable
    object Home: ScreenRoutes(route = "HomeScreen")
    @Serializable
    object Favourites: ScreenRoutes(route = "FavouritesScreen")
    @Serializable
    data class Map(var context: Context): ScreenRoutes(route = "MapScreen")
    @Serializable
    object Alarms: ScreenRoutes(route = "AlarmsScreen")
    @Serializable
    object Settings: ScreenRoutes(route = "SettingsScreen")
}