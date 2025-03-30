package com.example.tempora.composables.alarms

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tempora.data.models.Alarm
import com.example.tempora.data.models.FavouriteLocation
import com.example.tempora.data.repository.Repository
import com.example.tempora.data.response_state.AlarmsResponseState
import com.example.tempora.data.response_state.FavouriteLocationsResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AlarmsScreenViewModel(private val repository: Repository): ViewModel() {

    class AlarmsScreenViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory
    {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AlarmsScreenViewModel(repository) as T
        }
    }

    private val mutableAlarms = MutableStateFlow<AlarmsResponseState>(AlarmsResponseState.Loading)
    val alarm = mutableAlarms.asStateFlow()

    private val mutableMessage = MutableSharedFlow<String>()
    val message = mutableMessage.asSharedFlow()

    fun insertAlarm(alarm: Alarm){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.insertAlarm(alarm)
            } catch (ex: Exception){
                mutableMessage.emit("An Error Occurred!, ${ex.message}")
            }
        }
    }

    fun getAllAlarms(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.getAllAlarms()
                result
                    .catch {
                        ex -> mutableAlarms.value = AlarmsResponseState.Failed(ex)
                        mutableMessage.emit(ex.message.toString()) }
                    .collect {
                        mutableAlarms.value = AlarmsResponseState.Success(it)
                    }
            } catch (ex: Exception){
                mutableMessage.emit("An Error Occurred!, ${ex.message}")
            }
        }
    }

    fun deleteAlarm(alarm: Alarm){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.deleteAlarm(alarm)
                mutableMessage.emit("Alarm Deleted Successfully!")
            } catch (ex: Exception){
                mutableMessage.emit("Couldn't Delete Alarm From Alarms Screen ${ex.message}")
            }
        }
    }
}