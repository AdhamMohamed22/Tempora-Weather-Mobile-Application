package com.example.tempora.composables.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tempora.data.remote.RetrofitHelper
import com.example.tempora.data.remote.WeatherRemoteDataSource
import com.example.tempora.data.repository.Repository

@Composable
fun HomeScreen(){

    val currentWeatherFactory = HomeScreenViewModel.CurrentWeatherFactory(
        Repository.getInstance(
            WeatherRemoteDataSource(RetrofitHelper.retrofit)
        ))
    val viewModel: HomeScreenViewModel = viewModel(factory = currentWeatherFactory)
    viewModel.getCurrentWeather()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(text = "Home Screen")
    }
}