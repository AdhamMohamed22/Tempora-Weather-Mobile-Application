package com.example.tempora.composables.home

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tempora.BuildConfig
import com.example.tempora.R
import com.example.tempora.composables.settings.PreferencesManager
import com.example.tempora.data.models.CashedWeather
import com.example.tempora.data.models.CurrentWeather
import com.example.tempora.data.models.ForecastWeather
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

    private var tempCurrentWeather: CurrentWeather? = null
    private var tempForecastWeather: ForecastWeather? = null

//    init {
//        getCurrentWeather()
//        getForecastWeather()
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentWeather(lat: Double, lon: Double, context: Context){
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
                tempCurrentWeather = result.first()
                insertCashedWeather(tempCurrentWeather,null)

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
                getCachedWeather(context)  // Fetch from Room DB if API call fails
                //mutableCurrentWeather.value = CurrentWeatherResponseState.Failed(ex)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTodayForecastWeather(lat: Double, lon: Double, context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            val selectedUnit = PreferencesManager.getInstance(context).getPreference(
                PreferencesManager.TEMPERATURE_UNIT_KEY,"Kelvin °K").first()
            val units = getTemperatureUnit(selectedUnit)

            val selectedLanguage = PreferencesManager.getInstance(context).getPreference(
                PreferencesManager.LANGUAGE_KEY, "English").first()
            var language = if(selectedLanguage == "Arabic") "ar" else "en"

            try {
                val result = repository.getForecastWeather(lat,lon,BuildConfig.appidSafe,units,language)
                tempForecastWeather = result.first()
                insertCashedWeather(null,tempForecastWeather)

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
                //mutableTodayForecastWeather.value = ForecastWeatherResponseState.Failed(ex)
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
            val units = getTemperatureUnit(selectedUnit)

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
                //mutable5DaysForecastWeather.value = ForecastWeatherResponseState.Failed(ex)
                Log.i("TAG", "getForecastWeather: ${ex.message}")
            }
        }
    }

    private fun insertCashedWeather(cashedCurrentWeather: CurrentWeather?, cashedForecastWeather: ForecastWeather?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Store cashed values
                cashedCurrentWeather?.let { tempCurrentWeather = it }
                cashedForecastWeather?.let { tempForecastWeather = it }

                // Insert into database only when both values are available
                if (tempCurrentWeather != null && tempForecastWeather != null) {
                    val cashedWeather = CashedWeather(
                        currentWeather = tempCurrentWeather!!,
                        forecastWeather = tempForecastWeather!!
                    )
                    repository.insertCashedWeather(cashedWeather)

                    // Reset temporary storage after insertion
                    tempCurrentWeather = null
                    tempForecastWeather = null
                }
            } catch (ex: Exception){
                mutableMessage.emit("An Error Occurred!, ${ex.message}")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCachedWeather(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val cachedWeather = repository.getCashedWeather().first()
                if (cachedWeather != null) {
                    // Load cached current weather
                    mutableCurrentWeather.value = CurrentWeatherResponseState.Success(cachedWeather.currentWeather)

                    // Load cached today's forecast
                    mutableTodayForecastWeather.value = ForecastWeatherResponseState.Success(
                        cachedWeather.forecastWeather.list.take(8) // Take today's forecast
                    )

                    // Load cached 5-day forecast
                    val today = LocalDate.now(ZoneId.of("Africa/Cairo"))
                    val todayFormattedDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

                    val filteredForecast = cachedWeather.forecastWeather.list
                        .filter { item -> item.dt_txt.substringBefore(" ") != todayFormattedDate }
                        .groupBy { item -> item.dt_txt.substringBefore(" ") }
                        .map { (_, items) -> items.first() }

                    mutable5DaysForecastWeather.value = ForecastWeatherResponseState.Success(filteredForecast)

                    mutableMessage.emit(context.getString(R.string.network_connection_lost))
                    Log.i("TAG", "Loaded cached weather: $cachedWeather")
                } else {
                    mutableMessage.emit("No cached weather available!")
                    mutableCurrentWeather.value = CurrentWeatherResponseState.Failed(Exception("No cached data"))
                }
            } catch (ex: Exception) {
                mutableMessage.emit("Failed to load cached weather: ${ex.message}")
                mutableCurrentWeather.value = CurrentWeatherResponseState.Failed(ex)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadCachedWeather(context: Context) {
        getCachedWeather(context)
    }

}
