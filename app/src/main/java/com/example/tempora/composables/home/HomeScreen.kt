package com.example.tempora.composables.home

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tempora.R
import com.example.tempora.composables.home.components.City
import com.example.tempora.composables.home.components.IconWeatherStatus
import com.example.tempora.composables.home.components.ListOfHourCards
import com.example.tempora.composables.home.components.Logo
import com.example.tempora.composables.home.components.TemperatureDegree
import com.example.tempora.composables.home.components.WeatherDescription
import com.example.tempora.composables.home.components.WeatherDetails
import com.example.tempora.data.models.CurrentWeather
import com.example.tempora.data.remote.RetrofitHelper
import com.example.tempora.data.remote.WeatherRemoteDataSource
import com.example.tempora.data.repository.Repository
import com.example.tempora.data.response_state.CurrentWeatherResponseState
import com.example.tempora.data.response_state.ForecastWeatherResponseState


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun HomeScreen(){

    val currentWeatherFactory = HomeScreenViewModel.CurrentWeatherFactory(
        Repository.getInstance(
            WeatherRemoteDataSource(RetrofitHelper.retrofit)
        ))
    val viewModel: HomeScreenViewModel = viewModel(factory = currentWeatherFactory)

    val context = LocalContext.current

    val currentWeatherState by viewModel.currentWeather.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.message.collect{
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getCurrentWeather()
        viewModel.getForecastWeather()
    }

    when(currentWeatherState){
        is CurrentWeatherResponseState.Loading -> LoadingIndicator()
        is CurrentWeatherResponseState.Failed -> Text("Failed !")
        is CurrentWeatherResponseState.Success -> DisplayHomeScreen(currentWeather = (currentWeatherState as CurrentWeatherResponseState.Success).currentWeather,viewModel)
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DisplayHomeScreen(currentWeather: CurrentWeather,viewModel: HomeScreenViewModel){
    val scope = rememberCoroutineScope()

    val forecastWeatherState by viewModel.forecastWeather.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize())
    {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "HomeScreen Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().graphicsLayer(alpha = 0.8f)
        )
        // Foreground UI elements
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Logo()
            Spacer(modifier = Modifier.height(8.dp))
            IconWeatherStatus()
            TemperatureDegree(currentWeather.main.temp.toString())
            WeatherDescription(currentWeather.weather[0].description)
            Spacer(modifier = Modifier.height(8.dp))
            City(currentWeather.name,currentWeather.sys.country)
            Spacer(modifier = Modifier.height(24.dp))
            WeatherDetails(currentWeather.clouds.all, currentWeather.main.humidity, currentWeather.wind.speed.toString(), currentWeather.main.pressure.toString(), currentWeather.dt.toLong())

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Today's Hourly Temperature", style = MaterialTheme.typography.titleMedium, color = colorResource(R.color.white), fontWeight = FontWeight.Bold)
            when(forecastWeatherState){
                is ForecastWeatherResponseState.Loading -> LoadingIndicator()
                is ForecastWeatherResponseState.Failed -> Text("Failed !")
                is ForecastWeatherResponseState.Success -> ListOfHourCards(todayForecast = (forecastWeatherState as ForecastWeatherResponseState.Success).forecastWeather)
            }
        }
    }
}

@Composable
fun LoadingIndicator(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize()
    ) {
        CircularProgressIndicator()
    }
}