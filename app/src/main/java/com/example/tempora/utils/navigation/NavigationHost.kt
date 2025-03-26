package com.example.tempora.utils.navigation

import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tempora.composables.alarms.AlarmsScreen
import com.example.tempora.composables.favourites.FavouritesDetailsScreen
import com.example.tempora.composables.favourites.FavouritesScreen
import com.example.tempora.composables.favourites.map.MapScreen
import com.example.tempora.composables.home.HomeScreen
import com.example.tempora.composables.settings.SettingsScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SetupAppNavigation(
    navController: NavHostController,
    location: Location,
    showFAB: MutableState<Boolean>,
    snackBarHostState: SnackbarHostState
) {

    val context = LocalContext.current

    NavHost(navController = navController, startDestination = ScreenRoutes.Home.route)
    {
        composable(ScreenRoutes.Home.route) { HomeScreen(showFAB,location) }
        composable(ScreenRoutes.Favourites.route) { FavouritesScreen(showFAB, snackBarHostState, navigationAction = { favouriteLocation ->  navController.navigate("FavouritesDetailsScreen/${favouriteLocation.latitude}/${favouriteLocation.longitude}") }) }
        composable(ScreenRoutes.Map.route) { MapScreen(showFAB) }
        composable(ScreenRoutes.Alarms.route) { AlarmsScreen(showFAB) }
        composable(ScreenRoutes.Settings.route) { SettingsScreen(showFAB) }

        composable("FavouritesDetailsScreen/{lat}/{lon}") { backStackEntry ->
            val lat = backStackEntry.arguments?.getString("lat")?.toDoubleOrNull() ?: 0.0
            val lon = backStackEntry.arguments?.getString("lon")?.toDoubleOrNull() ?: 0.0
            FavouritesDetailsScreen(showFAB, lat, lon)
        }

    }
}
