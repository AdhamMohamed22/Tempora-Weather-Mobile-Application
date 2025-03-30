package com.example.tempora.data.response_state

import com.example.tempora.data.models.Alarm
import com.example.tempora.data.models.CurrentWeather
import com.example.tempora.data.models.FavouriteLocation
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

sealed class FavouriteLocationsResponseState {
    data object Loading: FavouriteLocationsResponseState()
    data class Success(val favouriteLocations: List<FavouriteLocation> ): FavouriteLocationsResponseState()
    data class Failed(val error: Throwable): FavouriteLocationsResponseState()
}

sealed class AlarmsResponseState {
    data object Loading: AlarmsResponseState()
    data class Success(val alarms: List<Alarm> ): AlarmsResponseState()
    data class Failed(val error: Throwable): AlarmsResponseState()
}