package com.example.restaurants.model

import com.google.gson.annotations.SerializedName

data class Restaurant(
    val name: String,
    val geometry: Geometry,
    val rating: Double,
    @SerializedName("user_ratings_total")
    val count: Int,
    var distance: String? = null
)