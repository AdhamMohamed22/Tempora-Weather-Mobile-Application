package com.example.tempora.composables.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tempora.R

@Preview
@Composable
fun WeatherStatusIconPreview() {
    WeatherStatusIcon(weatherDescription = "clear sky")
}

@Composable
fun WeatherStatusIcon(weatherDescription: String) {
    val image = when (weatherDescription.lowercase().trim()) {
        "clear sky", "clear" -> R.drawable.clearsky
        "few clouds" -> R.drawable.fewclouds
        "scattered clouds" -> R.drawable.scatteredclouds
        "broken clouds" -> R.drawable.brokenclouds
        "overcast clouds" -> R.drawable.overcastclouds
        "shower rain", "light rain", "moderate rain", "heavy rain", "very heavy rain", "extreme rain" , "freezing rain", "rain" -> R.drawable.rain
        "light snow", "heavy snow", "blizzard" , "snow" -> R.drawable.snow
        "thunderstorm", "thunderstorm with rain", "thunderstorm with snow" -> R.drawable.thunderstorm
        "mist", "fog", "haze", "smoke" -> R.drawable.haze
        "dust", "sand" -> R.drawable.dust
        "tornado", "squalls" -> R.drawable.tornado
        else -> R.drawable.weather_status
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
