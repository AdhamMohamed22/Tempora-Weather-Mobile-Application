package com.example.tempora.data.repository

import com.example.tempora.data.models.CurrentWeather
import kotlinx.coroutines.flow.Flow

interface IRepository {
    suspend fun getCurrentWeather(lat: Double,lon: Double,appid: String): Flow<CurrentWeather>
}