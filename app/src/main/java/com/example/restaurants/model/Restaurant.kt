package com.example.restaurants.model

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName

data class Restaurant(
    val name: String,
    val geometry: Geometry,
    val rating: Double,
    @SerializedName("user_ratings_total")
    val count: Int,
    var distance: String? = null,
    val photos: List<Photo>?,
    var map: Bitmap?
)