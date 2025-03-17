package com.example.tempora.composables.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tempora.R
import com.example.tempora.composables.home.components.City
import com.example.tempora.composables.home.components.IconWeatherStatus
import com.example.tempora.composables.home.components.Logo
import com.example.tempora.composables.home.components.TemperatureDegree
import com.example.tempora.composables.home.components.WeatherDescription
import com.example.tempora.composables.home.components.WeatherDetails
import com.example.tempora.data.remote.RetrofitHelper
import com.example.tempora.data.remote.WeatherRemoteDataSource
import com.example.tempora.data.repository.Repository


@Preview
@Composable
fun HomeScreen(){

    val currentWeatherFactory = HomeScreenViewModel.CurrentWeatherFactory(
        Repository.getInstance(
            WeatherRemoteDataSource(RetrofitHelper.retrofit)
        ))
    val viewModel: HomeScreenViewModel = viewModel(factory = currentWeatherFactory)
    viewModel.getCurrentWeather()

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
            Logo()
            Spacer(modifier = Modifier.height(8.dp))
            IconWeatherStatus()
            TemperatureDegree()
            WeatherDescription()
            Spacer(modifier = Modifier.height(8.dp))
            City()
            Spacer(modifier = Modifier.height(24.dp))
            WeatherDetails(40, 45, "2.2", "1000", "Mon, Mar 17  17:00")
        }
    }
}