package com.example.tempora.composables.settings

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tempora.composables.settings.utils.LocalizationHelper
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
                //Log.i("TAG", "******: $it")
            }
        }
        viewModelScope.launch {
            preferencesManager.getPreference(PreferencesManager.LOCATION_KEY, "GPS").collect {
                mutableSelectedLocation.value = it
                Log.i("TAG", "xxxx: $it")
            }
        }
        viewModelScope.launch {
            preferencesManager.getPreference(PreferencesManager.TEMPERATURE_UNIT_KEY, "Kelvin °K").collect {
                mutableSelectedTemperatureUnit.value = it
                //Log.i("TAG", "******: $it")
            }
        }
        viewModelScope.launch {
            preferencesManager.getPreference(PreferencesManager.WIND_SPEED_UNIT_KEY, "Meter/Sec").collect {
                mutableSelectedWindSpeedUnit.value = it
                //Log.i("TAG", "loadPreferences: $it")
            }
        }
    }

    fun savePreference(key: Preferences.Key<String>, value: String, context: Context) {
        viewModelScope.launch {
            preferencesManager.savePreference(key, value)

            if (key == PreferencesManager.LANGUAGE_KEY) {
                val languageCode = if (value == "Arabic" || value == "العربية") "ar" else "en"
                LocalizationHelper.setLocale(context, languageCode)

                // Restart the activity to apply language change
                val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
                intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }

            //Automatically Synchronise Temperature Unit With Wind Speed Unit & Vice Versa
            when (key) {
                PreferencesManager.TEMPERATURE_UNIT_KEY -> {
                    val automaticWindSpeedUnit = if (value == "Celsius °C" || value == "سيلسيوس °C" || value == "Kelvin °K" || value == "فهرنهايت °F") {
                        "Meters/Sec"
                    } else {
                        "Miles/Hour"
                    }
                    preferencesManager.savePreference(PreferencesManager.WIND_SPEED_UNIT_KEY, automaticWindSpeedUnit)
                    mutableSelectedWindSpeedUnit.value = automaticWindSpeedUnit
                }

                PreferencesManager.WIND_SPEED_UNIT_KEY -> {
                    val automaticTemperatureUnit = if (value == "Meters/Sec" || value == "متر/ثانية") {
                        if (mutableSelectedTemperatureUnit.value == "Fahrenheit °F" || mutableSelectedTemperatureUnit.value == "فهرنهايت °F") "Kelvin °K"
                        else mutableSelectedTemperatureUnit.value
                    } else {
                        "Fahrenheit °F"
                    }
                    preferencesManager.savePreference(PreferencesManager.TEMPERATURE_UNIT_KEY, automaticTemperatureUnit)
                    mutableSelectedTemperatureUnit.value = automaticTemperatureUnit
                }
            }

            if (key == PreferencesManager.LOCATION_KEY) {
                mutableSelectedLocation.value = if (value == "Map" || value == "الخريطة") "Map" else "GPS"
            }
        }
    }

}