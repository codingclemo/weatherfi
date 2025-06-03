package com.codingclemo.weatherfinder.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.codingclemo.weatherfinder.data.model.Location
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [LocationEntity::class], version = 1, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao

    companion object {
        val initialLocations = listOf(
            Location("1", "Linz", "Austria", lat = 48.3069, lng = 14.2858),
            Location("2", "Vienna", "Austria", lat = 48.2082, lng = 16.3738),
            Location("3", "Salzburg", "Austria", lat = 47.8095, lng = 13.0550),
            Location("4", "Graz", "Austria", lat = 47.0707, lng = 15.4395),
            Location("5", "Innsbruck", "Austria", lat = 47.2692, lng = 11.4041),
        )
    }
} 