package com.example.tempora.data.local

import com.example.tempora.data.models.Alarm
import com.example.tempora.data.models.CashedWeather
import com.example.tempora.data.models.CurrentWeather
import com.example.tempora.data.models.FavouriteLocation
import com.example.tempora.data.models.ForecastWeather
import kotlinx.coroutines.flow.Flow

interface IWeatherLocalDataSource {

    suspend fun insertCashedWeather(cashedWeather: CashedWeather)
    suspend fun getCashedWeather(): Flow<CashedWeather>

    suspend fun insertFavouriteLocation(favouriteLocation: FavouriteLocation)
    suspend fun getAllFavouriteLocations(): Flow<List<FavouriteLocation>>
    suspend fun deleteFavouriteLocation(favouriteLocation: FavouriteLocation)

    suspend fun insertAlarm(alarm: Alarm)
    suspend fun getAllAlarms(): Flow<List<Alarm>>
    suspend fun deleteAlarm(alarm: Alarm)

}