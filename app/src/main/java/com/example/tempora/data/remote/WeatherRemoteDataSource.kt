package com.example.tempora.data.remote

import android.util.Log
import com.example.tempora.data.models.CurrentWeather
import com.example.tempora.data.models.ForecastWeather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class WeatherRemoteDataSource(private val apiDataService: ApiDataService) : IWeatherRemoteDataSource {

    override suspend fun getCurrentWeather(lat: Double,lon: Double,appid: String,units: String?,language: String): Flow<CurrentWeather> {
        return flowOf(apiDataService.getCurrentWeather(lat,lon,appid,units,language).body()!!)
    }

    override suspend fun getForecastWeather(lat: Double, lon: Double, appid: String,units: String?,language: String): Flow<ForecastWeather> {
        return flowOf(apiDataService.getForecastWeather(lat,lon,appid,units,language).body()!!)
    }

}