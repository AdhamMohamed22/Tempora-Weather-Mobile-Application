package com.example.tempora.composables.settings.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPref (context: Context){
    private val sharedPreferences: SharedPreferences =
        context.applicationContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()
    companion object {
        private const val FILENAME = "sharedPrefFile"
        @Volatile
        private var instance: SharedPref? = null

        fun getInstance(context: Context): SharedPref {
            return instance ?: synchronized(this) {
                instance ?: SharedPref(context).also { instance = it }
            }
        }
    }

    fun setLatitude(latitude: Double) {
        editor.putFloat("latitude", latitude.toFloat()).apply()
        editor.commit()
    }
    fun setLongitude(longitude: Double) {
        editor.putFloat("longitude", longitude.toFloat()).apply()
        editor.commit()
    }
    fun getLatitude(): Double {
        return sharedPreferences.getFloat("latitude", 0.0f).toDouble()
    }
    fun getLongitude(): Double {
        return sharedPreferences.getFloat("longitude", 0.0f).toDouble()
    }
    fun setGpsSelected(isSelected: Boolean) {
        editor.putBoolean("gpsSelected", isSelected).apply()
        editor.commit()
    }
    fun getGpsSelected(): Boolean {
        return sharedPreferences.getBoolean("gpsSelected", true)
    }

}