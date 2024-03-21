package com.example.restaurants.model

data class DirectionsResponse(
    // Define the structure of the JSON response
    val routes: List<Route>
) {
    data class Route(
        val legs: List<Leg>
    ) {
        data class Leg(
            val distance: Distance
        ) {
            data class Distance(
                val text: String // Distance in text format (e.g., "10.5 km")
            )
        }
    }
}