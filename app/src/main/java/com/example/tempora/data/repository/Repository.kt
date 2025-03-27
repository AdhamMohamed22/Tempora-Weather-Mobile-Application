package com.example.tempora.data.repository

import com.example.tempora.data.local.WeatherLocalDataSource
import com.example.tempora.data.models.CurrentWeather
import com.example.tempora.data.models.FavouriteLocation
import com.example.tempora.data.models.ForecastWeather
import com.example.tempora.data.remote.WeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow

class Repository(private val remoteDataSource: WeatherRemoteDataSource,private val localDataSource: WeatherLocalDataSource) : IRepository{

    override suspend fun getCurrentWeather(lat: Double, lon: Double, appid: String, units: String?,language: String): Flow<CurrentWeather> {
        return remoteDataSource.getCurrentWeather(lat,lon,appid,units,language)
    }

    override suspend fun getForecastWeather(lat: Double, lon: Double, appid: String, units: String?,language: String): Flow<ForecastWeather> {
        return remoteDataSource.getForecastWeather(lat,lon,appid,units,language)
    }

    override suspend fun insertFavouriteLocation(favouriteLocation: FavouriteLocation) {
        localDataSource.insertFavouriteLocation(favouriteLocation)
    }

    override suspend fun getAllFavouriteLocations(): Flow<List<FavouriteLocation>> {
        return localDataSource.getAllFavouriteLocations()
    }

    override suspend fun deleteFavouriteLocation(favouriteLocation: FavouriteLocation) {
        localDataSource.deleteFavouriteLocation(favouriteLocation)
    }

    companion object {
        @Volatile
        private var INSTANCE: Repository? = null
        fun getInstance(
            remoteDataSource: WeatherRemoteDataSource,
            localDataSource: WeatherLocalDataSource
        ): Repository {
            return INSTANCE ?: synchronized(this) {
                val instance = Repository(remoteDataSource,localDataSource)
                INSTANCE = instance
                instance
            }
        }
    }
}