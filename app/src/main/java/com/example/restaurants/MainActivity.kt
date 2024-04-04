package com.example.restaurants

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.restaurants.core.Permissions
import com.example.restaurants.ui.restaurants.RestaurantsScreen
import com.example.restaurants.ui.theme.RestaurantsTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RestaurantsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    /*Permissions(
                        permissions = listOf(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ),
                        rationaleText = "Please allow app to use location (coarse or fine)",
                        dismissedText = "O noes! No location provider allowed!"
                    ) {
                    }*/
                    RestaurantsScreen()


                }
            }
        }
    }
}
