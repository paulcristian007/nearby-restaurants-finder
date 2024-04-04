package com.example.restaurants.ui.restaurants

import com.example.restaurants.core.Result
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.restaurants.model.Restaurant

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RestaurantsScreen() {
    val restaurantsViewModel = viewModel<RestaurantsViewModel>(factory = RestaurantsViewModel.Factory)
    val mapsViewModel = viewModel<MapsViewModel>(factory = MapsViewModel.Factory)
    val restaurantsState by restaurantsViewModel.uiState.collectAsStateWithLifecycle()
    var restaurants by rememberSaveable { mutableStateOf(listOf<Restaurant>()) }
    val nextPage by restaurantsViewModel.nextPageState.collectAsStateWithLifecycle()
    val photo by restaurantsViewModel.photoState.collectAsStateWithLifecycle()
    val lat = mapsViewModel.lat
    val lng = mapsViewModel.lng
    var mapDisplayed by rememberSaveable { mutableStateOf(false) }

    Log.d("RestaurantScreen", "recompose $photo $nextPage $restaurantsState")

    var distance by rememberSaveable { mutableStateOf("0") }
    val modes = listOf("walking", "driving", "cycling")
    var selectedMode by rememberSaveable { mutableStateOf("") }
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    val onDisplay = {restaurant: Restaurant ->
        restaurantsViewModel.loadImage(restaurant)
    }

    if (mapDisplayed) {
        Row {
            MyMap()
        }
        Row {
            Button(onClick = { mapDisplayed = false }) {
                Text(text = "Save location")
            }
        }
    }

    else {

    Column {
        Row {
            Column {
                TextField(
                    value = distance,
                    onValueChange = { distance = it },
                    label = { Text("Distance (m)") })
                ExposedDropdownMenuBox(
                    expanded = isExpanded,
                    onExpandedChange = { isExpanded = it }) {
                    TextField(
                        value = selectedMode,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                        })
                    ExposedDropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false }) {

                        modes.forEach { mode ->
                            DropdownMenuItem(
                                onClick = {
                                    isExpanded = false
                                    selectedMode = mode
                                }
                            ) {
                                Text(mode)
                            }
                        }
                    }
                }
            }
        }

        Row {
            ClickableText(text = AnnotatedString("$lat, $lng"), onClick = {
                mapDisplayed = true
            })
        }

        Button(onClick = {
            restaurants = listOf()
            restaurantsViewModel.reset()
            restaurantsViewModel.loadRestaurants(distance.toInt(), selectedMode, lat, lng)
        }) {
            Text(text = "Search")
        }
        /*Button(onClick = {
            restaurantsViewModel.loadImage(restaurants[0])
        }) {
            Text(text = "Load Image")
        }*/
        if (nextPage) {
            Button(onClick = {
                restaurantsViewModel.loadRestaurants(distance.toInt(), selectedMode, lat, lng)
                Log.d("RestaurantScreen", restaurants.toString())
            }) {
                Text(text = "Load more data")
            }
        }


        when (restaurantsState) {
            is Result.Start -> {
            }
            is Result.Loading -> {
                CircularProgressIndicator()
            }

            is Result.Success -> {
                CircularProgressIndicator()
                restaurantsViewModel.calcDistances((restaurantsState as Result.Success<List<Restaurant>>).data, lat, lng)
            }

            is Result.Distances -> {
                CircularProgressIndicator()
                restaurantsViewModel.sortRestaurants(distance.toDouble() / 1000.0)
            }

            is Result.Sorted -> {
                restaurants = (restaurantsState as Result.Sorted<List<Restaurant>>).data
            }

            is Result.Error -> {
                Text(text = (restaurantsState as Result.Error).exception!!.message!!)
            }

            is Result.Photo -> {
                restaurants = (restaurantsState as Result.Photo<List<Restaurant>>).data
            }
        }

        Column {
            RestaurantList(restaurants, onDisplay)
        }
    }}
}


