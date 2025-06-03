package com.codingclemo.weatherfinder.ui.locations.overview

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

class LocationsOverviewViewModelTest {
    private lateinit var viewModel: LocationsOverviewViewModel
    private lateinit var locationsRepo: TestLocationsRepository
    private lateinit var weatherRepo: TestWeatherRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        locationsRepo = TestLocationsRepository()
        weatherRepo = TestWeatherRepository()
        viewModel = LocationsOverviewViewModel(
            weatherRepo = weatherRepo,
            locationsRepo = locationsRepo,
            ioDispatcher = testDispatcher
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has empty list and no loading state`() = runTest {
        viewModel.state.test {
            val initialState = awaitItem()
            assertEquals(emptyList<LocationAndWeather>(), initialState.locationAndWeatherList)
            assertEquals(false, initialState.isLoading)
            assertEquals(false, initialState.isRefreshing)
            assertEquals(null, initialState.error)
        }
    }

    @Test
    fun `loadLocations successfully loads locations with weather`() = runTest {
        // Given
        val location1 = Location("1", "Linz", "AT", 48.3069, 14.2858)
        val location2 = Location("2", "Vienna", "AT", 48.2082, 16.3738)
        val weather1 = Fixtures.createWeather()
        val weather2 = Fixtures.createWeather().copy(temperature = 22.0)

        locationsRepo.addLocation(location1)
        locationsRepo.addLocation(location2)
        weatherRepo.setWeatherForLocation(location1.lat!!, location1.lng!!, weather1)
        weatherRepo.setWeatherForLocation(location2.lat!!, location2.lng!!, weather2)

        viewModel.state.test {
            val initialState = awaitItem() // Initial state
            assertEquals(false, initialState.isLoading)

            // When
            viewModel.handleAction(LocationsOverviewAction.LoadData)
            testDispatcher.scheduler.advanceUntilIdle()

            val loadingState = awaitItem() // Loading state
            assertEquals(true, loadingState.isLoading)

            // Then
            val finalState = awaitItem() // Final state
            assertEquals(false, finalState.isLoading)
            assertEquals(false, finalState.isRefreshing)
            assertEquals(null, finalState.error)
            assertEquals(2, finalState.locationAndWeatherList.size)
            assertEquals(location1, finalState.locationAndWeatherList[0].location)
            assertEquals(weather1, finalState.locationAndWeatherList[0].weather)
            assertEquals(location2, finalState.locationAndWeatherList[1].location)
            assertEquals(weather2, finalState.locationAndWeatherList[1].weather)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadLocations handles repository errors gracefully`() = runTest {
        // Given
        val location = Location("1", "Linz", "AT", 48.3069, 14.2858)
        locationsRepo.addLocation(location)
        weatherRepo.shouldThrowError = true

        viewModel.state.test {
            val initialState = awaitItem() // Initial state
            assertEquals(false, initialState.isLoading)

            // When
            viewModel.handleAction(LocationsOverviewAction.LoadData)
            testDispatcher.scheduler.advanceUntilIdle()

            val loadingState = awaitItem() // Loading state
            assertEquals(true, loadingState.isLoading)

            // Then
            val errorState = awaitItem() // Error state
            assertEquals(false, errorState.isLoading)
            assertEquals(false, errorState.isRefreshing)
            assertEquals("No weather set for location", errorState.error)
            assertEquals(emptyList<LocationAndWeather>(), errorState.locationAndWeatherList)
        }
    }

    @Test
    fun `pull to refresh updates isRefreshing state correctly`() = runTest {
        // Given
        val location = Location("1", "Linz", "AT", 48.3069, 14.2858)
        val weather = Fixtures.createWeather()
        locationsRepo.addLocation(location)
        weatherRepo.setWeatherForLocation(location.lat!!, location.lng!!, weather)

        viewModel.state.test {
            val initialState = awaitItem() // Initial state
            assertEquals(false, initialState.isLoading)

            // When
            viewModel.handleAction(LocationsOverviewAction.PullToRefresh)
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            val refreshingState = awaitItem() // Loading state
            assertEquals(true, refreshingState.isRefreshing)
            assertEquals(false, refreshingState.isLoading)

            val finalState = awaitItem() // Final state
            assertEquals(false, finalState.isRefreshing)
            assertEquals(false, finalState.isLoading)
            assertEquals(null, finalState.error)
            assertEquals(1, finalState.locationAndWeatherList.size)
        }
    }

    @Test
    fun `locations without coordinates are filtered out`() = runTest {
        // Given
        val locationWithCoords = Location("1", "Linz", "AT", 48.3069, 14.2858)
        val locationWithoutCoords = Location("2", "Vienna", "AT", null, null)
        val weather = Fixtures.createWeather()

        locationsRepo.addLocation(locationWithCoords)
        locationsRepo.addLocation(locationWithoutCoords)
        weatherRepo.setWeatherForLocation(locationWithCoords.lat!!, locationWithCoords.lng!!, weather)

        viewModel.state.test {
            val initialState = awaitItem() // Initial state
            assertEquals(false, initialState.isLoading)

            // When
            viewModel.handleAction(LocationsOverviewAction.LoadData)
            testDispatcher.scheduler.advanceUntilIdle()

            val loadingState = awaitItem() // Loading state
            assertEquals(true, loadingState.isLoading)

            // Then
            val finalState = awaitItem() // Final state
            assertEquals(false, finalState.isLoading)
            assertEquals(false, finalState.isRefreshing)
            assertEquals(null, finalState.error)
            assertEquals(1, finalState.locationAndWeatherList.size)
            assertEquals(locationWithCoords, finalState.locationAndWeatherList[0].location)
            assertEquals(weather, finalState.locationAndWeatherList[0].weather)
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
    var shouldThrowError = false

    fun setWeatherForLocation(lat: Double, lng: Double, weather: Weather) {
        weatherMap[Pair(lat, lng)] = weather
    }

    override suspend fun getCurrentWeather(latitude: Double, longitude: Double): Weather {
        if (shouldThrowError) {
            throw IllegalStateException("No weather set for location")
        }
        return weatherMap[Pair(latitude, longitude)] ?: throw IllegalStateException("No weather set for location")
    }

    override suspend fun getWeatherForecast(latitude: Double, longitude: Double): WeatherForecast {
        throw NotImplementedError("Not needed for these tests")
    }
} 