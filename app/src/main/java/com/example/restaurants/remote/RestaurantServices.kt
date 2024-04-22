package com.example.restaurants.remote

import android.util.Log
import com.example.restaurants.core.Props
import com.example.restaurants.model.Restaurant
import com.example.restaurants.core.Result
import com.example.restaurants.core.TAG
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RestaurantServices(private val googleApi: GoogleApi, private val apiKey: String) {

    private var restaurants: List<Restaurant> = listOf()
    private val _restaurantsFlow: MutableStateFlow<Result<List<Restaurant>>> = MutableStateFlow(Result.Start)
    val restaurantsFlow: StateFlow<Result<List<Restaurant>>> = _restaurantsFlow.asStateFlow()
    private var nextPage = false
    private var token: String? = null

    private suspend fun getRestaurants(location: String, radius: Int, keyword: String) {
        Log.d(TAG, "get restaurants")
            try {
                _restaurantsFlow.value = Result.Loading
                nextPage = false
                val restaurantResponse = googleApi.getRestaurants(
                    apiKey, location, radius, "restaurants", keyword, token)
                if (restaurantResponse.token != null) {
                    nextPage = true
                    token = restaurantResponse.token
                }
                restaurants = restaurants.plus(restaurantResponse.results)
            }
            catch (e: Exception) {
                Log.d(TAG, e.message!!)
                _restaurantsFlow.value = Result.Error(e)
            }
    }

    private suspend fun getDistance(restaurant: Restaurant, destination: String, mode: String) {
        val origin =
            "${restaurant.geometry.location.lat},${restaurant.geometry.location.lng}"
        val response = googleApi.calculateDistance(apiKey, origin, destination, mode)
        val distance = response.routes.firstOrNull()?.legs?.firstOrNull()?.distance?.text
        restaurant.distance = distance
    }

    private suspend fun getDistances(destination: String, mode: String) {
        Log.d(TAG, "getDistances")

        coroutineScope {
            for (restaurant in restaurants)
                if (restaurant.distance == null)
                    launch {
                        getDistance(restaurant, destination, mode)
                        Log.d(TAG, Thread.currentThread().name)
                    }
        }
    }

    suspend fun getPhoto(restaurant: Restaurant)  {
        Log.d(TAG, "get photo ${restaurant.name}")
        if (restaurant.photos != null && !restaurant.startedDownloading) {
            restaurant.startedDownloading = true
            val photoUrl = Props.url
                .replace("{ref}", restaurant.photos[0].photo_reference)
                .replace("{key}",  Props.key)

            withContext(Dispatchers.IO) {
                try {
                    restaurant.displayImage = Picasso.get().load(photoUrl).get()
                }
                catch (e: Exception) {
                    e.message?.let { Log.d(TAG, it) }
                }
            }
        }
    }

    private suspend fun sortRestaurants(distance: Double) {
        withContext(Dispatchers.Default) {
            var filteredRestaurants = listOf<Restaurant>()
            for (restaurant in restaurants)
                if (restaurant.distance != null) {
                    val dist = restaurant.distance!!.split(" ")
                    if (dist[0].toDouble() <= distance)
                        filteredRestaurants = filteredRestaurants.plus(restaurant)
                }
            restaurants = filteredRestaurants.sortedByDescending { it.rating }
            _restaurantsFlow.value = Result.Success(restaurants)
        }
    }

    suspend fun loadRestaurants(distance: Int, selectedMode: String, lat: Double, lng: Double): Boolean {
        val location = "$lat,$lng"
        getRestaurants(location, distance, selectedMode)
        getDistances(location, selectedMode)
        sortRestaurants(distance / 1000.0)
        return nextPage
    }

    fun reset() {
        restaurants = listOf()
        nextPage = false
        token = null
    }
}