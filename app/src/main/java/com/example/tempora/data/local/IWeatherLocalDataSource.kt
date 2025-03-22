package com.example.tempora.data.local

import com.example.tempora.data.models.FavouriteLocation

interface IWeatherLocalDataSource {
    suspend fun insertFavouriteLocation(favouriteLocation: FavouriteLocation)
}