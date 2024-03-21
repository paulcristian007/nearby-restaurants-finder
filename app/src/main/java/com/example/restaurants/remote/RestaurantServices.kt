package com.example.restaurants.remote

import android.util.Log
import com.example.restaurants.model.Restaurant
import com.example.restaurants.core.Result
import com.example.restaurants.core.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.http.Query

class RestaurantServices(val googleApi: GoogleApi) {

    private var restaurants: List<Restaurant> = listOf()
    val restaurantsFlow: MutableStateFlow<Result<List<Restaurant>>> = MutableStateFlow(Result.Start)
    val nextPageFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var count = 0
    private var nextPage = false
    private var token: String? = null

    suspend fun getRestaurants(apiKey: String, location: String, radius: Int, keyword: String) {
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

    suspend fun getDistance(apiKey: String, origin: String, destination: String, mode: String, restaurant: Restaurant) {
        Log.d(TAG, "getDistance")
        withContext(Dispatchers.IO) {
            try {
                val response = googleApi.calculateDistance(apiKey, origin, destination, mode)
                val distance = response.routes.firstOrNull()?.legs?.firstOrNull()?.distance?.text
                restaurant.distance = distance
                count++
                if (count == restaurants.size) {
                    restaurantsFlow.emit(Result.Distances)
                    nextPageFlow.emit(nextPage)
                }
                Log.d(TAG, "${restaurant.name} ${restaurants.toString()}")
            }
            catch (e: Exception) {
                Log.d(TAG, e.message!!)
            }
        }
    }

    suspend fun sortRestaurants() {
        withContext(Dispatchers.Default) {
            restaurants = restaurants.sortedByDescending { it.rating }
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