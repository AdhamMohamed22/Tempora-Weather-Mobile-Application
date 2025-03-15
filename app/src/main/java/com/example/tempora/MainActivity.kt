package com.example.tempora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.tempora.currentweather.CurrentWeatherViewModel
import com.example.tempora.data.remote.RetrofitHelper
import com.example.tempora.data.remote.WeatherRemoteDataSource
import com.example.tempora.data.repository.Repository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            SetupAppNavigation()

            val currentWeatherFactory = CurrentWeatherViewModel.CurrentWeatherFactory(
                Repository.getInstance(
                    WeatherRemoteDataSource(RetrofitHelper.retrofit)
                ))
            val viewModel = ViewModelProvider(this,currentWeatherFactory)[CurrentWeatherViewModel::class.java]

            viewModel.getCurrentWeather()

            //AllProductsScreen(viewModel)

        }
    }
}

@Composable
fun MainScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Welcome to the Main Screen!")
    }
}