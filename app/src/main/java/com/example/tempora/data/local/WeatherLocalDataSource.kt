package com.example.tempora.data.local

import com.example.tempora.data.models.FavouriteLocation
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSource(private val weatherDao: WeatherDao): IWeatherLocalDataSource {
    override suspend fun insertFavouriteLocation(favouriteLocation: FavouriteLocation) {
        weatherDao.insertFavouriteLocation(favouriteLocation)
    }

    override suspend fun getAllFavouriteLocations(): Flow<List<FavouriteLocation>> {
        return weatherDao.getAllFavouriteLocations()
    }

    override suspend fun deleteFavouriteLocation(favouriteLocation: FavouriteLocation) {
        weatherDao.deleteFavouriteLocation(favouriteLocation)
    }
}