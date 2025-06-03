package com.codingclemo.weatherfinder.ui.locations.overview

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.codingclemo.weatherfinder.R
import com.codingclemo.weatherfinder.domain.model.Weather
import com.codingclemo.weatherfinder.ui.components.ErrorDialog
import com.codingclemo.weatherfinder.ui.components.WeatherImage
import com.codingclemo.weatherfinder.ui.theme.WeatherFinderTheme
import com.codingclemo.weatherfinder.util.Fixtures
import kotlinx.coroutines.delay
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsOverviewScreen(
    onLocationClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LocationsOverviewViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = { viewModel.handleAction(LocationsOverviewAction.PullToRefresh) },
    ) {
        Content(
            state = state,
            onLocationClick = { locationId ->
                onLocationClick(locationId)
            },
            onRefresh = { viewModel.handleAction(LocationsOverviewAction.LoadData) },
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    state: LocationsOverviewState,
    onLocationClick: (String) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isPreview: Boolean = LocalInspectionMode.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        contentScale = ContentScale.FillHeight
                    )
                },
                actions = {}
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                state.error != null -> {
                    ErrorDialog(onRefresh = onRefresh)
                }

                else -> {
                    Column {
                        Text(
                            text = stringResource(R.string.locations_title).toUpperCase(Locale.getDefault()),
                            modifier = Modifier.padding(start = 24.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.ExtraBold
                        )

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            itemsIndexed(state.locationAndWeatherList) { index, locationAndWeather ->
                                var visible by remember { mutableStateOf(false) }

                                LaunchedEffect(Unit) {
                                    delay(index * 100L) // Staggered delay
                                    visible = true
                                }

                                AnimatedVisibility(
                                    visible = if (isPreview) true else visible,
                                    enter = fadeIn(animationSpec = tween(700))
                                ) {
                                    LocationItem(
                                        locationAndWeather = locationAndWeather,
                                        onClick = { onLocationClick(locationAndWeather.location.id) },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocationItem(
    locationAndWeather: LocationAndWeather,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.onPrimaryContainer),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable(
                    enabled = true,
                ) {
                    onClick()
                }
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 2.dp, horizontal = 8.dp)
                    .background(color = Color.Transparent)
            ) {
                WeatherImage(
                    iconCode = locationAndWeather.weather.iconCode,
                    modifier = Modifier.size(64.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 8.dp)
            ) {
                Text(
                    text = locationAndWeather.location.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(4.dp))
                TempertaureAndDescription(weather = locationAndWeather.weather)
            }
        }
    }
}

@Composable
private fun TempertaureAndDescription(weather: Weather) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${weather.temperature.toInt()}°",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Text(
            text = "-",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        Text(
            text = weather.description.capitalize(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview()
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LocationItemPreview() {
    WeatherFinderTheme {
        LocationItem(
            locationAndWeather =
                LocationAndWeather(
                    location = Fixtures.createLocation(),
                    weather = Fixtures.createWeather()
                ),
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ContentPreview() {
    WeatherFinderTheme {
        Content(
            state = LocationsOverviewState(
                locationAndWeatherList = listOf(
                    LocationAndWeather(
                        location = Fixtures.createLocation(),
                        weather = Fixtures.createWeather()
                    ),
                    LocationAndWeather(
                        location = Fixtures.createLocation(),
                        weather = Fixtures.createWeather()
                    )
                )
            ),
            onLocationClick = {},
            onRefresh = {}
        )
    }
} 