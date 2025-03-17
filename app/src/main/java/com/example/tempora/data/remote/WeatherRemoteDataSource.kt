package com.example.tempora.data.remote

import com.example.tempora.data.models.CurrentWeather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class WeatherRemoteDataSource(private val apiDataService: ApiDataService) : IWeatherRemoteDataSource {
    override suspend fun getCurrentWeather(lat: Double,lon: Double,appid: String): Flow<CurrentWeather> {
        return flowOf(apiDataService.getCurrentWeather(lat,lon,appid).body()!!)
    }
}