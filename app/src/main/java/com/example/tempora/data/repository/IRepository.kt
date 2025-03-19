package com.example.tempora.data.repository

import com.example.tempora.data.models.CurrentWeather
import com.example.tempora.data.models.ForecastWeather
import kotlinx.coroutines.flow.Flow

interface IRepository {
    suspend fun getCurrentWeather(lat: Double,lon: Double,appid: String): Flow<CurrentWeather>

    suspend fun getForecastWeather(lat: Double,lon: Double,appid: String): Flow<ForecastWeather>
}