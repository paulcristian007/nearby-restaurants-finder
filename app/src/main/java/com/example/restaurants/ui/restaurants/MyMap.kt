package com.example.restaurants.ui.restaurants

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*


@Composable
fun MyMap() {
    val mapsViewModel = viewModel<MapsViewModel>(factory = MapsViewModel.Factory)
    var lat by rememberSaveable { mutableStateOf(mapsViewModel.lat) }
    var lng by rememberSaveable { mutableStateOf(mapsViewModel.lng) }

    val markerState = rememberMarkerState(position = LatLng(lat, lng))
    var mapWidth by rememberSaveable { mutableStateOf(1f) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(markerState.position, 10f)
    }

    GoogleMap(
                modifier = Modifier.fillMaxSize(mapWidth),
                cameraPositionState = cameraPositionState,
                onMapClick = {
                    Log.d("MyMap", "onMapClick $it")
                    mapWidth = 1f
                },
                onMapLongClick = {
                    Log.d("MyMap", "onMapLongClick $it")
                    markerState.position = it
                    lat = it.latitude
                    mapsViewModel.lat = it.latitude
                    lng = it.longitude
                    mapsViewModel.lng = it.longitude
                },
            ) {
                Marker(
                    state = MarkerState(position = markerState.position),
                    title = "User location title",
                    snippet = "User location",
                )
        }
}
