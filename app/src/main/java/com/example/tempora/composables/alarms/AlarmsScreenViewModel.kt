package com.example.tempora.composables.alarms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tempora.data.repository.Repository

class AlarmsScreenViewModel(private val repository: Repository): ViewModel() {

    class AlarmsScreenViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory
    {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AlarmsScreenViewModel(repository) as T
        }
    }


}