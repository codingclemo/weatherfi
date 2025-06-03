package com.codingclemo.weatherfinder.ui.locations.details

import app.cash.turbine.test
import com.codingclemo.weatherfinder.data.model.Location
import com.codingclemo.weatherfinder.data.repository.LocationsRepository
import com.codingclemo.weatherfinder.data.repository.WeatherRepository
import com.codingclemo.weatherfinder.domain.model.Weather
import com.codingclemo.weatherfinder.domain.model.WeatherForecast
import com.codingclemo.weatherfinder.util.Fixtures
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import androidx.lifecycle.SavedStateHandle

class LocationDetailsViewModelTest {
    private lateinit var viewModel: LocationDetailsViewModel
    private lateinit var locationsRepo: TestLocationsRepository
    private lateinit var weatherRepo: TestWeatherRepository
    private val testDispatcher = StandardTestDispatcher()
    private val locationId = "1"

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        locationsRepo = TestLocationsRepository()
        weatherRepo = TestWeatherRepository()
        viewModel = LocationDetailsViewModel(
            savedStateHandle = SavedStateHandle(mapOf("locationId" to locationId)),
            locationsRepo = locationsRepo,
            weatherRepo = weatherRepo
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has no location details and no loading state`() = runTest {
        // Given
        val location = Location(locationId, "Linz", "AT", 48.3069, 14.2858)
        val weather = Fixtures.createWeather()
        val forecast = Fixtures.createWeatherForecast()

        locationsRepo.addLocation(location)
        weatherRepo.setWeatherForLocation(location.lat!!, location.lng!!, weather)
        weatherRepo.setForecastForLocation(location.lat!!, location.lng!!, forecast)

        viewModel = LocationDetailsViewModel(
            savedStateHandle = SavedStateHandle(mapOf("locationId" to locationId)),
            locationsRepo = locationsRepo,
            weatherRepo = weatherRepo
        )

        viewModel.state.test {
            val initialState = awaitItem()
            assertEquals(null, initialState.locationDetails)
            assertEquals(false, initialState.isLoading)
            assertEquals(false, initialState.isRefreshing)
            assertEquals(null, initialState.error)
            assertEquals(0, initialState.selectedForecastIndex)
        }
    }

    @Test
    fun `loadLocationDetails successfully loads location with weather`() = runTest {
        // Given
        val location = Location(locationId, "Linz", "AT", 48.3069, 14.2858)
        val weather = Fixtures.createWeather()
        val forecast = Fixtures.createWeatherForecast()

        locationsRepo.addLocation(location)
        weatherRepo.setWeatherForLocation(location.lat!!, location.lng!!, weather)
        weatherRepo.setForecastForLocation(location.lat!!, location.lng!!, forecast)

        viewModel.state.test {
            val initialState = awaitItem() // Initial state
            assertEquals(false, initialState.isLoading)

            // When
            viewModel.handleAction(LocationDetailsAction.LoadData)
            testDispatcher.scheduler.advanceUntilIdle()

            val loadingState = awaitItem() // Loading state
            assertEquals(true, loadingState.isLoading)

            // Then
            val finalState = awaitItem() // Final state
            assertEquals(false, finalState.isLoading)
            assertEquals(false, finalState.isRefreshing)
            assertEquals(null, finalState.error)
            assertEquals(location, finalState.locationDetails?.location)
            assertEquals(weather, finalState.locationDetails?.currentWeather)
            assertEquals(forecast, finalState.locationDetails?.weatherForecast)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadLocationDetails handles repository errors gracefully`() = runTest {
        // Given
        val location = Location(locationId, "Linz", "AT", 48.3069, 14.2858)
        locationsRepo.addLocation(location)
        weatherRepo.shouldThrowError = true

        viewModel.state.test {
            val initialState = awaitItem() // Initial state
            assertEquals(false, initialState.isLoading)

            // When
            viewModel.handleAction(LocationDetailsAction.LoadData)
            testDispatcher.scheduler.advanceUntilIdle()

            val loadingState = awaitItem() // Loading state
            assertEquals(true, loadingState.isLoading)

            // Then
            val errorState = awaitItem() // Error state
            assertEquals(false, errorState.isLoading)
            assertEquals(false, errorState.isRefreshing)
            assertEquals("No weather set for location", errorState.error)
            assertEquals(null, errorState.locationDetails)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `pull to refresh updates isRefreshing state correctly`() = runTest {
        // Given
        val location = Location(locationId, "Linz", "AT", 48.3069, 14.2858)
        val weather = Fixtures.createWeather()
        val forecast = Fixtures.createWeatherForecast()

        locationsRepo.addLocation(location)
        weatherRepo.setWeatherForLocation(location.lat!!, location.lng!!, weather)
        weatherRepo.setForecastForLocation(location.lat!!, location.lng!!, forecast)

        viewModel.state.test {
            val initialState = awaitItem() // Initial state
            assertEquals(false, initialState.isLoading)

            // When
            viewModel.handleAction(LocationDetailsAction.RefreshData)
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            val refreshingState = awaitItem() // Refreshing state
            assertEquals(true, refreshingState.isRefreshing)
            assertEquals(false, refreshingState.isLoading)

            val finalState = awaitItem() // Final state
            assertEquals(false, finalState.isRefreshing)
            assertEquals(false, finalState.isLoading)
            assertEquals(null, finalState.error)
            assertEquals(location, finalState.locationDetails?.location)
            assertEquals(weather, finalState.locationDetails?.currentWeather)
            assertEquals(forecast, finalState.locationDetails?.weatherForecast)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `location without coordinates shows error state`() = runTest {
        // Given
        val location = Location(locationId, "Linz", "AT", null, null)
        locationsRepo.addLocation(location)

        viewModel.state.test {
            val initialState = awaitItem() // Initial state
            assertEquals(false, initialState.isLoading)

            // When
            viewModel.handleAction(LocationDetailsAction.LoadData)
            testDispatcher.scheduler.advanceUntilIdle()

            val loadingState = awaitItem() // Loading state
            assertEquals(true, loadingState.isLoading)

            // Then
            val errorState = awaitItem() // Error state
            assertEquals(false, errorState.isLoading)
            assertEquals(false, errorState.isRefreshing)
            assertEquals("Location coordinates not available", errorState.error)
            assertEquals(null, errorState.locationDetails)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `selectForecastDay updates selected index`() = runTest {
        viewModel.state.test {
            val initialState = awaitItem() // Initial state
            assertEquals(0, initialState.selectedForecastIndex)

            // When
            viewModel.handleAction(LocationDetailsAction.SelectForecastDay(2))
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            val finalState = awaitItem() // Final state
            assertEquals(2, finalState.selectedForecastIndex)

            cancelAndIgnoreRemainingEvents()
        }
    }
}

class TestLocationsRepository : LocationsRepository {
    private val locations = mutableListOf<Location>()

    override fun getAllLocations(): Flow<List<Location>> = flow {
        emit(locations.toList())
    }

    override suspend fun getLocationById(id: String): Location? {
        return locations.find { it.id == id }
    }

    override suspend fun addLocation(location: Location) {
        locations.add(location)
    }

    override suspend fun removeLocation(location: Location) {
        // do nothing
    }

    override suspend fun isDatabaseEmpty(): Boolean {
        return locations.isEmpty()
    }
}

class TestWeatherRepository : WeatherRepository {
    private val weatherMap = mutableMapOf<Pair<Double, Double>, Weather>()
    private val forecastMap = mutableMapOf<Pair<Double, Double>, WeatherForecast>()
    var shouldThrowError = false

    fun setWeatherForLocation(lat: Double, lng: Double, weather: Weather) {
        weatherMap[Pair(lat, lng)] = weather
    }

    fun setForecastForLocation(lat: Double, lng: Double, forecast: WeatherForecast) {
        forecastMap[Pair(lat, lng)] = forecast
    }

    override suspend fun getCurrentWeather(latitude: Double, longitude: Double): Weather {
        if (shouldThrowError) {
            throw IllegalStateException("No weather set for location")
        }
        return weatherMap[Pair(latitude, longitude)] ?: throw IllegalStateException("No weather set for location")
    }

    override suspend fun getWeatherForecast(latitude: Double, longitude: Double): WeatherForecast {
        if (shouldThrowError) {
            throw IllegalStateException("No forecast set for location")
        }
        return forecastMap[Pair(latitude, longitude)] ?: throw IllegalStateException("No forecast set for location")
    }
} 