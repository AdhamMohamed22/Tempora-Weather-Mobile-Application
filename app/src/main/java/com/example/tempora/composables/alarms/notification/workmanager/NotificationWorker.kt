package com.example.tempora.composables.alarms.notification.workmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.tempora.BuildConfig
import com.example.tempora.composables.alarms.notification.showNotification
import com.example.tempora.composables.settings.PreferencesManager
import com.example.tempora.composables.settings.utils.SharedPref
import com.example.tempora.data.local.WeatherDatabase
import com.example.tempora.data.local.WeatherLocalDataSource
import com.example.tempora.data.remote.RetrofitHelper
import com.example.tempora.data.remote.WeatherRemoteDataSource
import com.example.tempora.data.repository.Repository
import com.example.tempora.utils.getTemperatureUnit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class NotificationWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {

        val repository = Repository.getInstance(
            WeatherRemoteDataSource(RetrofitHelper.retrofit),
            WeatherLocalDataSource(WeatherDatabase.getInstance(applicationContext).getWeatherDao())
        )

        CoroutineScope(Dispatchers.IO).launch {
            val selectedUnit = PreferencesManager.getInstance(applicationContext).getPreference(
                PreferencesManager.TEMPERATURE_UNIT_KEY,"Kelvin °K").first()
            val units = getTemperatureUnit(selectedUnit)

            val selectedLanguage = PreferencesManager.getInstance(applicationContext).getPreference(
                PreferencesManager.LANGUAGE_KEY, "English").first()

            val sharedPref = SharedPref.getInstance(applicationContext)
            val result = repository.getCurrentWeather(sharedPref.getLatitude(),sharedPref.getLongitude(),BuildConfig.appidSafe,units,selectedLanguage).first()
            showNotification(applicationContext,result)
        }
        return Result.success()
    }

}
