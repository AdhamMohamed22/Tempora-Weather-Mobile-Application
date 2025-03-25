package com.example.tempora.composables.settings

import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsScreenViewModel(private val preferencesManager: PreferencesManager): ViewModel() {

    class SettingScreenViewModelFactory(private val preferencesManager: PreferencesManager): ViewModelProvider.Factory
    {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SettingsScreenViewModel(preferencesManager) as T
        }
    }

    private val mutableSelectedLanguage = MutableStateFlow("English")
    val selectedLanguage = mutableSelectedLanguage.asStateFlow()

    private val mutableSelectedLocation = MutableStateFlow("GPS")
    val selectedLocation = mutableSelectedLocation.asStateFlow()

    private val mutableSelectedTemperatureUnit = MutableStateFlow("Kelvin °K")
    val selectedTemperatureUnit = mutableSelectedTemperatureUnit.asStateFlow()

    private val mutableSelectedWindSpeedUnit = MutableStateFlow("Meters/Sec")
    val selectedWindSpeedUnit = mutableSelectedWindSpeedUnit.asStateFlow()

    init {
        loadPreferences()
    }

    private fun loadPreferences() {
        viewModelScope.launch {
            preferencesManager.getPreference(PreferencesManager.LANGUAGE_KEY, "English").collect {
                mutableSelectedLanguage.value = it
            }
        }
        viewModelScope.launch {
            preferencesManager.getPreference(PreferencesManager.LOCATION_KEY, "GPS").collect {
                mutableSelectedLocation.value = it
            }
        }
        viewModelScope.launch {
            preferencesManager.getPreference(PreferencesManager.TEMPERATURE_UNIT_KEY, "Kelvin °K").collect {
                mutableSelectedTemperatureUnit.value = it
                Log.i("TAG", "loadPreferences: $it")
            }
        }
        viewModelScope.launch {
            preferencesManager.getPreference(PreferencesManager.WIND_SPEED_UNIT_KEY, "Meter/Sec").collect {
                mutableSelectedWindSpeedUnit.value = it
                Log.i("TAG", "loadPreferences: $it")
            }
        }
    }

    fun savePreference(key: Preferences.Key<String>, value: String) {
        viewModelScope.launch {
            preferencesManager.savePreference(key, value)

            //Automatically Synchronise Temperature Unit With Wind Speed Unit & Vice Versa
            when (key) {
                PreferencesManager.TEMPERATURE_UNIT_KEY -> {
                    val automaticWindSpeedUnit = if (value == "Celsius °C" || value == "Kelvin °K") {
                        "Meters/Sec"
                    } else {
                        "Miles/Hour"
                    }
                    preferencesManager.savePreference(PreferencesManager.WIND_SPEED_UNIT_KEY, automaticWindSpeedUnit)
                    mutableSelectedWindSpeedUnit.value = automaticWindSpeedUnit
                }

                PreferencesManager.WIND_SPEED_UNIT_KEY -> {
                    val automaticTemperatureUnit = if (value == "Meters/Sec") {
                        if (mutableSelectedTemperatureUnit.value == "Fahrenheit °F") "Kelvin °K"
                        else mutableSelectedTemperatureUnit.value
                    } else {
                        "Fahrenheit °F"
                    }
                    preferencesManager.savePreference(PreferencesManager.TEMPERATURE_UNIT_KEY, automaticTemperatureUnit)
                    mutableSelectedTemperatureUnit.value = automaticTemperatureUnit
                }
            }
        }
    }

}