package com.codingclemo.weatherfinder.ui.locations.overview

import com.codingclemo.weatherfinder.data.model.Location
import com.codingclemo.weatherfinder.domain.model.Weather

data class LocationsOverviewState(
    val locationAndWeatherList: List<LocationAndWeather> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null
)

data class LocationAndWeather(
    val location: Location,
    val weather: Weather
)

sealed interface LocationsOverviewAction {
    data object PullToRefresh: LocationsOverviewAction
    data object LoadData : LocationsOverviewAction
} 