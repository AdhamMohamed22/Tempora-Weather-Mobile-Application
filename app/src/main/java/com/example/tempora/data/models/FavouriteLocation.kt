package com.example.tempora.data.models

import androidx.room.Entity
import androidx.room.TypeConverters
import com.example.tempora.utils.converter.Converters

@TypeConverters(Converters::class)
@Entity(tableName = "favouriteLocations",primaryKeys = ["latitude", "longitude"])
data class FavouriteLocation(
    val latitude: Double,
    val longitude: Double,
    val country: String?,
    val currentWeather: CurrentWeather,
    val forecastWeather: ForecastWeather
)