package com.example.tempora.composables.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tempora.data.models.CurrentWeather
import com.example.tempora.data.repository.Repository
import com.example.tempora.data.response_state.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val repository: Repository) : ViewModel() {

    class CurrentWeatherFactory(private val repository: Repository) : ViewModelProvider.Factory
    {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeScreenViewModel(repository) as T
        }
    }

    private val mutableCurrentWeather = MutableStateFlow<ResponseState>(ResponseState.Loading)
    val currentWeather = mutableCurrentWeather.asStateFlow()

    private val mutableMessage = MutableSharedFlow<String>()
    val message = mutableMessage.asSharedFlow()

    init {
        getCurrentWeather()
    }

    fun getCurrentWeather(){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val result = repository.getCurrentWeather(lat = 44.34,10.99,appid = "52eeded717ded0ae2029412d4f1ae35f")
                result
                    .catch {
                        ex -> mutableCurrentWeather.value = ResponseState.Failed(ex)
                        mutableMessage.emit(ex.message.toString()) }
                    .collect{
                        mutableCurrentWeather.value = ResponseState.Success(it)
                        Log.i("TAG", "getCurrentWeather: $it")
                    }
            } catch (ex: Exception){
                mutableMessage.emit("An Error Occurred!, ${ex.message}")
            }
        }
    }

}