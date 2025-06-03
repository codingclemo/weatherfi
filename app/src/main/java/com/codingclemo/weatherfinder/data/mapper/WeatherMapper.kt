package com.codingclemo.weatherfinder.data.mapper

import android.util.Log
import com.codingclemo.weatherfinder.data.model.CurrentWeatherResponse
import com.codingclemo.weatherfinder.data.model.ForecastWeatherResponse
import com.codingclemo.weatherfinder.domain.model.DailyForecast
import com.codingclemo.weatherfinder.domain.model.HourlyForecast
import com.codingclemo.weatherfinder.domain.model.Weather
import com.codingclemo.weatherfinder.domain.model.WeatherForecast
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class WeatherMapper {
    fun toWeather(response: CurrentWeatherResponse): Weather {
        return Weather(
            temperature = response.main.temp,
            feelsLike = response.main.feelsLike,
            description = response.weather.first().description,
            iconCode = response.weather.first().icon,
            humidity = response.main.humidity,
            windSpeed = response.wind.speed,
            windDirection = response.wind.directionInDegrees,
            visibility = response.visibility,
            pressure = response.main.pressure,
            timestamp = response.dt,
            cityName = response.name,
        )
    }

    fun toWeatherForecast(response: ForecastWeatherResponse): WeatherForecast {
        val forecasts = response.list.map { forecast ->
            HourlyForecast(
                temperature = forecast.main.temp,
                temperatureMin = forecast.main.tempMin,
                temperatureMax = forecast.main.tempMax,
                feelsLike = forecast.main.feelsLike,
                humidity = forecast.main.humidity,
                description = forecast.weather.first().description,
                iconCode = forecast.weather.first().icon,
                windSpeed = forecast.wind.speed,
                windDirection = forecast.wind.directionInDegrees,
                probabilityOfprecipitation = forecast.probabilityOfPrecipitation,
                visibility = forecast.visibility,
                pressure = forecast.main.pressure,
                timestamp = forecast.dt
            )
        }

        // Group forecasts by day (from timestamp to midnight)
        val dailyForecasts = forecasts
            .groupBy { forecast ->
                val dateTime = LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(forecast.timestamp),
                    ZoneId.systemDefault()
                )
                dateTime.truncatedTo(ChronoUnit.DAYS)
            }
            .map { (dayStart, dayForecasts) ->
                // Get the most frequent weather condition for the day
                val mostFrequentWeather = dayForecasts
                    .groupBy { it.iconCode }
                    .maxByOrNull { it.value.size }
                    ?.value?.first()

                val mostFrequentWindDirection = dayForecasts
                    .groupBy { it.windDirection }
                    .maxByOrNull { it.value.size }
                    ?.value?.first()

                val mostFrequentVisibility = dayForecasts
                    .groupBy { it.visibility }
                    .maxByOrNull { it.value.size }
                    ?.value?.first()


                val firstForecastOfDay = forecasts
                    .filter { forecast ->
                        val forecastTime = LocalDateTime.ofInstant(
                            Instant.ofEpochSecond(forecast.timestamp),
                            ZoneId.systemDefault()
                        )
                        forecastTime.truncatedTo(ChronoUnit.DAYS) == dayStart
                    }
                    .minByOrNull { it.timestamp }

                val firstForecastTime = LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(firstForecastOfDay?.timestamp ?: dayStart.atZone(ZoneId.systemDefault()).toEpochSecond()),
                    ZoneId.systemDefault()
                )

             DailyForecast(
                    timestamp = dayStart.atZone(ZoneId.systemDefault()).toEpochSecond(),
                    averageTemperature = dayForecasts.map { it.temperature }.average(),
                    minTemperature = dayForecasts.minOf { it.temperature },
                    maxTemperature = dayForecasts.maxOf { it.temperature },
                    description = mostFrequentWeather?.description ?: dayForecasts.first().description,
                    feelsLike = dayForecasts.map { it.feelsLike }.average(),
                    humidity = dayForecasts.map { it.humidity }.average().toInt(),
                    maxWindSpeed = dayForecasts.map { it.windSpeed }.max(),
                    windDirection = mostFrequentWindDirection?.windDirection ?: dayForecasts.first().windDirection,
                    visibility = mostFrequentVisibility?.visibility ?: dayForecasts.first().visibility,
                    iconCode = mostFrequentWeather?.iconCode ?: dayForecasts.first().iconCode,
                    avgProbabilityOfPrecipitation = dayForecasts.map {it.probabilityOfprecipitation}.average(),
                    hourlyForecasts = forecasts
                        .filter { forecast ->
                            val forecastTime = LocalDateTime.ofInstant(
                                Instant.ofEpochSecond(forecast.timestamp),
                                ZoneId.systemDefault()
                            )
                            // Only include forecasts that are:
                            // 1. After or equal to the first forecast of this day
                            // 2. Before the first forecast + 24 hours
                            forecastTime >= firstForecastTime &&
                                    forecastTime.isBefore(firstForecastTime.plusHours(24))
                        }
                        .sortedBy { it.timestamp }
                )
            }
            .sortedBy { it.timestamp }
        return WeatherForecast(dailyForecasts = dailyForecasts)
    }
} 