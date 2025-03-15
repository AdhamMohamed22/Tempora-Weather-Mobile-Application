package com.example.tempora

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.example.tempora.R // Replace with your actual package name

@Composable
fun SplashScreen(navController: NavController) {
    // Load the Lottie animation
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.tempora_splash))

    // Control animation progress
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = 3,
        speed = 1f
    )

    // Display the animation
    Box(
        modifier = Modifier.fillMaxSize().background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = Modifier.fillMaxSize()
        )
    }

    // Navigate when animation completes
    LaunchedEffect(progress) {
        if (progress == 1f) {
            navController.navigate("mainScreen") {
                popUpTo("splashScreen") { inclusive = true } // Remove splash from backstack
            }
        }
    }

}
