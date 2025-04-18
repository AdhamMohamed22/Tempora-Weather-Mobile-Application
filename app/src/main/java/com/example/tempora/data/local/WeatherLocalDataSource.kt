package com.example.tempora.data.local

import com.example.tempora.data.models.Alarm
import com.example.tempora.data.models.CashedWeather
import com.example.tempora.data.models.CurrentWeather
import com.example.tempora.data.models.FavouriteLocation
import com.example.tempora.data.models.ForecastWeather
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSource(private val weatherDao: WeatherDao): IWeatherLocalDataSource {

    override suspend fun insertCashedWeather(cashedWeather: CashedWeather) {
        weatherDao.insertCashedWeather(cashedWeather)
    }

    override suspend fun getCashedWeather(): Flow<CashedWeather> {
        return weatherDao.getCashedWeather()
    }

    override suspend fun insertFavouriteLocation(favouriteLocation: FavouriteLocation) {
        weatherDao.insertFavouriteLocation(favouriteLocation)
    }

    override suspend fun getAllFavouriteLocations(): Flow<List<FavouriteLocation>> {
        return weatherDao.getAllFavouriteLocations()
    }

    override suspend fun deleteFavouriteLocation(favouriteLocation: FavouriteLocation) {
        weatherDao.deleteFavouriteLocation(favouriteLocation)
    }

    override suspend fun insertAlarm(alarm: Alarm) {
        weatherDao.insertAlarm(alarm)
    }

    override suspend fun getAllAlarms(): Flow<List<Alarm>> {
        return weatherDao.getAllAlarms()
    }

    override suspend fun deleteAlarm(alarm: Alarm) {
        weatherDao.deleteAlarm(alarm)
    }

}