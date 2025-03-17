package com.example.tempora.data.response_state

import com.example.tempora.data.models.CurrentWeather

sealed class ResponseState {
    data object Loading: ResponseState()
    data class Success(val currentWeather: CurrentWeather): ResponseState()
    data class Failed(val error: Throwable): ResponseState()
}