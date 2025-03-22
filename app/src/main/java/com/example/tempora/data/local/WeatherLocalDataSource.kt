package com.example.tempora.data.local

import com.example.tempora.data.models.FavouriteLocation

class WeatherLocalDataSource(private val weatherDao: WeatherDao): IWeatherLocalDataSource {
    override suspend fun insertFavouriteLocation(favouriteLocation: FavouriteLocation) {
        weatherDao.insertFavouriteLocation(favouriteLocation)
    }
}