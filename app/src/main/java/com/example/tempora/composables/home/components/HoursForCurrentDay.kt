package com.example.tempora.composables.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.tempora.R
import com.example.tempora.utils.helpers.formatNumberBasedOnLanguage
import com.example.tempora.utils.helpers.formatTemperatureUnitBasedOnLanguage
import com.example.tempora.data.models.Item0

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HourCard(todayForecast: Item0, selectedUnit: String) {
    Card(
        modifier = Modifier
            .wrapContentSize(Alignment.Center)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.secondaryColor).copy(alpha = 0.75f),
        )
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .wrapContentSize(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = formatNumberBasedOnLanguage(todayForecast.dt_txt.substringAfter(" ")),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )

            GlideImage(
                model = "https://openweathermap.org/img/wn/${todayForecast.weather[0].icon}@2x.png",
                contentDescription = "",
                modifier = Modifier.size(75.dp)
            )

            Text(
                text = formatNumberBasedOnLanguage(
                    "${todayForecast.main.temp} ${
                        formatTemperatureUnitBasedOnLanguage(
                            selectedUnit
                        )
                    }"
                ), fontWeight = FontWeight.Medium, fontSize = 16.sp
            )
        }
    }
}


@Composable
fun ListOfHourCards(todayForecast: List<Item0>, selectedUnit: String) {
    LazyRow(
        modifier = Modifier.wrapContentSize(Alignment.Center),
    )
    {
        items(todayForecast.size)
        {
            HourCard(todayForecast[it], selectedUnit)
        }
    }
    Text(
        text = stringResource(R.string.upcoming_5_days_temperature),
        style = MaterialTheme.typography.titleMedium,
        color = colorResource(R.color.white),
        fontWeight = FontWeight.Bold
    )
}

