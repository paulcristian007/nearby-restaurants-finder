package com.example.restaurants

import android.app.Application
import android.util.Log
import com.example.restaurants.core.TAG

class RestaurantsApplication : Application() {
    lateinit var container: AppContainer


    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "init")
        container = AppContainer()
    }
}