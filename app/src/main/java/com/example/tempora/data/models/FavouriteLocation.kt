package com.example.tempora.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favouriteLocations",primaryKeys = ["latitude", "longitude"])
data class FavouriteLocation(
    val latitude: Double,
    val longitude: Double,
    val name: String? = null // Optional, to store location name
)