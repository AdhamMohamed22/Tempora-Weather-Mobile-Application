package com.example.tempora.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.tempora.utils.converter.Converters

@TypeConverters(Converters::class)
@Entity(tableName = "cashedWeather")
data class CashedWeather(
    @PrimaryKey
    val id: Int = 1,
    val currentWeather: CurrentWeather,
    val forecastWeather: ForecastWeather,
)