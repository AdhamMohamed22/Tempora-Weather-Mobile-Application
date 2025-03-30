package com.example.tempora.composables.home

import android.location.Location
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tempora.R
import com.example.tempora.composables.home.components.DisplayCurrentWeather
import com.example.tempora.composables.home.components.NetworkCutOffAnimation
import com.example.tempora.composables.home.components.ListOf5WeekDaysCards
import com.example.tempora.composables.home.components.ListOfHourCards
import com.example.tempora.composables.settings.utils.SharedPref
import com.example.tempora.data.local.WeatherDatabase
import com.example.tempora.data.local.WeatherLocalDataSource
import com.example.tempora.data.remote.RetrofitHelper
import com.example.tempora.data.remote.WeatherRemoteDataSource
import com.example.tempora.data.repository.Repository
import com.example.tempora.data.response_state.CurrentWeatherResponseState
import com.example.tempora.data.response_state.ForecastWeatherResponseState
import com.example.tempora.composables.home.components.LoadingIndicator


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(showFAB: MutableState<Boolean>, location: Location){

    showFAB.value = false
    val context = LocalContext.current

    val homeScreenViewModelFactory = HomeScreenViewModel.HomeScreenViewModelFactory(
        Repository.getInstance(
            WeatherRemoteDataSource(RetrofitHelper.retrofit),
            WeatherLocalDataSource(WeatherDatabase.getInstance(context).getWeatherDao())
        )
    )
    val viewModel: HomeScreenViewModel = viewModel(factory = homeScreenViewModelFactory)

    val currentWeatherState by viewModel.currentWeather.collectAsStateWithLifecycle()
    val todayForecastWeatherState by viewModel.todayForecastWeather.collectAsStateWithLifecycle()
    val daysForecastWeather by viewModel.daysForecastWeather.collectAsStateWithLifecycle()
    val selectedUnit by viewModel.selectedUnit.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.message.collect{
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        val sharedPref = SharedPref.getInstance(context)
        if(sharedPref.getGpsSelected()){
            viewModel.getCurrentWeather(location.latitude,location.longitude,context)
            viewModel.getTodayForecastWeather(location.latitude,location.longitude,context)
            viewModel.get5DaysForecastWeather(location.latitude,location.longitude,context)
        }
        else {
            viewModel.getCurrentWeather(sharedPref.getLatitude(),sharedPref.getLongitude(),context)
            viewModel.getTodayForecastWeather(sharedPref.getLatitude(),sharedPref.getLongitude(),context)
            viewModel.get5DaysForecastWeather(sharedPref.getLatitude(),sharedPref.getLongitude(),context)
        }
    }

    val isFailed = currentWeatherState is CurrentWeatherResponseState.Failed || todayForecastWeatherState is ForecastWeatherResponseState.Failed || daysForecastWeather is ForecastWeatherResponseState.Failed

    Box(modifier = Modifier.fillMaxSize()) {
        if (isFailed) {
            NetworkCutOffAnimation()
        } else {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = "HomeScreen Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(alpha = 0.8f)
            )

            // Foreground UI elements
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (currentWeatherState) {
                    is CurrentWeatherResponseState.Loading -> LoadingIndicator()
                    is CurrentWeatherResponseState.Failed -> Text("")
                    is CurrentWeatherResponseState.Success -> DisplayCurrentWeather(
                        currentWeather = (currentWeatherState as CurrentWeatherResponseState.Success).currentWeather,
                        selectedUnit
                    )
                }

                when (todayForecastWeatherState) {
                    is ForecastWeatherResponseState.Loading -> LoadingIndicator()
                    is ForecastWeatherResponseState.Failed -> Text("")
                    is ForecastWeatherResponseState.Success -> ListOfHourCards(
                        todayForecast = (todayForecastWeatherState as ForecastWeatherResponseState.Success).forecastWeather,
                        selectedUnit
                    )
                }

                when (daysForecastWeather) {
                    is ForecastWeatherResponseState.Loading -> LoadingIndicator()
                    is ForecastWeatherResponseState.Failed -> Text("")
                    is ForecastWeatherResponseState.Success -> ListOf5WeekDaysCards(
                        fiveDaysList = (daysForecastWeather as ForecastWeatherResponseState.Success).forecastWeather,
                        selectedUnit
                    )
                }
            }
        }
    }

}


