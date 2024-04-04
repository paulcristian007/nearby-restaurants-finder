package com.example.restaurants.ui.restaurants

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restaurants.R
import com.example.restaurants.model.Restaurant
import com.google.maps.android.compose.GoogleMap

@Composable
fun RestaurantList(restaurants: List<Restaurant>, onDisplay: (Restaurant) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        items(restaurants) { restaurant ->
            RestaurantDetail(restaurant, onDisplay)
        }
    }
}

@Composable
fun RestaurantDetail(restaurant: Restaurant, onDisplay: (Restaurant) -> Unit) {
    Log.d("RestaurantDetail", "on display $restaurant")
    if (restaurant.distance != null) {
        Row {
                Column {
                    if (restaurant.map != null) {
                        Image(
                        bitmap = restaurant.map!!.asImageBitmap(),
                        contentDescription = "Bitmap Image"
                        )
                    }

                    else {
                        val imageResId = R.drawable.blank_map // Replace "your_image_filename" with the actual filename of your image resource

                        Image(
                            painter = painterResource(id = imageResId),
                            contentDescription = null, // Provide a content description if the image is meaningful for accessibility
                            modifier = Modifier.fillMaxWidth(0.2f)
                        )
                        onDisplay(restaurant)
                    }
            }


            Column {
                Row {
                        ClickableText(text = AnnotatedString(restaurant.name),
                            style = TextStyle(
                                fontSize = 24.sp,
                            ),
                            onClick = {}
                        )
                }
                Row {
                    ClickableText(text = AnnotatedString(restaurant.distance.toString()),
                        style = TextStyle(
                            fontSize = 24.sp,
                        ),
                        onClick = {}
                    )
                }
                Row {
                        ClickableText(text = AnnotatedString("Rating: ${restaurant.rating} (${restaurant.count})"),
                            style = TextStyle(
                                fontSize = 24.sp,
                            ),
                            onClick = {}
                        )
                }
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
    }
}