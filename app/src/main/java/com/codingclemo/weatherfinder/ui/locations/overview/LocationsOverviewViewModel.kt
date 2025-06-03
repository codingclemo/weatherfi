package com.codingclemo.weatherfinder.ui.locations.overview

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingclemo.weatherfinder.data.repository.LocationsRepository
import com.codingclemo.weatherfinder.data.repository.WeatherRepository
import com.codingclemo.weatherfinder.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "LocationsOverviewViewModel"

@HiltViewModel
class LocationsOverviewViewModel @Inject constructor(
    private val weatherRepo: WeatherRepository,
    private val locationsRepo: LocationsRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow(LocationsOverviewState())
    val state: StateFlow<LocationsOverviewState> = _state.asStateFlow()

    init {
        loadLocations(true)
    }

    fun handleAction(action: LocationsOverviewAction) {
        when (action) {
            is LocationsOverviewAction.PullToRefresh -> {
                loadLocations(false)
            }

            LocationsOverviewAction.LoadData -> {
                loadLocations(true)
            }
        }
    }

    private fun loadLocations(isInitialLoading: Boolean) {
        viewModelScope.launch(ioDispatcher) {
            try {
                if(isInitialLoading) {
                    _state.update { it.copy(isLoading = true, error = null) }
                } else {
                    _state.update { it.copy(isRefreshing = true, error = null) }
                }

                locationsRepo.getAllLocations().collect { locations ->
                    val locationWithWeatherList = mutableListOf<LocationAndWeather>()

                    for (location in locations) {
                        if (location.lat != null && location.lng != null) {
                            val weather = weatherRepo.getCurrentWeather(location.lat, location.lng)
                            locationWithWeatherList.add(LocationAndWeather(location, weather))
                        }
                    }

                    _state.update { it.copy(locationAndWeatherList = locationWithWeatherList, isLoading = false, isRefreshing = false) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false, isRefreshing = false) }
            }
        }
    }
} 