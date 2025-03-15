package com.example.tempora.data.remote

import com.example.tempora.data.models.CurrentWeather

class WeatherRemoteDataSource(private val apiDataService: ApiDataService) : IWeatherRemoteDataSource {
    override suspend fun getCurrentWeather(): CurrentWeather {
        return apiDataService.getCurrentWeather().body()!!
    }
}