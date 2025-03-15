package com.example.tempora.data.remote

import com.example.tempora.data.models.CurrentWeather

interface IWeatherRemoteDataSource {
    suspend fun getCurrentWeather(): CurrentWeather
}