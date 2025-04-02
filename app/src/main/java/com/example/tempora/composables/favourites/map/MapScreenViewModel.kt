package com.example.tempora.composables.favourites.map

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tempora.BuildConfig
import com.example.tempora.R
import com.example.tempora.composables.settings.PreferencesManager
import com.example.tempora.composables.settings.utils.SharedPref
import com.example.tempora.composables.settings.utils.getTemperatureUnit
import com.example.tempora.data.models.FavouriteLocation
import com.example.tempora.data.repository.Repository
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MapScreenViewModel(private val placesClient: PlacesClient,private val repository: Repository): ViewModel() {

    class MapScreenViewModelFactory(private val placesClient: PlacesClient,private val repository: Repository) : ViewModelProvider.Factory
    {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MapScreenViewModel(placesClient,repository) as T
        }
    }

    private val mutableSearchText = MutableStateFlow("")
    val searchText: StateFlow<String> = mutableSearchText

    private val mutablePredictions = MutableStateFlow<List<AutocompletePrediction>>(emptyList())
    val predictions: StateFlow<List<AutocompletePrediction>> = mutablePredictions

    private val mutableSelectedLocation = MutableStateFlow<Location?>(null)
    val selectedLocation: StateFlow<Location?> get() = mutableSelectedLocation

    private val mutableMessage = MutableSharedFlow<String>()
    val message = mutableMessage.asSharedFlow()

    //private val locationBias: LocationBias = RectangularBounds.newInstance(LatLng(39.9, -105.5), // SW lat, lng LatLng(40.1, -105.0)  // NE lat, lng)

    // Update search query and fetch predictions
    fun onSearchQueryChanged(query: String) {
        mutableSearchText.value = query
        fetchPredictions(query)
    }

    //Fetch place details when a prediction is selected
    fun onPlaceSelected(placeId: String) {
        fetchPlaceDetails(placeId)
        mutableSearchText.value = "" // Clear search text after selection
        mutablePredictions.value = emptyList() // Clear predictions
    }

    // Fetch autocomplete predictions
    private fun fetchPredictions(query: String) {
        if (query.isEmpty()) {
            mutablePredictions.value = emptyList() // Clear predictions when input is empty
            return
        }

        viewModelScope.launch {
            val request = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .build()

            placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener { response ->
                    mutablePredictions.value = response.autocompletePredictions
                }
                .addOnFailureListener { exception ->
                    Log.e("MapViewModel", "Error fetching predictions: ${exception.message}")
                }
        }
    }

    // Fetch place details
    fun fetchPlaceDetails(placeId: String) {
        val placeFields = listOf(Place.Field.LAT_LNG)
        val request = FetchPlaceRequest.builder(placeId, placeFields).build()
        placesClient.fetchPlace(request)
            .addOnSuccessListener { response ->
                response.place.latLng?.let { latLng ->
                    mutableSelectedLocation.value = Location("").apply {
                        latitude = latLng.latitude
                        longitude = latLng.longitude
                    }

                }
            }
            .addOnFailureListener { exception ->
                Log.e("MapViewModel", "Error fetching place details: ${exception.message}")
            }
    }

    fun updateSelectedLocation(latLng: LatLng) {
        mutableSelectedLocation.value = Location("").apply {
            latitude = latLng.latitude
            longitude = latLng.longitude
        }
    }

    fun insertFavouriteLocation(lat: Double, lon: Double, address: String, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val selectedUnit = PreferencesManager.getInstance(context).getPreference(PreferencesManager.TEMPERATURE_UNIT_KEY,"Kelvin °K").first()
            val units = getTemperatureUnit(selectedUnit)

            val selectedLanguage = PreferencesManager.getInstance(context).getPreference(PreferencesManager.LANGUAGE_KEY, "English").first()
            var language = if(selectedLanguage == "Arabic") "ar" else "en"

            try {
                val currentWeather = repository.getCurrentWeather(lat,lon,BuildConfig.appidSafe,units,language).first()
                val forecastWeather = repository.getForecastWeather(lat,lon,BuildConfig.appidSafe,units,language).first()
                val favouriteLocation = FavouriteLocation(latitude = lat, longitude = lon, country = address, currentWeather = currentWeather, forecastWeather = forecastWeather)
                repository.insertFavouriteLocation(favouriteLocation)
                mutableMessage.emit(context.getString(R.string.location_added_successfully))
            } catch (ex: Exception){
                mutableMessage.emit("Couldn't Add Location To Favourites ${ex.message}")
            }
        }
    }

    fun selectFavouriteLocation(lat: Double, lon: Double,context: Context) {
        val sharedPref = SharedPref.getInstance(context)
        sharedPref.setLatitude(lat)
        sharedPref.setLongitude(lon)
        sharedPref.setGpsSelected(false)
    }
}

