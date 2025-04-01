package com.example.tempora.composables.alarms

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tempora.data.models.Alarm
import com.example.tempora.data.repository.Repository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import com.example.tempora.R
import com.example.tempora.data.response_state.AlarmsResponseState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class AlarmsScreenViewModelTest {

    private lateinit var repository: Repository
    private lateinit var viewModel: AlarmsScreenViewModel
    private lateinit var context: Context

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        viewModel = AlarmsScreenViewModel(repository)
        context = mockk(relaxed = true)
    }


    @Test
    @Config(manifest = Config.NONE)
    fun insertAlarm_validAlarm_emitsSuccessMessage() = runTest {

        //Given
        val alarm = Alarm(selectedDate = "2025-04-01", selectedTime = "08:30 AM")
        //Mocking
        coEvery { repository.insertAlarm(alarm) } returns Unit

        //When
        viewModel.insertAlarm(alarm, context)
        advanceUntilIdle() // Use advanceUntilIdle to make sure all coroutines finish execution

        //Then
        coVerify { repository.insertAlarm(alarm) }

        val toastMessage = viewModel.message.first()
        assertThat(toastMessage, `is`(context.getString(R.string.alarm_added_successfully)))
    }

    @Test
    @Config(manifest = Config.NONE)
    fun deleteAlarm_validAlarm_emitsSuccessMessage() = runTest {

        //Given
        val alarm = Alarm(selectedDate = "2025-04-01", selectedTime = "08:30 AM")
        //Mocking
        coEvery { repository.deleteAlarm(alarm) } returns Unit

        //When
        viewModel.deleteAlarm(alarm, context)
        advanceUntilIdle() // Use advanceUntilIdle to make sure all coroutines finish execution

        //Then
        coVerify { repository.deleteAlarm(alarm) }

        val toastMessage = viewModel.message.first()
        assertThat(toastMessage, `is`(context.getString(R.string.alarm_deleted_successfully)))
    }

    @Test
    @Config(manifest = Config.NONE)
    fun getAllAlarms_successfulFetch_emitsAlarms() = runTest {

        //Given
        val alarmList = listOf(
            Alarm(selectedDate = "2025-04-01", selectedTime = "08:30 AM"),
            Alarm(selectedDate = "2025-04-02", selectedTime = "09:30 AM")
        )
        //Mocking
        coEvery { repository.getAllAlarms() } returns flowOf(alarmList)

        //When
        viewModel.getAllAlarms()
        advanceUntilIdle() // Ensure all coroutines finish

        //Then
        val result = viewModel.alarm.first { it is AlarmsResponseState.Success } // Wait for success state
        assertThat(result, `is`(AlarmsResponseState.Success(alarmList)))
    }

}
