package com.example.tempora.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.tempora.data.models.Alarm
import com.example.tempora.data.models.City
import com.example.tempora.data.models.Clouds
import com.example.tempora.data.models.Coord
import com.example.tempora.data.models.CurrentWeather
import com.example.tempora.data.models.FavouriteLocation
import com.example.tempora.data.models.ForecastWeather
import com.example.tempora.data.models.Item0
import com.example.tempora.data.models.Main
import com.example.tempora.data.models.Rain
import com.example.tempora.data.models.Sys
import com.example.tempora.data.models.Weather
import com.example.tempora.data.models.Wind
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.hasItem
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse

@RunWith(AndroidJUnit4::class)
@SmallTest
class WeatherDaoTest {

    private lateinit var database: WeatherDatabase
    private lateinit var dao: WeatherDao

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        ).build()
        dao = database.getWeatherDao()
    }

    @After
    fun tearDown() = database.close()


    @Test
    fun insertAndRetrieveFavouriteLocation() = runTest {
        //Given --> Create FavouriteLocation Object & Insert it (insertFavouriteLocation(favouriteLocation))
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

        val favouriteLocation = FavouriteLocation(
            latitude = 40.7128,
            longitude = -74.0060,
            country = "US",
            currentWeather = currentWeather,
            forecastWeather = forecastWeather
        )
        dao.insertFavouriteLocation(favouriteLocation)

        //When --> Get All FavouritesLocations (getAllFavouriteLocations()) & Converting The Flow<List<FavouriteLocations>> To List<FavouriteLocations>
        val favouriteLocations = dao.getAllFavouriteLocations().first()

        //Then --> AssertThat The Retrieved FavouriteLocations List isNotEmpty & Contains The FavouriteLocation Inserted
        assertTrue(favouriteLocations.isNotEmpty())
        assertThat(favouriteLocations, hasItem(favouriteLocation))
    }

    @Test
    fun insertAndRetrieveAlarm() = runTest {
        //Given --> Create Alarm Object & Insert it (insertAlarm(alarm))
        val alarm = Alarm(selectedDate = "2025-04-01", selectedTime = "08:30 AM")
        dao.insertAlarm(alarm)

        //When --> Get All Alarms (getAllAlarms()) & Converting The Flow<List<Alarm>> To List<Alarm> Then To Alarm
        val alarms = dao.getAllAlarms().first()
        val retrievedAlarm = alarms.first()

        //Then --> Asserting The List "alarms" isNotEmpty & Comparing The Expected Values With The Actually Inserted Ones Without id (as it is auto-generated) "retrievedAlarm"
        assertTrue(alarms.isNotEmpty())
        assertEquals("2025-04-01", retrievedAlarm.selectedDate)
        assertEquals("08:30 AM", retrievedAlarm.selectedTime)
    }

    @Test
    fun insertAndDeleteAlarm() = runTest {
        //Given --> Create Alarm Object & Insert it (insertAlarm(alarm))
        val alarm = Alarm(selectedDate = "2025-04-01", selectedTime = "08:30 AM")
        dao.insertAlarm(alarm)

        //When --> Get All Alarms,Converting The Flow<List<Alarm>> To List<Alarm> & Then --> Assert "alarmsBeforeDelete" isNotEmpty
        val alarmsBeforeDelete = dao.getAllAlarms().first()
        assertTrue(alarmsBeforeDelete.isNotEmpty())

        //When --> Delete Alarm (deleteAlarm(alarm)) - Get All Alarms After Deletion And Converting The Flow<List<Alarm>> To List<Alarm> & Then --> Assert Retrieved List "alarmsAfterDelete" Not Contains The Deleted Alarm
        dao.deleteAlarm(alarm)
        val alarmsAfterDelete = dao.getAllAlarms().first()
        assertFalse(alarmsAfterDelete.contains(alarm))
    }

}