package com.codingclemo.weatherfinder.ui.locations.details


import com.codingclemo.weatherfinder.data.model.Location
import com.codingclemo.weatherfinder.domain.model.WeatherForecast
import com.codingclemo.weatherfinder.domain.model.Weather

data class LocationDetailsState(
    val locationDetails: LocationDetails? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRefreshing: Boolean = false,
    val selectedForecastIndex: Int = 0
)

data class LocationDetails(
    val location: Location,
    val currentWeather: Weather,
    val weatherForecast: WeatherForecast,
)

sealed class LocationDetailsAction {
    data object LoadData : LocationDetailsAction()
    data object RefreshData : LocationDetailsAction()
    data class SelectForecastDay(val index: Int) : LocationDetailsAction()
}