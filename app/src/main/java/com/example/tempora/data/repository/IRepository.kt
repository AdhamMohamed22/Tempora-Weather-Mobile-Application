package com.example.tempora.data.repository

import com.example.tempora.data.models.CurrentWeather

interface IRepository {
    suspend fun getCurrentWeather():  CurrentWeather
}