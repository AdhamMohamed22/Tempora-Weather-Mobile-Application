package com.example.tempora.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tempora.data.models.Alarm
import com.example.tempora.data.models.FavouriteLocation
import com.example.tempora.utils.converter.Converters

@TypeConverters(Converters::class)
@Database(entities = [FavouriteLocation::class,Alarm::class], version = 6)
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