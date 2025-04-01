package com.example.tempora.data.repository

import com.example.tempora.data.local.WeatherLocalDataSource
import com.example.tempora.data.models.Alarm
import com.example.tempora.data.remote.WeatherRemoteDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class RepositoryTest {

     private lateinit var repository: Repository
     private val remoteDataSource: WeatherRemoteDataSource = mockk(relaxed = true)
     private val localDataSource: WeatherLocalDataSource = mockk(relaxed = true)

     @Before
     fun setUp() {
      repository = Repository(remoteDataSource, localDataSource)
     }

     @Test
     fun insertAlarm_shouldCallLocalDataSourceInsertAlarm() = runTest {

         //Given
         val alarm = Alarm(selectedDate = "2025-04-01", selectedTime = "08:30 AM")

         //When
         repository.insertAlarm(alarm)

         //That
         coVerify { localDataSource.insertAlarm(alarm) }
     }

     @Test
     fun deleteAlarm_shouldCallLocalDataSourceInsertAlarm() = runTest {

         //Given
         val alarm = Alarm(selectedDate = "2025-04-01", selectedTime = "08:30 AM")

         //When
         repository.deleteAlarm(alarm)

         //That
         coVerify { localDataSource.deleteAlarm(alarm) }
     }

     @Test
     fun getAllAlarms_shouldReturnFlowOfAlarms() = runTest {

         //Given
         val alarmList = listOf(
          Alarm(selectedDate = "2025-04-01", selectedTime = "08:30 AM"),
          Alarm(selectedDate = "2025-04-02", selectedTime = "09:30 AM")
         )
         //Mocking
         coEvery { localDataSource.getAllAlarms() } returns flowOf(alarmList)

         //When
         val result = repository.getAllAlarms().first()

         //Then
         assertEquals(result,alarmList)
     }
}