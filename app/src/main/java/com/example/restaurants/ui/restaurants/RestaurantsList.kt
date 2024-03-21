package com.example.restaurants.ui.restaurants

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restaurants.model.Restaurant

@Composable
fun RestaurantList(restaurants: List<Restaurant>) {
    Log.d("StudentsList", "recompose")
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        items(restaurants) { restaurant ->
            RestaurantDetail(restaurant)
        }
    }
}

@Composable
fun RestaurantDetail(restaurant: Restaurant) {
    if (restaurant.distance != null) {
        Row {
            Column {
                ClickableText(text = AnnotatedString(restaurant.name),
                    style = TextStyle(
                        fontSize = 24.sp,
                    ),
                    onClick = {}
                )
            }
        }
        Row {
            Column {
                ClickableText(text = AnnotatedString(restaurant.distance.toString()),
                    style = TextStyle(
                        fontSize = 24.sp,
                    ),
                    onClick = {}
                )
            }
            Column {
                ClickableText(text = AnnotatedString("Rating: ${restaurant.rating} (${restaurant.count})"),
                    style = TextStyle(
                        fontSize = 24.sp,
                    ),
                    onClick = {}
                )
            }
        }
    }
}