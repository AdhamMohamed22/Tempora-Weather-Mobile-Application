package com.example.tempora.data.repository

import com.example.tempora.data.models.CurrentWeather
import com.example.tempora.data.models.FavouriteLocation
import com.example.tempora.data.models.ForecastWeather
import kotlinx.coroutines.flow.Flow

interface IRepository {

    suspend fun getCurrentWeather(lat: Double,lon: Double,appid: String,units: String?,language: String): Flow<CurrentWeather>
    suspend fun getForecastWeather(lat: Double,lon: Double,appid: String,units: String?,language: String): Flow<ForecastWeather>

    suspend fun insertFavouriteLocation(favouriteLocation: FavouriteLocation)
    suspend fun getAllFavouriteLocations(): Flow<List<FavouriteLocation>>
    suspend fun deleteFavouriteLocation(favouriteLocation: FavouriteLocation)
}