package com.example.tempora.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tempora.composables.alarms.AlarmsScreen
import com.example.tempora.composables.favourites.FavouritesScreen
import com.example.tempora.composables.home.HomeScreen
import com.example.tempora.composables.settings.SettingsScreen

@Composable
fun SetupAppNavigation(navController: NavHostController) {

    NavHost(navController = navController, startDestination = ScreenRoutes.Home.route)
    {
        composable(ScreenRoutes.Home.route) { HomeScreen() }
        composable(ScreenRoutes.Favourites.route) { FavouritesScreen() }
        composable(ScreenRoutes.Alarms.route) { AlarmsScreen() }
        composable(ScreenRoutes.Settings.route) { SettingsScreen() }
    }
}
