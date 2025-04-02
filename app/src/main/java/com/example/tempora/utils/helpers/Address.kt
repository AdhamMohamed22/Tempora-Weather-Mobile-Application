package com.example.tempora.utils.helpers

import android.location.Geocoder
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

@Composable
fun getAddressFromLocation(latitude: Double,longitude: Double): String {
    val geocoder = Geocoder(LocalContext.current, Locale.getDefault())
    return try {
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        if (!addresses.isNullOrEmpty()) {
            val address = addresses[0]
            address.getCountryName()
        } else {
            "Address Not Found !"
        }
    } catch (e: Exception) {
        e.printStackTrace()
        "Error Fetching Address"
    }
}
