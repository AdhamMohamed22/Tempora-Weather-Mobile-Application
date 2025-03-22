package com.example.tempora.composables.favourites

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.text.toSpannable
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.LocationBias
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.kotlin.awaitFindAutocompletePredictions
import com.google.android.libraries.places.compose.autocomplete.components.PlacesAutocompleteTextField
import com.google.android.libraries.places.compose.autocomplete.models.AutocompletePlace
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun MapScreen(context: Context) {

    Places.initializeWithNewPlacesApiEnabled(context, "AIzaSyCaj10hgcwGaosoYRyv79ppLviFJ9eMNmM")
    val placesClient = Places.createClient(context)

    val bias: LocationBias = RectangularBounds.newInstance(
        LatLng(39.9, -105.5), // SW lat, lng
        LatLng(40.1, -105.0)  // NE lat, lng
    )

    var searchText by remember { mutableStateOf("") }
    var predictions by remember { mutableStateOf(emptyList<AutocompletePrediction>()) }

    LaunchedEffect(searchText) {
        if (searchText.isNotEmpty()) {
            val response = placesClient.awaitFindAutocompletePredictions {
                locationBias = bias
                typesFilter = listOf(PlaceTypes.CITIES)
                query = searchText
            }
            predictions = response.autocompletePredictions
        }
    }

    var markerState = rememberMarkerState(position = LatLng(1.35, 103.87))
    val cameraPositionState = rememberCameraPositionState { position = CameraPosition.fromLatLngZoom(markerState.position, 10f) }

    val result = remember { mutableStateOf<AutocompletePlace?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(mapType = MapType.HYBRID),
            onMapClick = { latLng -> markerState.position = latLng }
        ) {
            Marker(
                state = markerState,
                title = "Singapore",
                snippet = "Marker in Singapore"
            )
        }

        PlacesAutocompleteTextField(
            modifier = Modifier.fillMaxWidth(),
            searchText = searchText,
            predictions = predictions.map {
                AutocompletePlace(
                    placeId = it.placeId,
                    primaryText = it.getPrimaryText(null).toSpannable(),
                    secondaryText = it.getSecondaryText(null).toSpannable()
                )
            },
            onQueryChanged = { searchText = it },
            onSelected = { autocompletePlace ->
                result.value = autocompletePlace
                searchText = autocompletePlace.primaryText.toString()  // Fix: Keep text in search bar
                markerState.position = LatLng(10.35, 103.87)
            },
            selectedPlace = result.value,
        )
    }
}

