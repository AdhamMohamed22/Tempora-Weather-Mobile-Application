package com.example.tempora.composables.home.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tempora.R
import com.example.tempora.data.models.CurrentWeather

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DisplayCurrentWeather(currentWeather: CurrentWeather, selectedUnit: String){
    Spacer(modifier = Modifier.height(24.dp))
    Logo()
    WeatherStatusIcon(currentWeather.weather[0].icon)
    TemperatureDegree(currentWeather.main.temp.toString(),selectedUnit)
    WeatherDescription(currentWeather.weather[0].description)
    Spacer(modifier = Modifier.height(8.dp))
    City(currentWeather.name,currentWeather.sys.country)
    Spacer(modifier = Modifier.height(24.dp))
    WeatherDetails(currentWeather.clouds.all, currentWeather.main.humidity, currentWeather.wind.speed.toString(), currentWeather.main.pressure.toString(), currentWeather.dt.toLong())
    Spacer(modifier = Modifier.height(8.dp))
    Text(text = stringResource(R.string.today_s_hourly_temperature), style = MaterialTheme.typography.titleMedium, color = colorResource(R.color.white), fontWeight = FontWeight.Bold)
}