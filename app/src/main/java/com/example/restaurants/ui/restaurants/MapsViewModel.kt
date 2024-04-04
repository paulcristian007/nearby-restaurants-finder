package com.example.restaurants.ui.restaurants

import android.location.Location
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.restaurants.RestaurantsApplication
import com.example.restaurants.core.LocationMonitor
import com.example.restaurants.core.TAG
import kotlinx.coroutines.launch

class MapsViewModel(val application: RestaurantsApplication) : ViewModel() {
    var lat = 45.882189
    var lng = 22.908367
    var uiState by mutableStateOf<Location?>(null)
        private set

    init {
        Log.d(TAG, "init")
        collectLocation()
    }

    private fun collectLocation() {
        /*viewModelScope.launch {
            LocationMonitor(application).currentLocation.collect {
                Log.d(TAG, "collect $it")
                uiState = it
                lat = it.latitude
                lng = it.longitude
            }
        }*/
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RestaurantsApplication)
                MapsViewModel(app)
            }
        }
    }
}