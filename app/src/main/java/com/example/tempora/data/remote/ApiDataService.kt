package com.example.tempora.data.remote

import com.example.tempora.data.models.CurrentWeather
import retrofit2.Response
import retrofit2.http.GET

interface ApiDataService {
    @GET("weather?lat=44.34&lon=10.99&appid=52eeded717ded0ae2029412d4f1ae35f")
    suspend fun getCurrentWeather(): Response<CurrentWeather>
}