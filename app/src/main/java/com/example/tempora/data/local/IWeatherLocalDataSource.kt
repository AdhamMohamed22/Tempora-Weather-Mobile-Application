package com.example.tempora.data.local

import com.example.tempora.data.models.FavouriteLocation
import kotlinx.coroutines.flow.Flow

interface IWeatherLocalDataSource {

    suspend fun insertFavouriteLocation(favouriteLocation: FavouriteLocation)
    suspend fun getAllFavouriteLocations(): Flow<List<FavouriteLocation>>
    suspend fun deleteFavouriteLocation(favouriteLocation: FavouriteLocation)
}