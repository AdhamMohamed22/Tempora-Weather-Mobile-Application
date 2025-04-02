package com.example.tempora.data.remote

import com.example.tempora.data.models.CurrentWeather
import com.example.tempora.data.models.ForecastWeather
import kotlinx.coroutines.flow.Flow

interface IWeatherRemoteDataSource {
    suspend fun getCurrentWeather(lat: Double,lon: Double,appid: String,units: String?,language: String): Flow<CurrentWeather>
    suspend fun getForecastWeather(lat: Double,lon: Double,appid: String,units: String?,language: String): Flow<ForecastWeather>
}