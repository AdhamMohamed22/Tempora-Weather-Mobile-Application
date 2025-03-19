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
fun WeatherStatusIcon() {
    Image(
        painter = painterResource(id = R.drawable.weather_status),
        contentDescription = "Icon Weather Status",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(70.dp)
            .clip(CircleShape)
    )
}