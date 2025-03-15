package com.example.tempora

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun SetupAppNavigation() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "splashScreen") {
        composable("splashScreen") { SplashScreen(navController) }
        composable("mainScreen") { MainScreen() }
    }
}
