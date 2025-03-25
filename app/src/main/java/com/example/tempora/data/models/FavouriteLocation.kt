package com.example.tempora.data.models

import androidx.room.Entity


@Entity(tableName = "favouriteLocations",primaryKeys = ["latitude", "longitude"])
data class FavouriteLocation(
    val latitude: Double,
    val longitude: Double,
    val country: String?
)