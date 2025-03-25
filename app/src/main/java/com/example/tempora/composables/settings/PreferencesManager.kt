package com.example.tempora.composables.settings

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("settings_prefs")

class PreferencesManager private constructor(context: Context) {
    private val dataStore = context.dataStore

    companion object {
        @Volatile
        private var INSTANCE: PreferencesManager? = null

        fun getInstance(context: Context): PreferencesManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: PreferencesManager(context).also { INSTANCE = it }
            }
        }

        val LANGUAGE_KEY = stringPreferencesKey("language")
        val LOCATION_KEY = stringPreferencesKey("location")
        val TEMPERATURE_UNIT_KEY = stringPreferencesKey("temperature_unit")
        val WIND_SPEED_UNIT_KEY = stringPreferencesKey("wind_speed_unit")
    }

    suspend fun savePreference(key: Preferences.Key<String>, value: String) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    fun getPreference(key: Preferences.Key<String>, defaultValue: String): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[key] ?: defaultValue
        }
    }
}
