package com.example.tempora.utils

import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tempora.composables.alarms.AlarmsScreen
import com.example.tempora.composables.favourites.FavouritesScreen
import com.example.tempora.composables.favourites.MapScreen
import com.example.tempora.composables.home.HomeScreen
import com.example.tempora.composables.settings.SettingsScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SetupAppNavigation(navController: NavHostController,location: Location) {

    val context = LocalContext.current

    NavHost(navController = navController, startDestination = ScreenRoutes.Home.route)
    {
        composable(ScreenRoutes.Home.route) { HomeScreen(location) }
        composable(ScreenRoutes.Favourites.route) { FavouritesScreen(onNavigationToMap = { context -> navController.navigate(ScreenRoutes.Map(context).route) }) }
        composable(ScreenRoutes.Map(context).route) { MapScreen(context) }
        composable(ScreenRoutes.Alarms.route) { AlarmsScreen() }
        composable(ScreenRoutes.Settings.route) { SettingsScreen() }
    }
}
