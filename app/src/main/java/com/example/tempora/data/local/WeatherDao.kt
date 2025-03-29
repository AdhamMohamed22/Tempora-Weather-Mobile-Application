package com.example.tempora.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tempora.data.models.FavouriteLocation
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteLocation(favouriteLocation: FavouriteLocation)

    @Query("SELECT * FROM favouriteLocations")
    fun getAllFavouriteLocations(): Flow<List<FavouriteLocation>>

    @Delete
    suspend fun deleteFavouriteLocation(favouriteLocation: FavouriteLocation)

}