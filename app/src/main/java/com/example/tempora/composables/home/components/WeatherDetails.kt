package com.example.tempora.composables.home.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tempora.R
import com.example.tempora.utils.helpers.formatNumberBasedOnLanguage
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherDetails(
    cloud: Int,
    humidity: Int,
    windSpeed: String,
    pressure: String,
    date: Long
) {
    // Convert to Instant
    val instant = Instant.ofEpochSecond(date)
    // Format the date and time in Africa/Cairo
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy - HH:mm:ss")
        .withZone(ZoneId.of("Africa/Cairo"))
    val formattedTime = formatter.format(instant)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = formatNumberBasedOnLanguage(formattedTime),
            color = colorResource(R.color.white),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                WeatherDetailItem(
                    icon = R.drawable.clouds,
                    label = stringResource(R.string.cloud),
                    value = formatNumberBasedOnLanguage("$cloud%")
                )
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(
                    modifier = Modifier
                        .width(160.dp)
                        .padding(horizontal = 8.dp),
                    thickness = 1.dp,
                    color = Color.White.copy(alpha = 0.4f)
                )
                Spacer(modifier = Modifier.height(12.dp))
                WeatherDetailItem(
                    icon = R.drawable.humidity,
                    label = stringResource(R.string.humidity),
                    value = formatNumberBasedOnLanguage("$humidity%")
                )
            }

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(100.dp)
                    .background(Color.White.copy(alpha = 0.4f))
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                WeatherDetailItem(
                    icon = R.drawable.wind,
                    label = stringResource(R.string.wind),
                    value = formatNumberBasedOnLanguage(windSpeed)
                )
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(
                    modifier = Modifier
                        .width(160.dp)
                        .padding(horizontal = 8.dp),
                    thickness = 1.dp,
                    color = Color.White.copy(alpha = 0.4f)
                )
                Spacer(modifier = Modifier.height(12.dp))
                WeatherDetailItem(
                    icon = R.drawable.pressure,
                    label = stringResource(R.string.pressure),
                    value = formatNumberBasedOnLanguage(pressure)
                )
            }
        }
    }
}

@Composable
fun WeatherDetailItem(icon: Int, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = label,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Column(
            modifier = Modifier.width(80.dp), // Ensuring consistent text width
            horizontalAlignment = Alignment.CenterHorizontally // Align text to the start)
        ) {
            Text(
                text = label,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = value,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
