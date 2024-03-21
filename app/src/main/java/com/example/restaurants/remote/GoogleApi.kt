package com.example.restaurants.remote

import com.example.restaurants.model.DirectionsResponse
import com.example.restaurants.model.Restaurant
import com.example.restaurants.model.RestaurantResult
import org.json.JSONObject
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
}