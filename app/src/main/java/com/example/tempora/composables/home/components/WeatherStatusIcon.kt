package com.example.tempora.composables.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.tempora.R

@Composable
fun WeatherStatusIcon(weatherIconCode: String) {
    val image = when (weatherIconCode) {
        "01d", "01n" -> R.drawable.clearsky // Clear sky
        "02d", "02n" -> R.drawable.fewclouds // Few clouds
        "03d", "03n" -> R.drawable.scatteredclouds // Scattered clouds
        "04d", "04n" -> R.drawable.brokenclouds // Broken clouds
        "09d", "09n" -> R.drawable.rain // Shower rain
        "10d", "10n" -> R.drawable.rain // Rain (day & night)
        "11d", "11n" -> R.drawable.thunderstorm // Thunderstorm
        "13d", "13n" -> R.drawable.snow // Snow
        "50d", "50n" -> R.drawable.haze // Mist, fog, haze
        else -> R.drawable.weather_status // Default icon
    }

    Image(
        painter = painterResource(id = image),
        contentDescription = "Weather Status Icon",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(70.dp)
            .clip(CircleShape)
    )
}
