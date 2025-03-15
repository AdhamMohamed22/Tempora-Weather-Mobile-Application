package com.example.tempora.currentweather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tempora.data.models.CurrentWeather
import com.example.tempora.data.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CurrentWeatherViewModel(private val repository: Repository) : ViewModel() {

    class CurrentWeatherFactory(private val repository: Repository) : ViewModelProvider.Factory
    {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CurrentWeatherViewModel(repository) as T
        }
    }

    private val mutableCurrentWeather: MutableLiveData<CurrentWeather> = MutableLiveData()
    val currentWeather: LiveData<CurrentWeather> = mutableCurrentWeather

    private val mutableMessage: MutableLiveData<String> = MutableLiveData()
    val message: LiveData<String> = mutableMessage

    fun getCurrentWeather(){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val result = repository.getCurrentWeather()
                mutableCurrentWeather.postValue(result)
                Log.i("TAG", "getCurrentWeather: $result")
            } catch (ex: Exception){
                mutableMessage.postValue("An Error Occurred!, ${ex.message}")
            }
        }
    }

}