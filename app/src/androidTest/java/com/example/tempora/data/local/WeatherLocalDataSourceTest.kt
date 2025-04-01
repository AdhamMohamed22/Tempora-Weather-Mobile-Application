package com.example.tempora.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.tempora.data.models.Alarm
import com.example.tempora.data.models.CashedWeather
import com.example.tempora.data.models.City
import com.example.tempora.data.models.Clouds
import com.example.tempora.data.models.Coord
import com.example.tempora.data.models.CurrentWeather
import com.example.tempora.data.models.ForecastWeather
import com.example.tempora.data.models.Item0
import com.example.tempora.data.models.Main
import com.example.tempora.data.models.Rain
import com.example.tempora.data.models.Sys
import com.example.tempora.data.models.Weather
import com.example.tempora.data.models.Wind
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class WeatherLocalDataSourceTest {

    private lateinit var database: WeatherDatabase
    private lateinit var dao: WeatherDao
    private lateinit var localDataSource: WeatherLocalDataSource

    @Before
    fun setup(){
     database = Room.inMemoryDatabaseBuilder(
      ApplicationProvider.getApplicationContext(),
      WeatherDatabase::class.java
     )
      .allowMainThreadQueries()
      .build()
     dao = database.getWeatherDao()
     localDataSource = WeatherLocalDataSource(dao)
    }

    @After
    fun tearDown() {
     database.close()
    }

    @Test
    fun insertAndRetrieveCashedWeather() = runTest {
       //Given --> Create CashedWeather Object & Insert it Using localDataSource (localDataSource.insertCashedWeather(cashedWeather))
       val currentWeather = CurrentWeather(
        base = "stations",
        clouds = Clouds(50),
        cod = 200,
        coord = Coord(40.7128, -74.0060),
        dt = 1618317040,
        id = 5128581,
        main = Main(5.0, 1013, 60, 1013, 1013, 10.0, 12.0, 8.0),
        name = "New York",
        sys = Sys("US", 1, 1618282134, 1618326624, 1),
        timezone = -14400,
        visibility = 10000,
        weather = listOf(Weather("clear sky", "01d", 800, "Clear")),
        wind = Wind(180, 5.0, 3.0)
       )

       val forecastWeather = ForecastWeather(
        city = City(
         coord = Coord(40.7128, -74.0060),
         country = "US",
         id = 5128581,
         name = "New York",
         population = 8175133,
         sunrise = 1618282134,
         sunset = 1618326624,
         timezone = -14400
        ),
        cnt = 1,
        cod = "200",
        list = listOf(
         Item0(
          clouds = Clouds(50),
          dt = 1618317040,
          dt_txt = "2024-04-01 12:00:00",
          main = Main(5.0, 1013, 60, 1013, 1013, 10.0, 12.0, 8.0),
          pop = 0.1,
          rain = Rain(3.0),
          sys = Sys("US", 1, 1618282134, 1618326624, 1),
          visibility = 10000,
          weather = listOf(Weather("clear sky", "01d", 800, "Clear")),
          wind = Wind(180, 5.0, 3.0)
         )
        ),
        message = 0
       )

       val cashedWeather = CashedWeather(
        id = 1,
        currentWeather = currentWeather,
        forecastWeather = forecastWeather
       )
       localDataSource.insertCashedWeather(cashedWeather)

       //When --> Get CashedWeather Using localDataSource (localDataSource.getCashedWeather().first()) & Converting The Flow<CashedWeather> To CashedWeather
       val result = localDataSource.getCashedWeather().first()

       //Then --> Asserting The Retrieved Object "result" notNull & Comparing The Expected Values With The Actually Inserted Ones
       assertNotNull(result)
       assertEquals("New York", result.currentWeather.name)
       assertEquals(10.0, result.currentWeather.main.temp, 0.01)
       assertEquals("clear sky", result.currentWeather.weather.first().description)
       assertEquals("New York", result.forecastWeather.city.name)
       assertEquals(1, result.forecastWeather.list.size)
       assertEquals("clear sky", result.forecastWeather.list.first().weather.first().description)
    }

    @Test
    fun insertAndDeleteAlarm() = runTest {
       //Given --> Create Alarm Object & Insert it Using LocalDataSource (localDataSource.insertAlarm(alarm))
       val alarm = Alarm(selectedDate = "2025-04-01", selectedTime = "08:30 AM")
       localDataSource.insertAlarm(alarm)

       //When --> Get All Alarms Using LocalDataSource & Converting The Flow<List<Alarm>> To List<Alarm> Then To Alarm & Delete Alarm & Get All Alarms Using LocalDataSource & Converting The Flow<List<Alarm>> To List<Alarm> After Deletion
       val alarmsBeforeDelete = localDataSource.getAllAlarms().first()
       val retrievedAlarm = alarmsBeforeDelete.first()
       localDataSource.deleteAlarm(retrievedAlarm)
       val alarmsAfterDelete = localDataSource.getAllAlarms().first()

       //Then --> Asserting "alarmsAfterDelete" isEmpty
       assertTrue(alarmsAfterDelete.isEmpty())
    }
}