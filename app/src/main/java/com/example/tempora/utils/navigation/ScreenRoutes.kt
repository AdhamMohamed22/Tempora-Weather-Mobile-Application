package com.example.tempora.utils.navigation

import com.example.tempora.data.models.FavouriteLocation
import com.example.tempora.utils.converter.FavouriteLocationSerializer
import kotlinx.serialization.Serializable

@Serializable
sealed class ScreenRoutes(val route: String){
    @Serializable
    object Home: ScreenRoutes(route = "HomeScreen")
    @Serializable
    object Favourites: ScreenRoutes(route = "FavouritesScreen")

    @Serializable
    object Alarms: ScreenRoutes(route = "AlarmsScreen")
    @Serializable
    object Settings: ScreenRoutes(route = "SettingsScreen")


    @Serializable
    data class FavouritesDetails(
        @Serializable(with = FavouriteLocationSerializer::class)
        val favouriteLocation: FavouriteLocation
    ) : ScreenRoutes(route = "FavouritesDetailsScreen/{favouriteLocation}")

    @Serializable
    data class Map(val isFavouritesScreen: Boolean): ScreenRoutes(route = "MapScreen/{isFavouritesScreen}")
}