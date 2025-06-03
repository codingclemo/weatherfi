package com.codingclemo.weatherfinder.util

import com.codingclemo.weatherfinder.data.model.Location
import com.codingclemo.weatherfinder.domain.model.DailyForecast
import com.codingclemo.weatherfinder.domain.model.HourlyForecast
import com.codingclemo.weatherfinder.domain.model.WeatherForecast
import com.codingclemo.weatherfinder.domain.model.Weather
import java.time.Instant

object Fixtures {
    fun createWeather() = Weather(
        temperature = 21.5,
        feelsLike = 22.0,
        description = "clear sky",
        iconCode = "01d",
        humidity = 65,
        windSpeed = 3.5,
        cityName = "Linz",
        timestamp = Instant.now().epochSecond,
        windDirection = 180,
        visibility = 12,
        pressure = 1234,
    )


    fun createLocation() = Location(
        id = "1",
        name = "Linz",
        country = "Austria",
        lat = 48.3069,
        lng = 14.2858,
    )

    fun createHourlyForecast() = HourlyForecast(
        temperature = 21.5,
        temperatureMin = 15.3,
        temperatureMax = 22.1,
        feelsLike = 22.0,
        humidity = 65,
        description = "clear sky",
        iconCode = "01d",
        windSpeed = 3.5,
        windDirection = 180,
        probabilityOfprecipitation = 0.0,
        visibility = 12,
        pressure = 1234,
        timestamp = Instant.now().epochSecond
    )

    fun createHourlyForecasts() = listOf(
        HourlyForecast(
            temperature = 21.5,
            temperatureMin = 15.3,
            temperatureMax = 22.1,
            feelsLike = 22.0,
            humidity = 65,
            description = "clear sky",
            iconCode = "01d",
            windSpeed = 3.5,
            windDirection = 180,
            probabilityOfprecipitation = 0.0,
            visibility = 12,
            pressure = 1234,
            timestamp = Instant.now().epochSecond
        ),
        HourlyForecast(
            temperature = 20.0,
            temperatureMin = 15.3,
            temperatureMax = 22.1,
            feelsLike = 19.5,
            humidity = 70,
            description = "few clouds",
            iconCode = "02d",
            windSpeed = 4.0,
            windDirection = 190,
            probabilityOfprecipitation = 0.0,
            visibility = 13,
            pressure = 1234,
            timestamp = Instant.now().plusSeconds(3600).epochSecond
        ),
        HourlyForecast(
            temperature = 18.5,
            temperatureMin = 15.3,
            temperatureMax = 22.1,
            feelsLike = 17.8,
            humidity = 75,
            description = "light rain",
            iconCode = "10d",
            windSpeed = 4.5,
            windDirection = 200,
            probabilityOfprecipitation = 0.5,
            visibility = 13,
            pressure = 1234,
            timestamp = Instant.now().plusSeconds(7200).epochSecond
        ),
        HourlyForecast(
            temperature = 21.5,
            temperatureMin = 15.3,
            temperatureMax = 22.1,
            feelsLike = 22.0,
            humidity = 65,
            description = "clear sky",
            iconCode = "01d",
            windSpeed = 3.5,
            windDirection = 180,
            probabilityOfprecipitation = 0.0,
            visibility = 12,
            pressure = 1234,
            timestamp = Instant.now().plusSeconds(10800).epochSecond
        ),
        HourlyForecast(
            temperature = 20.0,
            temperatureMin = 15.3,
            temperatureMax = 22.1,
            feelsLike = 19.5,
            humidity = 70,
            description = "few clouds",
            iconCode = "02d",
            windSpeed = 4.0,
            windDirection = 190,
            probabilityOfprecipitation = 0.0,
            visibility = 13,
            pressure = 1234,
            timestamp = Instant.now().plusSeconds(14400).epochSecond
        ),
        HourlyForecast(
            temperature = 18.5,
            temperatureMin = 15.3,
            temperatureMax = 22.1,
            feelsLike = 17.8,
            humidity = 75,
            description = "light rain",
            iconCode = "10d",
            windSpeed = 4.5,
            windDirection = 200,
            probabilityOfprecipitation = 0.5,
            visibility = 13,
            pressure = 1234,
            timestamp = Instant.now().plusSeconds(18000).epochSecond
        )
    )

    fun createDailyForecast() = DailyForecast(
        timestamp = Instant.now().epochSecond,
        minTemperature = 18.0,
        maxTemperature = 23.0,
        description = "clear sky",
        iconCode = "01d",
        avgProbabilityOfPrecipitation = 0.0,
        hourlyForecasts = createHourlyForecasts(),
        feelsLike = 25.0,
        humidity = 75,
        maxWindSpeed = 4.23,
        windDirection = 248,
        visibility = 1532,
        averageTemperature = 20.0,
    )

    fun createDailyForecasts() = listOf(
        DailyForecast(
            timestamp = Instant.now().epochSecond,
            minTemperature = 18.0,
            maxTemperature = 23.0,
            description = "clear sky",
            iconCode = "01d",
            avgProbabilityOfPrecipitation = 0.0,
            hourlyForecasts = createHourlyForecasts(),
            feelsLike = 25.0,
            humidity = 75,
            maxWindSpeed = 4.23,
            windDirection = 248,
            visibility = 1532,
            averageTemperature = 20.0,
        ),
        DailyForecast(
            timestamp = Instant.now().plusSeconds(86400).epochSecond,
            minTemperature = 17.0,
            maxTemperature = 22.0,
            description = "few clouds",
            iconCode = "02d",
            avgProbabilityOfPrecipitation = 0.0,
            hourlyForecasts = createHourlyForecasts(),
            feelsLike = 25.0,
            humidity = 75,
            maxWindSpeed = 4.23,
            windDirection = 248,
            visibility = 1532,
            averageTemperature = 20.0,
        ),
        DailyForecast(
            timestamp = Instant.now().plusSeconds(172800).epochSecond,
            minTemperature = 16.0,
            maxTemperature = 20.0,
            description = "light rain",
            iconCode = "10d",
            avgProbabilityOfPrecipitation = 2.5,
            hourlyForecasts = createHourlyForecasts(),
            feelsLike = 25.0,
            humidity = 75,
            maxWindSpeed = 4.23,
            windDirection = 248,
            visibility = 1532,
            averageTemperature = 20.0,
        ),

        )

    fun createWeatherForecast() = WeatherForecast(
        dailyForecasts = createDailyForecasts()
    )
} 