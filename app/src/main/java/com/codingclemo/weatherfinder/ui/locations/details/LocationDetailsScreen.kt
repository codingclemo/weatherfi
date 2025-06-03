package com.codingclemo.weatherfinder.ui.locations.details

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.codingclemo.weatherfinder.R
import com.codingclemo.weatherfinder.ui.components.DynamicHeightTopBar
import com.codingclemo.weatherfinder.ui.components.ErrorDialog
import com.codingclemo.weatherfinder.ui.locations.details.components.DailyForecastList
import com.codingclemo.weatherfinder.ui.locations.details.components.HourlyForecastList
import com.codingclemo.weatherfinder.ui.locations.details.components.MainWeatherSection
import com.codingclemo.weatherfinder.ui.locations.details.components.WeatherDetails
import com.codingclemo.weatherfinder.ui.theme.WeatherFinderTheme
import com.codingclemo.weatherfinder.util.Fixtures
import com.codingclemo.weatherfinder.util.Formatter.formatTimestamp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDetailsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LocationDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = {
            viewModel.handleAction(LocationDetailsAction.RefreshData)
        }
    ) {
        Content(
            state = state,
            onForecastSelected = { index -> viewModel.handleAction(LocationDetailsAction.SelectForecastDay(index)) },
            onNavigateBack = onNavigateBack,
            onRefresh = { viewModel.handleAction(LocationDetailsAction.RefreshData) },
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    state: LocationDetailsState,
    onForecastSelected: (Int) -> Unit,
    onNavigateBack: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            DynamicHeightTopBar(
                title = {
                    LocationAndDateInfo(
                        locationName = state.locationDetails?.location?.name,
                        timestamp = state.locationDetails?.weatherForecast?.dailyForecasts?.get(
                            state
                                .selectedForecastIndex
                        )?.timestamp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back_button_content_description))
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val alpha: Float by animateFloatAsState(
                targetValue = if (!state.isLoading) 1f else 0f,
                animationSpec = tween(durationMillis = 1500)
            )

            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                state.error != null -> {
                    ErrorDialog(onRefresh)
                }

                state.locationDetails != null -> {
                    ContentLoaded(alpha, state.locationDetails, state, onForecastSelected)
                }
            }
        }
    }
}

@Composable
private fun ContentLoaded(
    alpha: Float,
    locationDetails: LocationDetails,
    state: LocationDetailsState,
    onForecastSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .alpha(alpha)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                MainWeatherSection(state, locationDetails)
            }
            item {
                WeatherDetailsSection(locationDetails, state)
            }
            item {
                HourlyForecastList(
                    hourlyForecasts = if (state.selectedForecastIndex == 0) {
                        locationDetails.weatherForecast.dailyForecasts.first().hourlyForecasts
                    } else {
                        locationDetails.weatherForecast.dailyForecasts[state.selectedForecastIndex].hourlyForecasts
                    }
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            DailyForecastList(
                dailyForecasts = locationDetails.weatherForecast.dailyForecasts,
                selectedIndex = state.selectedForecastIndex,
                onForecastSelected = onForecastSelected
            )
        }
    }
}


@Composable
private fun LocationAndDateInfo(
    locationName: String?,
    timestamp: Long?,
) {

    val visible = locationName != null && timestamp != null
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "LocationAndDateInfoAlpha"
    )
    if (visible) {
        Column(
            modifier = Modifier.alpha(alpha),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = locationName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = formatTimestamp(timestamp, "EEEE, d MMM"),
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 2.dp)
            )
        }
    }
}

@Composable
private fun WeatherDetailsSection(
    locationDetails: LocationDetails,
    state: LocationDetailsState
) {
    var windSpeed: Double? = null
    var windDirection: Int? = null
    var humidity: Int? = null
    val precipitation = locationDetails.weatherForecast.dailyForecasts[state.selectedForecastIndex]
        .avgProbabilityOfPrecipitation

    if (state.selectedForecastIndex == 0) {
        windSpeed = locationDetails.currentWeather.windSpeed
        windDirection = locationDetails.currentWeather.windDirection
        humidity = locationDetails.currentWeather.humidity
    } else {
        windSpeed =
            locationDetails.weatherForecast.dailyForecasts[state.selectedForecastIndex].maxWindSpeed
        windDirection =
            locationDetails.weatherForecast.dailyForecasts[state.selectedForecastIndex].windDirection
        humidity =
            locationDetails.weatherForecast.dailyForecasts[state.selectedForecastIndex].humidity
    }

    WeatherDetails(
        windSpeed = windSpeed,
        windDirection = windDirection,
        humidity = humidity,
        precipitation = precipitation
    )

    Spacer(modifier = Modifier.height(24.dp))
}

@Preview
@Composable
private fun ContentPreview() {
    WeatherFinderTheme {
        Content(
            state = LocationDetailsState(
                locationDetails = LocationDetails(
                    location = Fixtures.createLocation(),
                    currentWeather = Fixtures.createWeather(),
                    weatherForecast = Fixtures.createWeatherForecast()
                )
            ),
            onForecastSelected = {},
            onNavigateBack = {},
            onRefresh = {}
        )
    }
}

@Preview
@Composable
private fun ContentForecastPreview() {
    WeatherFinderTheme {
        Content(
            state = LocationDetailsState(
                locationDetails = LocationDetails(
                    location = Fixtures.createLocation(),
                    currentWeather = Fixtures.createWeather(),
                    weatherForecast = Fixtures.createWeatherForecast()
                ),
                selectedForecastIndex = 1,
            ),
            onForecastSelected = {},
            onNavigateBack = {},
            onRefresh = {}
        )
    }
}