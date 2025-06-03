package com.codingclemo.weatherfinder.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.codingclemo.weatherfinder.ui.locations.details.LocationDetailsScreen
import com.codingclemo.weatherfinder.ui.locations.overview.LocationsOverviewScreen

@Composable
fun WeatherNavigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = "locations"
    ) {
        composable("locations") {
            LocationsOverviewScreen(
                onLocationClick = { locationId ->
                    navController.navigate("location_details/$locationId")
                }
            )
        }
        
        composable(
            route = "location_details/{locationId}",
            arguments = listOf(
                navArgument("locationId") {
                    type = NavType.StringType
                }
            )
        ) {
            LocationDetailsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
} 