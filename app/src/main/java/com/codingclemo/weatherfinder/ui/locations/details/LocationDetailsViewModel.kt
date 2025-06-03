package com.codingclemo.weatherfinder.ui.locations.details

import android.net.ConnectivityManager
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingclemo.weatherfinder.data.repository.LocationsRepository
import com.codingclemo.weatherfinder.data.repository.WeatherRepository
import com.codingclemo.weatherfinder.util.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val locationsRepo: LocationsRepository,
    private val weatherRepo: WeatherRepository,
) : ViewModel() {

    private val locationId: String = checkNotNull(savedStateHandle["locationId"])

    private val _state = MutableStateFlow(LocationDetailsState())
    val state: StateFlow<LocationDetailsState> = _state.asStateFlow()

    init {
        loadLocationDetails(true)
    }

    fun handleAction(action: LocationDetailsAction) {
        when (action) {
            is LocationDetailsAction.LoadData -> loadLocationDetails(true)
            is LocationDetailsAction.RefreshData -> loadLocationDetails(false)
            is LocationDetailsAction.SelectForecastDay -> selectForecastDay(action.index)
        }
    }

    private fun loadLocationDetails(isInitialLoading: Boolean = true) {
        viewModelScope.launch {
            if (isInitialLoading) {
                _state.update { it.copy(isLoading = true, error = null) }
            } else {
                _state.update { it.copy(isRefreshing = true, error = null) }
            }
            try {
                locationsRepo.getLocationById(locationId).let { location ->
                    if (location?.lat != null && location.lng != null) {
                        coroutineScope {
                            val currentWeatherDeferred = async {
                                weatherRepo.getCurrentWeather(location.lat, location.lng)
                            }
                            val forecastDeferred = async {
                                weatherRepo.getWeatherForecast(location.lat, location.lng)
                            }

                            val forecast = forecastDeferred.await()
                            val currentWeather = currentWeatherDeferred.await()

                            //** end new**//
                            _state.update {
                                it.copy(
                                    locationDetails = LocationDetails(
                                        location = location,
                                        currentWeather = currentWeather,
                                        weatherForecast = forecast,
                                    ),
                                    isLoading = false,
                                    isRefreshing = false,
                                )
                            }
                        }
                    } else {
                        _state.update {
                            it.copy(
                                error = "Location coordinates not available",
                                isLoading = false,
                                isRefreshing = false,
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = e.message,
                        isLoading = false,
                        isRefreshing = false,
                    )
                }
            }
        }
    }

    private fun selectForecastDay(index: Int) {
        _state.update { it.copy(selectedForecastIndex = index) }
    }
} 