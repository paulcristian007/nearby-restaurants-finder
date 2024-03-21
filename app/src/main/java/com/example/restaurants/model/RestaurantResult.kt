package com.example.restaurants.model

import com.google.gson.annotations.SerializedName

data class RestaurantResult(
    val results: List<Restaurant>,
    @SerializedName("next_page_token")
    val token: String? = null
    )