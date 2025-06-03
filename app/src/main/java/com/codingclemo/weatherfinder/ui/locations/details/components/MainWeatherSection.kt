package com.codingclemo.weatherfinder.ui.locations.details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.codingclemo.weatherfinder.ui.components.WeatherImage
import com.codingclemo.weatherfinder.ui.locations.details.LocationDetails
import com.codingclemo.weatherfinder.ui.locations.details.LocationDetailsState

@Composable
fun MainWeatherSection(
    state: LocationDetailsState,
    locationDetails: LocationDetails
) {
    val weatherText = if (state.selectedForecastIndex == 0) {
        locationDetails.currentWeather.description
    } else {
        locationDetails.weatherForecast.dailyForecasts[state.selectedForecastIndex].description
    }
    Text(
        text = weatherText.capitalize(),
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
    WeatherImage(
        iconCode = if (state.selectedForecastIndex == 0) locationDetails.currentWeather.iconCode else locationDetails.weatherForecast.dailyForecasts[state.selectedForecastIndex].iconCode,
    )

    val displayTemperature: Double = if (state.selectedForecastIndex == 0) {
        locationDetails.currentWeather.temperature
    } else {
        locationDetails.weatherForecast.dailyForecasts[state
            .selectedForecastIndex].averageTemperature
    }

    Column(
        modifier = Modifier
            .fillMaxHeight(0.5f)
    ) {
        MainTemperature(
            temperature = displayTemperature,
            showAverage = state.selectedForecastIndex != 0,
        )
    }
    val feelsLike = if (state.selectedForecastIndex == 0) {
        locationDetails.currentWeather.feelsLike
    } else {
        locationDetails.weatherForecast.dailyForecasts[state.selectedForecastIndex].feelsLike
    }

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "feels like ",
        )
        Text(
            text = "${feelsLike.toInt()}°",
            fontWeight = FontWeight.Bold
        )
    }

    Spacer(modifier = Modifier.height(24.dp))
}