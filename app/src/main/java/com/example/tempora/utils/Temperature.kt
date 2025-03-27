package com.example.tempora.utils

fun getTemperatureUnit(selectedUnit: String): String? {
    return when (selectedUnit) {
        "Celsius °C" -> "metric"
        "Fahrenheit °F" -> "imperial"
        else -> null
    }
}

fun getTemperatureSymbol(selectedUnit: String): String {
    return when (selectedUnit) {
        "Celsius °C" -> "°C"
        "Fahrenheit °F" -> "°F"
        else -> "°K"
    }
}