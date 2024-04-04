package com.example.restaurants.remote

import android.util.Log
import com.example.restaurants.model.Restaurant
import com.example.restaurants.core.Result
import com.example.restaurants.core.TAG
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class RestaurantServices(private val googleApi: GoogleApi, private val apiKey: String) {

    private var restaurants: List<Restaurant> = listOf()
    val restaurantsFlow: MutableStateFlow<Result<List<Restaurant>>> = MutableStateFlow(Result.Start)
    val nextPageFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val photosFlow: MutableStateFlow<String> = MutableStateFlow("")
    private var count = 0
    private var nextPage = false
    private var token: String? = null

    suspend fun getRestaurants(location: String, radius: Int, keyword: String) {
        Log.d(TAG, "get restaurants")
        withContext(Dispatchers.IO) {
            try {
                restaurantsFlow.emit(Result.Loading)
                nextPage = false
                nextPageFlow.emit(false)
                Log.d(TAG, "switched context")
                val restaurantResponse = googleApi.getRestaurants(apiKey, location, radius, "restaurants", keyword, token)
                if (restaurantResponse.token != null) {
                    nextPage = true
                    token = restaurantResponse.token
                }

                restaurants = restaurants.plus(restaurantResponse.results)
                restaurantsFlow.emit(Result.Success(restaurants))
            } catch (e: HttpException) {
                Log.d(TAG, e.message!!)
                restaurantsFlow.emit(Result.Error(e))
            }
            catch (e: Exception) {
                restaurantsFlow.emit(Result.Error(e))
                restaurantsFlow.value
            }
        }
    }

    suspend fun getDistance(origin: String, destination: String, mode: String, restaurant: Restaurant) {
        Log.d(TAG, "getDistance")
        withContext(Dispatchers.IO) {
            try {
                val response = googleApi.calculateDistance(apiKey, origin, destination, mode)
                val distance = response.routes.firstOrNull()?.legs?.firstOrNull()?.distance?.text
                restaurant.distance = distance
                count++
                Log.d(TAG, "$count ${restaurants.size}")
                if (count == restaurants.size) {
                    restaurantsFlow.emit(Result.Distances)
                    nextPageFlow.emit(nextPage)
                }
                Log.d(TAG, "${restaurant.name} $restaurants")
            }
            catch (e: Exception) {
                Log.d(TAG, e.message!!)
            }
        }
    }

    suspend fun getPhoto(restaurant: Restaurant) {
        if (restaurant.photos != null) {
            val photoUrl = "https://maps.googleapis.com/maps/api/place/photo" +
                    "?maxwidth=200" +
                    "&photoreference=${restaurant.photos[0].photo_reference}" +
                    "&key=$apiKey"

            withContext(Dispatchers.IO) {
                try {
                    Log.d(TAG, "prepare fetch")
                    restaurant.map = Picasso.get().load(photoUrl).get()
                    Log.d(TAG, "downloaded ${restaurant.name}")
                    photosFlow.emit(restaurant.name)
                    //restaurantsFlow.emit(Result.Photo(restaurants))
                } catch (_: Exception) {

                }
            }
        }
    }

    suspend fun sortRestaurants(distance: Double) {
        withContext(Dispatchers.Default) {
            var filteredRestaurants = listOf<Restaurant>()
            for (restaurant in restaurants)
                if (restaurant.distance != null) {
                    val dist = restaurant.distance!!.split(" ")
                    if (dist[0].toDouble() <= distance)
                        filteredRestaurants = filteredRestaurants.plus(restaurant)
                }
            restaurants = filteredRestaurants.sortedByDescending { it.rating }
            count = restaurants.size
            Log.d(TAG, "post filter $count ${restaurants.size}")
            restaurantsFlow.emit(Result.Sorted(restaurants))

        }
    }

    fun reset() {
        restaurants = listOf()
        count = 0
        nextPage = false
        token = null
    }
}