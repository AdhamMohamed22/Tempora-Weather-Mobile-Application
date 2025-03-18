package com.example.tempora.composables.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.tempora.R
import com.example.tempora.data.models.CurrentWeather
import com.example.tempora.data.models.ForecastWeather
import com.example.tempora.data.models.Item0

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HourCard(todayForecast: Item0) {
    Card(
        modifier = Modifier
            .wrapContentSize(Alignment.Center)
            .padding(8.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.secondaryColor).copy(alpha = 0.75f),
        )
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .wrapContentSize(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = todayForecast.dt_txt.substringAfter(" "), fontWeight = FontWeight.Medium, fontSize = 16.sp)

            GlideImage(
                model = "https://openweathermap.org/img/wn/${todayForecast.weather[0].icon}@2x.png", contentDescription = "",
                modifier = Modifier.size(75.dp)
            )

            Text(text = todayForecast.main.temp.toString(), fontWeight = FontWeight.Medium, fontSize = 16.sp)
        }
    }
}


@Composable
fun ListOfHourCards(todayForecast: List<Item0>) {
    LazyRow(
        modifier = Modifier.wrapContentSize(Alignment.Center),
    )
    {
        items(todayForecast.size)
        {
            HourCard(todayForecast[it])
        }
    }
}

