package com.example.restaurants.remote

import com.example.restaurants.model.DirectionsResponse
import com.example.restaurants.model.Restaurant
import com.example.restaurants.model.RestaurantResult
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleApi {

    @GET("place/nearbysearch/json")
    suspend fun getRestaurants(
        @Query("key")apiKey: String,
        @Query("location")location: String,
        @Query("radius")radius: Int,
        @Query("keyword")keyword: String,
        @Query("mode")mode: String,
        @Query("pagetoken")token: String? = null
    ): RestaurantResult

    @GET("directions/json")
    suspend fun calculateDistance(
        @Query("key")apiKey: String,
        @Query("origin")origin: String,
        @Query("destination")destination: String,
        @Query("mode")mode: String,
    ): DirectionsResponse

    @GET("/maps/api/place/photo")
    suspend fun getRestaurantPhoto(
        @Query("maxwidth") maxWidth: Int,
        @Query("photoreference") photoReference: String,
        @Query("key") apiKey: String
    ): String
}