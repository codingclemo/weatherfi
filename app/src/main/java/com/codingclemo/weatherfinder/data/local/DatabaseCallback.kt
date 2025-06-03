package com.codingclemo.weatherfinder.data.local

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.codingclemo.weatherfinder.data.repository.LocationsRepository
import com.codingclemo.weatherfinder.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class DatabaseCallback @Inject constructor(
    private val locationsRepository: Provider<LocationsRepository>,
    @ApplicationScope private val applicationScope: CoroutineScope
) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        applicationScope.launch(Dispatchers.IO) {
            if (locationsRepository.get().isDatabaseEmpty()) {
                WeatherDatabase.initialLocations.forEach { location ->
                    locationsRepository.get().addLocation(location)
                }
            }
        }
    }
} 