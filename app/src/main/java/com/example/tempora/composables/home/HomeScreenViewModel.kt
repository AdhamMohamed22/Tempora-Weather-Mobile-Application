package com.example.tempora.composables.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale.filter

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

    private val mutableTodayForecastWeather = MutableStateFlow<ForecastWeatherResponseState>(ForecastWeatherResponseState.Loading)
    val todayForecastWeather = mutableTodayForecastWeather.asStateFlow()

    private val mutable5DaysForecastWeather = MutableStateFlow<ForecastWeatherResponseState>(ForecastWeatherResponseState.Loading)
    val daysForecastWeather = mutable5DaysForecastWeather.asStateFlow()

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

    fun getTodayForecastWeather(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.getForecastWeather(lat = 44.34,10.99,appid = "52eeded717ded0ae2029412d4f1ae35f")
                result
                    .catch {
                        ex -> mutableTodayForecastWeather.value = ForecastWeatherResponseState.Failed(ex)
                        mutableMessage.emit(ex.message.toString()) }
                    .map { it -> it.list.take(8)}
                    .collect {
                        mutableTodayForecastWeather.value = ForecastWeatherResponseState.Success(it)
                        Log.i("TAG", "getForecastWeather: ${it.size}")
                    }
            } catch (ex: Exception){
                mutableMessage.emit("An Error Occurred!, ${ex.message}")
                Log.i("TAG", "getForecastWeather: ${ex.message}")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun get5DaysForecastWeather(){

        val today = LocalDate.now(ZoneId.of("Africa/Cairo"))
        val todayFormattedDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.getForecastWeather(lat = 44.34,10.99,appid = "52eeded717ded0ae2029412d4f1ae35f")
                result
                    .catch {
                            ex -> mutable5DaysForecastWeather.value = ForecastWeatherResponseState.Failed(ex)
                            mutableMessage.emit(ex.message.toString()) }
                    .map { it -> it.list
                        .filter { item0 -> item0.dt_txt.substringBefore(" ") != todayFormattedDate } // Exclude today's forecast
                        .groupBy { item -> item.dt_txt.substringBefore(" ") } // Group by date
                        .map { (_, items) -> items.first() } // Take the first item from each date
                    }
                    .collect {
                        mutable5DaysForecastWeather.value = ForecastWeatherResponseState.Success(it)
                        Log.i("TAG", "getForecastWeather: ${it.size}")
                    }
            } catch (ex: Exception){
                mutableMessage.emit("An Error Occurred!, ${ex.message}")
                Log.i("TAG", "getForecastWeather: ${ex.message}")
            }
        }
    }
}