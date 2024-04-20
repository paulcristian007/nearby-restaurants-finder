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
import com.example.restaurants.core.Result
import com.example.restaurants.core.TAG
import com.example.restaurants.model.DirectionsResponse
import com.example.restaurants.model.Restaurant
import com.example.restaurants.remote.GoogleApi
import com.example.restaurants.remote.RestaurantServices
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.concurrent.CompletableFuture

class RestaurantsViewModel(private val service: RestaurantServices): ViewModel() {
    val uiState: StateFlow<Result<List<Restaurant>>> = service.restaurantsFlow
    //var uiState: Flow<Result<List<Restaurant>>> = service.getRestaurantsFlow()

    var photo by mutableStateOf("")
        private set
    var nextPage by mutableStateOf(false)
        private set
    init {
        Log.d(TAG, "init")
        //collectFlow()
    }

     fun collectFlow() {
        viewModelScope.launch {
            service.flooow.collect {
                Log.d(TAG, "collected $it")
            }
        }
    }

    fun reset() {
        service.reset()
    }

    fun loadImage(restaurant: Restaurant) {
        viewModelScope.launch {
            service.getPhoto(restaurant)
            photo = restaurant.name
        }
    }
    fun getAll(distance: Int, selectedMode: String, lat: Double, lng: Double) {
        viewModelScope.launch {
             nextPage = service.loadRestaurants(distance, selectedMode, lat, lng)
        }
        /*viewModelScope.launch {
            service.getRestaurantsFlow("$lat,$lng", distance, selectedMode).collect {

            }
        }*/
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