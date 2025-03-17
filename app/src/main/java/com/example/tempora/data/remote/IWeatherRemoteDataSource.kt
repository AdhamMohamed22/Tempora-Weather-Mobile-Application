package com.example.tempora.data.remote

import com.example.tempora.data.models.CurrentWeather
import kotlinx.coroutines.flow.Flow

interface IWeatherRemoteDataSource {
    suspend fun getCurrentWeather(lat: Double,lon: Double,appid: String): Flow<CurrentWeather>
}