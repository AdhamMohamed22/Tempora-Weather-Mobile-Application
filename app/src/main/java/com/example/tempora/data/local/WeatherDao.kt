package com.example.tempora.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.tempora.data.models.FavouriteLocation

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteLocation(favouriteLocation: FavouriteLocation)

//    @Query("SELECT * FROM locations")
//    suspend fun getAllLocations(): List<LocationEntity>
//
//    @Query("DELETE FROM locations WHERE id = :locationId")
//    suspend fun deleteLocation(locationId: Int)
}