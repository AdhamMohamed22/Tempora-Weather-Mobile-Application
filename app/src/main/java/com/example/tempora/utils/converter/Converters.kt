package com.example.tempora.utils.converter

import androidx.room.TypeConverter
import com.example.tempora.data.models.CurrentWeather
import com.example.tempora.data.models.ForecastWeather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromCurrentWeather(currentWeather: CurrentWeather): String {
        return gson.toJson(currentWeather)
    }

    @TypeConverter
    fun toCurrentWeather(data: String): CurrentWeather {
        val type = object : TypeToken<CurrentWeather>() {}.type
        return gson.fromJson(data, type)
    }

    @TypeConverter
    fun fromForecastWeather(forecast: ForecastWeather): String {
        return gson.toJson(forecast)
    }

    @TypeConverter
    fun toForecastWeather(data: String): ForecastWeather {
        val type = object : TypeToken<ForecastWeather>() {}.type
        return gson.fromJson(data, type)
    }
}