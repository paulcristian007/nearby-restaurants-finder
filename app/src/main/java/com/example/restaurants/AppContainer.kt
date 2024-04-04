package com.example.restaurants

import android.util.Log
import com.example.restaurants.core.Api
import com.example.restaurants.core.Props
import com.example.restaurants.core.TAG
import com.example.restaurants.remote.GoogleApi
import com.example.restaurants.remote.RestaurantServices

class AppContainer {

    init {
        Log.d(TAG, "init")
    }

    private val api: GoogleApi = Api.retrofit.create(GoogleApi::class.java)
    val restaurantService: RestaurantServices = RestaurantServices(api, Props.key)
}