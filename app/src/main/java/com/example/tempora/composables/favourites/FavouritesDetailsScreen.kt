package com.example.tempora.composables.favourites

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tempora.R
import com.example.tempora.composables.home.DisplayHomeScreen
import com.example.tempora.composables.home.HomeScreenViewModel
import com.example.tempora.data.local.WeatherDatabase
import com.example.tempora.data.local.WeatherLocalDataSource
import com.example.tempora.data.remote.RetrofitHelper
import com.example.tempora.data.remote.WeatherRemoteDataSource
import com.example.tempora.data.repository.Repository
import com.example.tempora.data.response_state.CurrentWeatherResponseState
import com.example.tempora.utils.LoadingIndicator


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FavouritesDetailsScreen(showFAB: MutableState<Boolean>, lat: Double, lon: Double) {

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

    LaunchedEffect(Unit) {
        viewModel.message.collect{
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getCurrentWeather(lat,lon,context)
        viewModel.getTodayForecastWeather(lat,lon,context)
        viewModel.get5DaysForecastWeather(lat,lon,context)
    }

    when(currentWeatherState){
        is CurrentWeatherResponseState.Loading -> LoadingIndicator()
        is CurrentWeatherResponseState.Failed -> Text("Failed !")
        is CurrentWeatherResponseState.Success -> DisplayHomeScreen(currentWeather = (currentWeatherState as CurrentWeatherResponseState.Success).currentWeather,viewModel)
    }

}