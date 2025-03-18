package com.example.tempora.composables.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tempora.data.repository.Repository
import com.example.tempora.data.response_state.CurrentWeatherResponseState
import com.example.tempora.data.response_state.ForecastWeatherResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val repository: Repository) : ViewModel() {

    class CurrentWeatherFactory(private val repository: Repository) : ViewModelProvider.Factory
    {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeScreenViewModel(repository) as T
        }
    }

    private val mutableCurrentWeather = MutableStateFlow<CurrentWeatherResponseState>(CurrentWeatherResponseState.Loading)
    val currentWeather = mutableCurrentWeather.asStateFlow()

    private val mutableMessage = MutableSharedFlow<String>()
    val message = mutableMessage.asSharedFlow()

    private val mutableForecastWeather = MutableStateFlow<ForecastWeatherResponseState>(ForecastWeatherResponseState.Loading)
    val forecastWeather = mutableForecastWeather.asStateFlow()

//    init {
//        getCurrentWeather()
//        getForecastWeather()
//    }

    fun getCurrentWeather(){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val result = repository.getCurrentWeather(lat = 44.34,10.99,appid = "52eeded717ded0ae2029412d4f1ae35f")
                result
                    .catch {
                        ex -> mutableCurrentWeather.value = CurrentWeatherResponseState.Failed(ex)
                        mutableMessage.emit(ex.message.toString()) }
                    .collect{
                        mutableCurrentWeather.value = CurrentWeatherResponseState.Success(it)
                        Log.i("TAG", "getCurrentWeather: $it")
                    }
            } catch (ex: Exception){
                mutableMessage.emit("An Error Occurred!, ${ex.message}")
            }
        }
    }

    fun getForecastWeather(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.getForecastWeather(lat = 44.34,10.99,appid = "52eeded717ded0ae2029412d4f1ae35f")
                result
                    .catch {
                        ex -> mutableForecastWeather.value = ForecastWeatherResponseState.Failed(ex)
                        mutableMessage.emit(ex.message.toString()) }
                    .map { it -> it.list.take(8)}
                    .collect {
                        mutableForecastWeather.value = ForecastWeatherResponseState.Success(it)
                        Log.i("TAG", "getForecastWeather: ${it.size}")
                    }
            } catch (ex: Exception){
                mutableMessage.emit("An Error Occurred!, ${ex.message}")
                Log.i("TAG", "getForecastWeather: ${ex.message}")
            }
        }
    }
}