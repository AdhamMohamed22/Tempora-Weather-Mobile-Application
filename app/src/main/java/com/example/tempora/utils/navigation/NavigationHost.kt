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
import com.example.tempora.composables.favourites.favouritesdetails.FavouritesDetailsScreen
import com.example.tempora.composables.favourites.FavouritesScreen
import com.example.tempora.composables.favourites.map.MapScreen
import com.example.tempora.composables.home.HomeScreen
import com.example.tempora.composables.settings.SettingsScreen
import com.example.tempora.data.models.FavouriteLocation
import com.google.gson.Gson
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

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
        composable(ScreenRoutes.Favourites.route) {
            FavouritesScreen(
                showFAB,
                snackBarHostState,
                navigationAction = { favouriteLocation ->
                    val json = Gson().toJson(favouriteLocation)
                    val encodedJson = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
                    navController.navigate("FavouritesDetailsScreen?favouriteLocation=$encodedJson")
                }
            )
        }

        composable(ScreenRoutes.Alarms.route) { AlarmsScreen(showFAB) }
        composable(ScreenRoutes.Settings.route) { SettingsScreen(showFAB, navigationAction = { navController.navigate("MapScreen/${false}")}) }

        composable("FavouritesDetailsScreen?favouriteLocation={favouriteLocation}") { backStackEntry ->
            val json = backStackEntry.arguments?.getString("favouriteLocation")
            val decodedJson = json?.let { URLDecoder.decode(it, StandardCharsets.UTF_8.toString()) }
            val favouriteLocation = decodedJson?.let { Gson().fromJson(it, FavouriteLocation::class.java) }

            if (favouriteLocation != null) {
                FavouritesDetailsScreen(showFAB, favouriteLocation)
            }
        }

        composable("MapScreen/{isFavouritesScreen}") { backStackEntry ->
            val isFavouritesScreen = backStackEntry.arguments?.getString("isFavouritesScreen")?.toBooleanStrictOrNull() ?: false
            MapScreen(showFAB,isFavouritesScreen)
        }
    }
}
