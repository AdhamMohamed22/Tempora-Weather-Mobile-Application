package com.example.tempora.composables.home

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tempora.BuildConfig
import com.example.tempora.composables.settings.PreferencesManager
import com.example.tempora.data.repository.Repository
import com.example.tempora.data.response_state.CurrentWeatherResponseState
import com.example.tempora.data.response_state.ForecastWeatherResponseState
import com.example.tempora.utils.getTemperatureSymbol
import com.example.tempora.utils.getTemperatureUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class HomeScreenViewModel(private val repository: Repository) : ViewModel() {

    class HomeScreenViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory
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

    private val mutableSelectedUnit = MutableStateFlow("°K")
    val selectedUnit = mutableSelectedUnit.asStateFlow()

//    init {
//        getCurrentWeather()
//        getForecastWeather()
//    }

    fun getCurrentWeather(lat: Double,lon: Double,context: Context){
        viewModelScope.launch(Dispatchers.IO){
            val selectedUnit = PreferencesManager.getInstance(context).getPreference(
                PreferencesManager.TEMPERATURE_UNIT_KEY,"Kelvin °K").first()
            val units = getTemperatureUnit(selectedUnit)

            val selectedLanguage = PreferencesManager.getInstance(context).getPreference(
                PreferencesManager.LANGUAGE_KEY, "English").first()
            val language = if(selectedLanguage == "Arabic") "ar" else "en"

            // Store the selected unit symbol
            mutableSelectedUnit.value = getTemperatureSymbol(selectedUnit)

            try {
                val result = repository.getCurrentWeather(lat,lon,BuildConfig.appidSafe,units,language)
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
                mutableCurrentWeather.value = CurrentWeatherResponseState.Failed(ex)
            }
        }
    }

    fun getTodayForecastWeather(lat: Double,lon: Double,context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            val selectedUnit = PreferencesManager.getInstance(context).getPreference(
                PreferencesManager.TEMPERATURE_UNIT_KEY,"Kelvin °K").first()
            val units = com.example.tempora.utils.getTemperatureUnit(selectedUnit)

            val selectedLanguage = PreferencesManager.getInstance(context).getPreference(
                PreferencesManager.LANGUAGE_KEY, "English").first()
            var language = if(selectedLanguage == "Arabic") "ar" else "en"

            try {
                val result = repository.getForecastWeather(lat,lon,BuildConfig.appidSafe,units,language)
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
                //mutableMessage.emit("An Error Occurred!, ${ex.message}")
                mutableTodayForecastWeather.value = ForecastWeatherResponseState.Failed(ex)
                Log.i("TAG", "getForecastWeather: ${ex.message}")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun get5DaysForecastWeather(lat: Double,lon: Double,context: Context){

        val today = LocalDate.now(ZoneId.of("Africa/Cairo"))
        val todayFormattedDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        viewModelScope.launch(Dispatchers.IO) {
            val selectedUnit = PreferencesManager.getInstance(context).getPreference(
                PreferencesManager.TEMPERATURE_UNIT_KEY,"Kelvin °K").first()
            val units = com.example.tempora.utils.getTemperatureUnit(selectedUnit)

            val selectedLanguage = PreferencesManager.getInstance(context).getPreference(
                PreferencesManager.LANGUAGE_KEY, "English").first()
            var language = if(selectedLanguage == "Arabic") "ar" else "en"

            try {
                val result = repository.getForecastWeather(lat,lon,BuildConfig.appidSafe,units,language)
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
                //mutableMessage.emit("An Error Occurred!, ${ex.message}")
                mutable5DaysForecastWeather.value = ForecastWeatherResponseState.Failed(ex)
                Log.i("TAG", "getForecastWeather: ${ex.message}")
            }
        }
    }
}
