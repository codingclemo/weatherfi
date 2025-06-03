package com.codingclemo.weatherfinder.domain.model


data class Weather(
    val temperature: Double,
    val feelsLike: Double,
    val description: String,
    val iconCode: String,
    val humidity: Int,
    val windSpeed: Double,
    val cityName: String,
    val timestamp: Long,
    val windDirection: Int,
    val visibility: Int,
    val pressure: Int
)

data class WeatherForecast(
    val dailyForecasts: List<DailyForecast>
)

data class DailyForecast(
    val timestamp: Long,
    val minTemperature: Double,
    val maxTemperature: Double,
    val description: String,
    val iconCode: String,
    val avgProbabilityOfPrecipitation: Double,
    val hourlyForecasts: List<HourlyForecast>,
    val feelsLike: Double,
    val humidity: Int,
    val maxWindSpeed: Double,
    val windDirection: Int,
    val visibility: Int,
    val averageTemperature: Double
)

data class HourlyForecast(
    val temperature: Double,
    val temperatureMin: Double,
    val temperatureMax: Double,
    val feelsLike: Double,
    val humidity: Int,
    val description: String,
    val iconCode: String,
    val windSpeed: Double,
    val windDirection: Int,
    val probabilityOfprecipitation: Double,
    val visibility: Int,
    val pressure: Int,
    val timestamp: Long
) 