package com.example.tempora.composables.favourites

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tempora.R
import com.example.tempora.data.models.FavouriteLocation
import com.example.tempora.data.repository.Repository
import com.example.tempora.data.response_state.FavouriteLocationsResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavouritesScreenViewModel(private val repository: Repository): ViewModel() {

    class FavouritesScreenViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory
    {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FavouritesScreenViewModel(repository) as T
        }
    }

    private val mutableFavouriteLocations = MutableStateFlow<FavouriteLocationsResponseState>(FavouriteLocationsResponseState.Loading)
    val favouriteLocations = mutableFavouriteLocations.asStateFlow()

    private val mutableMessage = MutableSharedFlow<String>()
    val message = mutableMessage.asSharedFlow()

    private var deletedFavoriteLocation: FavouriteLocation? = null

    fun getAllFavouriteLocations(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.getAllFavouriteLocations()
                result
                    .catch {
                        ex -> mutableFavouriteLocations.value = FavouriteLocationsResponseState.Failed(ex)
                        mutableMessage.emit(ex.message.toString()) }
                    .collect {
                        mutableFavouriteLocations.value = FavouriteLocationsResponseState.Success(it)
                        Log.i("TAG", "getAllFavouriteLocations: $it")
                    }
            } catch (ex: Exception){
                mutableMessage.emit("An Error Occurred!, ${ex.message}")
            }
        }
    }

    fun deleteFavouriteLocation(favouriteLocation: FavouriteLocation,context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                deletedFavoriteLocation = favouriteLocation
                repository.deleteFavouriteLocation(favouriteLocation)
                mutableMessage.emit(context.getString(R.string.location_deleted_successfully))
            } catch (ex: Exception){
                mutableMessage.emit("Couldn't Delete Location From Favourites ${ex.message}")
            }
        }
    }

    fun restoreFavouriteLocation(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.insertFavouriteLocation(deletedFavoriteLocation!!)
                mutableMessage.emit(context.getString(R.string.location_restored_successfully))
            } catch (ex: Exception) {
                mutableMessage.emit("Couldn't Restore Location: ${ex.message}")
            }
        }
    }
}