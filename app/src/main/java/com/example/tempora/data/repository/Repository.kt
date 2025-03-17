package com.example.tempora.data.repository

import com.example.tempora.data.models.CurrentWeather
import com.example.tempora.data.remote.WeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow

class Repository(private val remoteDataSource: WeatherRemoteDataSource) : IRepository{
    override suspend fun getCurrentWeather(lat: Double, lon: Double, appid: String): Flow<CurrentWeather> {
        return remoteDataSource.getCurrentWeather(lat,lon,appid)
    }

    companion object {
        @Volatile
        private var INSTANCE: Repository? = null
        fun getInstance(
            //localDataSource: ProductLocalDataSource,
            remoteDataSource: WeatherRemoteDataSource
        ): Repository {
            return INSTANCE ?: synchronized(this) {
                val instance = Repository(remoteDataSource)
                INSTANCE = instance
                instance
            }
        }
    }

}