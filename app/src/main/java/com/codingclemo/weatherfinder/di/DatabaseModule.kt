package com.codingclemo.weatherfinder.di

import android.content.Context
import androidx.room.Room
import com.codingclemo.weatherfinder.data.local.DatabaseCallback
import com.codingclemo.weatherfinder.data.local.WeatherDatabase
import com.codingclemo.weatherfinder.data.repository.LocationsRepository
import com.codingclemo.weatherfinder.data.repository.LocationsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideWeatherDatabase(
        @ApplicationContext context: Context,
        callback: DatabaseCallback
    ): WeatherDatabase {
        return Room.databaseBuilder(
            context,
            WeatherDatabase::class.java,
            "weather_database"
        )
            .addCallback(callback)
            .build()
    }

    @Provides
    fun provideLocationDao(database: WeatherDatabase) = database.locationDao()

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())

    @Provides
    @Singleton
    fun provideLocationsRepository(
        locationDao: com.codingclemo.weatherfinder.data.local.LocationDao,
        @IoDispatcher dispatcher: kotlinx.coroutines.CoroutineDispatcher
    ): LocationsRepository {
        return LocationsRepositoryImpl(locationDao, dispatcher)
    }
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class IoDispatcher 