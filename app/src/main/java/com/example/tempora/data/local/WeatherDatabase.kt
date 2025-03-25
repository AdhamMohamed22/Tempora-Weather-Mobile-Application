package com.example.tempora.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tempora.data.models.FavouriteLocation

@Database(entities = [FavouriteLocation::class], version = 3)
abstract class WeatherDatabase : RoomDatabase(){

    abstract fun getWeatherDao(): WeatherDao

    companion object{
        @Volatile
        private var INSTANCE: WeatherDatabase? = null
        fun getInstance(context: Context): WeatherDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context, WeatherDatabase::class.java,"FavouriteLocationsDB").fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}