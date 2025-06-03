package com.codingclemo.weatherfinder.data.repository

import com.codingclemo.weatherfinder.data.mapper.WeatherMapper
import com.codingclemo.weatherfinder.data.remote.WeatherApiService
import com.codingclemo.weatherfinder.domain.model.WeatherForecast
import com.codingclemo.weatherfinder.domain.model.Weather
import javax.inject.Inject
import javax.inject.Singleton

interface WeatherRepository {
    suspend fun getCurrentWeather(latitude: Double, longitude: Double): Weather
    suspend fun getWeatherForecast(latitude: Double, longitude: Double): WeatherForecast
}

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val weatherApiService: WeatherApiService,
    private val apiKey: String,
    private val weatherMapper: WeatherMapper
): WeatherRepository {
    override suspend fun getCurrentWeather(latitude: Double, longitude: Double): Weather {
        val response = weatherApiService.getCurrentWeather(
            latitude = latitude,
            longitude = longitude,
            apiKey = apiKey
        )
        return weatherMapper.toWeather(response)
    }
    override suspend fun getWeatherForecast(latitude: Double, longitude: Double): WeatherForecast {
        val response = weatherApiService.getWeatherForecast(
            latitude = latitude,
            longitude = longitude,
            apiKey = apiKey
        )
        return weatherMapper.toWeatherForecast(response)
    }
} 