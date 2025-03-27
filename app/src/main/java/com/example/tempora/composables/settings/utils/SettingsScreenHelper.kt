package com.example.tempora.composables.settings.utils

import java.util.Locale


fun getTemperatureUnit(value: String): String {
    val language = Locale.getDefault().language
    if(language == "ar"){
        return when(value){
            "Celsius °C" -> "سيلسيوس °C"
            "Kelvin °K" -> "كلفن °K"
            "Fahrenheit °F" -> "فهرنهايت °F"
            else -> "سيلسيوس °C"
        }
    }
    return value
}

fun getTemperatureValue(value: String): String {
    val language = Locale.getDefault().language
    if(language == "ar"){
        return when(value){
            "سيلسيوس °C" -> "Celsius °C"
            "كلفن °K" -> "Kelvin °K"
            "فهرنهايت °F" -> "Fahrenheit °F"
            else -> "Celsius °C"
        }
    }
    return value
}

fun getWindSpeedUnit(value: String): String {
    val language = Locale.getDefault().language
    if(language == "ar"){
        return when(value){
            "Meters/Sec" -> "متر/ثانية"
            "Miles/Hour" -> "ميل/ساعة"
            else -> "متر/ثانية"
        }
    }
    return value
}

fun getWindSpeedValue(value: String): String {
    val language = Locale.getDefault().language
    if(language == "ar"){
        return when(value){
            "متر/ثانية" -> "Meters/Sec"
            "ميل/ساعة" -> "Miles/Hour"
            else -> "Meters/Sec"
        }
    }
    return value
}

fun getLanguage(value: String): String {
    val language = Locale.getDefault().language
    if(language == "ar"){
        return when(value){
            "English" -> "الإنجليزية"
             "Arabic" -> "العربية"
             else -> "الإنجليزية"
        }
    }
    return value
}

fun getLanguageValue(value: String): String {
    val language = Locale.getDefault().language
    if(language == "ar"){
        return when(value){
            "الإنجليزية" -> "English"
            "العربية" -> "Arabic"
            else -> "English"
        }
    }
    return value
}

fun getLocation(value: String): String {
    val language = Locale.getDefault().language
    if(language == "ar"){
        return when(value){
            "GPS" -> "نظام تحديد المواقع (GPS)"
            "Map" -> "الخريطة"
            else -> "نظام تحديد المواقع (GPS)"
        }
    }
    return value
}

fun getLocationValue(value: String): String {
    val language = Locale.getDefault().language
    if(language == "ar"){
        return when(value){
            "نظام تحديد المواقع (GPS)" -> "GPS"
            "الخريطة" -> "Map"
            else -> "GPS"
        }
    }
    return value
}



























