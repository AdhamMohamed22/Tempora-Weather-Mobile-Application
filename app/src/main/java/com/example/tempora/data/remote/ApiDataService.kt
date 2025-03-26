package com.example.tempora.data.remote

import com.example.tempora.data.models.CurrentWeather
import com.example.tempora.data.models.ForecastWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiDataService {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String,
        @Query("units") units: String?,
        @Query("lang") language: String
    ): Response<CurrentWeather>

    @GET("data/2.5/forecast")
    suspend fun getForecastWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String,
        @Query("units") units: String?,
        @Query("lang") language: String
    ): Response<ForecastWeather>
}