package com.example.restaurants.ui.restaurants

import com.example.restaurants.core.Result
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.restaurants.model.Restaurant

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RestaurantsScreen() {
    Log.d("RestaurantScreen", "recompose")
    val restaurantsViewModel = viewModel<RestaurantsViewModel>(factory = RestaurantsViewModel.Factory)
    val restaurantsState by restaurantsViewModel.uiState.collectAsStateWithLifecycle()
    var restaurants by rememberSaveable { mutableStateOf(listOf<Restaurant>()) }
    val nextPage by restaurantsViewModel.nextPageState.collectAsStateWithLifecycle()
    var distance by rememberSaveable { mutableStateOf("0") }
    val modes = listOf("walking", "driving", "cycling")
    var selectedMode by rememberSaveable { mutableStateOf("") }
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    Column {
        TextField(
            value = distance,
            onValueChange = { distance = it },
            label = { Text("Distance (m)") })
        ExposedDropdownMenuBox(expanded = isExpanded, onExpandedChange = {isExpanded = it}) {
            TextField(value = selectedMode, onValueChange = {}, readOnly = true, trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            })
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false}) {

                modes.forEach {mode ->
                    DropdownMenuItem(
                        onClick = {
                            isExpanded = false
                            selectedMode = mode
                        },
                    ) {
                        Text(mode)
                    }
                }
            }
        }


        Button(onClick = {
            restaurants = listOf()
            restaurantsViewModel.reset()
            restaurantsViewModel.loadRestaurants(distance.toInt(), selectedMode)
        }) {
            Text(text = "Search")
        }
        if (nextPage) {
            Button(onClick = {
                restaurantsViewModel.loadRestaurants(distance.toInt(), selectedMode)
                Log.d("RestaurantScreen", restaurants.toString())
            }) {
                Text(text = "Load more data")
            }
        }


        when (restaurantsState) {
            is Result.Start -> {
                Log.d("RestaurantScreen", "start")
            }
            is Result.Loading -> {
                Log.d("RestaurantScreen", "loading")
                CircularProgressIndicator()
            }

            is Result.Success -> {
                Log.d("RestaurantScreen", "success")
                CircularProgressIndicator()
                restaurantsViewModel.calcDistances((restaurantsState as Result.Success<List<Restaurant>>).data)
            }

            is Result.Distances -> {
                Log.d("RestaurantScreen", "distances")
                CircularProgressIndicator()
                restaurantsViewModel.sortRestaurants()
            }

            is Result.Sorted -> {
                Log.d("RestaurantScreen", "sorted $restaurants")
                restaurants = (restaurantsState as Result.Sorted<List<Restaurant>>).data
            }

            is Result.Error -> {
                Log.d("RestaurantScreen", "error")
                Text(text = (restaurantsState as Result.Error).exception!!.message!!)
            }
        }

        Column {
            RestaurantList(restaurants)
        }
    }
}


