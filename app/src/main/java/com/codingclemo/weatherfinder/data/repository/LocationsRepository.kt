package com.codingclemo.weatherfinder.data.repository

import com.codingclemo.weatherfinder.data.local.LocationDao
import com.codingclemo.weatherfinder.data.local.LocationEntity
import com.codingclemo.weatherfinder.data.model.Location
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface LocationsRepository {
    fun getAllLocations(): Flow<List<Location>>
    suspend fun getLocationById(id: String): Location?
    suspend fun addLocation(location: Location)
    suspend fun removeLocation(location: Location)
    suspend fun isDatabaseEmpty(): Boolean
}

@Singleton
class LocationsRepositoryImpl @Inject constructor(
    private val locationDao: LocationDao,
    private val dispatcher: CoroutineDispatcher
): LocationsRepository {
    override fun getAllLocations(): Flow<List<Location>> {
        return locationDao.getAllLocations()
            .map { entities -> entities.map { it.toLocation() } }
            .flowOn(dispatcher)
    }

    override suspend fun getLocationById(id: String): Location? = withContext(dispatcher) {
        locationDao.getLocationById(id)?.toLocation()
    }

    override suspend fun addLocation(location: Location) = withContext(dispatcher) {
        locationDao.insertLocation(LocationEntity.fromLocation(location))
    }

    override suspend fun removeLocation(location: Location) = withContext(dispatcher) {
        locationDao.deleteLocation(LocationEntity.fromLocation(location))
    }

    override suspend fun isDatabaseEmpty(): Boolean = withContext(dispatcher) {
        locationDao.getLocationCount() == 0
    }
} 