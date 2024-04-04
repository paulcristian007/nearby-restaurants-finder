package com.example.restaurants.ui.restaurants

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

const val mainRoute = "main_route"
const val secondRoute = "second_route"
@Composable
fun MyNavHost() {
    val navController = rememberNavController()
    val onCloseMap = {
        Log.d("MyAppNavHost", "navigate back to main screen")
        navController.popBackStack()
    }

    val onClickLocation =  {
        Log.d("MyAppNavHost", "going to second screen")
        navController.navigate(secondRoute)
    }


    NavHost(
        navController = navController,
        startDestination = mainRoute
    ) {
        composable(route = mainRoute) {
            //ExamScreen(onClickLocation)
        }
        composable(route = secondRoute) {
            //SecondScreen(onCloseScreen = {onCloseMap()})
        }
    }
}