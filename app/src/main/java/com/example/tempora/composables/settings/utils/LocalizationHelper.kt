package com.example.tempora.composables.settings.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

object LocalizationHelper {

    fun setLocale(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)

        config.setLocale(locale)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.createConfigurationContext(config)
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
        } else {
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
        }
    }
}