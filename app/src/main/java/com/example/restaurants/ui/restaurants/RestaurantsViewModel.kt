package com.example.restaurants.ui.restaurants

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.restaurants.RestaurantsApplication
import com.example.restaurants.core.Result
import com.example.restaurants.core.TAG
import com.example.restaurants.model.DirectionsResponse
import com.example.restaurants.model.Restaurant
import com.example.restaurants.remote.GoogleApi
import com.example.restaurants.remote.RestaurantServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class RestaurantsViewModel(private val service: RestaurantServices): ViewModel() {
    val uiState: MutableStateFlow<Result<List<Restaurant>>> = service.restaurantsFlow
    val nextPageState: MutableStateFlow<Boolean> = service.nextPageFlow

    init {
        Log.d(TAG, "init")
    }

    fun loadRestaurants(distance: Int, selectedMode: String) {
        viewModelScope.launch {
            val apiKey = "AIzaSyCHsimVHnZfJespUMOWdKqBCLOfcQFwFKg"
            val location = "45.882189,22.908367"  // Example: Latitude,Longitude
            service.getRestaurants(apiKey, location, distance, selectedMode)
        }
    }

    fun calcDistances(restaurants: List<Restaurant>) {
        for (restaurant in restaurants)
            if (restaurant.distance == null) {
                viewModelScope.launch {
                    val apiKey = "AIzaSyCHsimVHnZfJespUMOWdKqBCLOfcQFwFKg"
                    val origin =
                        "${restaurant.geometry.location.lat},${restaurant.geometry.location.lng}"
                    val destination = "45.882189,22.908367"
                    val mode = "walking"
                    service.getDistance(apiKey, origin, destination, mode, restaurant)
                }
            }
    }

    fun sortRestaurants() {
        viewModelScope.launch {
            service.sortRestaurants()
        }
    }

    fun reset() {
        service.reset()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RestaurantsApplication)
                RestaurantsViewModel(app.container.restaurantService)
            }
        }
    }
}