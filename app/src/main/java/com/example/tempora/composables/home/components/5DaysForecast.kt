package com.example.tempora.composables.home.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.tempora.R
import com.example.tempora.data.models.Item0
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun WeekDayCard(dayForecast: Item0) {

    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val outputFormatter = DateTimeFormatter.ofPattern("EEEE", Locale.getDefault()) // "EEEE" gives full weekday name
    val dateTime = LocalDateTime.parse(dayForecast.dt_txt, inputFormatter)
    val weekDay = dateTime.format(outputFormatter)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 5.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.secondaryColor).copy(alpha = 0.75f),
        )
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = weekDay, fontWeight = FontWeight.Medium, fontSize = 16.sp)

            GlideImage(
                model = "https://openweathermap.org/img/wn/${dayForecast.weather[0].icon}@2x.png", contentDescription = "",
                modifier = Modifier.size(75.dp)
            )

            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Temp: ${dayForecast.main.temp}", fontWeight = FontWeight.Medium, fontSize = 15.sp)

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(text = "min: ${dayForecast.main.temp_min}  |", fontWeight = FontWeight.Normal, fontSize = 15.sp)
                    Spacer(Modifier.width(5.dp))
                    Text(text = "max: ${dayForecast.main.temp_max}", fontWeight = FontWeight.Normal, fontSize = 15.sp)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListOf5WeekDaysCards(fiveDaysList: List<Item0>) {
    LazyColumn(
        modifier = Modifier.wrapContentSize(Alignment.Center),
    )
    {
        items(fiveDaysList.size)
        {
            WeekDayCard(fiveDaysList[it])
        }
    }
}