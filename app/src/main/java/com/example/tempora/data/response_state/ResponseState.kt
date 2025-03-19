package com.example.tempora.data.response_state

import com.example.tempora.data.models.CurrentWeather
import com.example.tempora.data.models.ForecastWeather
import com.example.tempora.data.models.Item0

sealed class CurrentWeatherResponseState {
    data object Loading: CurrentWeatherResponseState()
    data class Success(val currentWeather: CurrentWeather): CurrentWeatherResponseState()
    data class Failed(val error: Throwable): CurrentWeatherResponseState()
}

sealed class ForecastWeatherResponseState {
    data object Loading: ForecastWeatherResponseState()
    data class Success(val forecastWeather: List<Item0>): ForecastWeatherResponseState()
    data class Failed(val error: Throwable): ForecastWeatherResponseState()
}