package com.codingclemo.weatherfinder

import com.codingclemo.weatherfinder.data.mapper.WeatherMapper
import com.codingclemo.weatherfinder.data.model.CurrentWeatherResponse
import com.codingclemo.weatherfinder.data.model.ForecastWeatherResponse
import com.codingclemo.weatherfinder.data.model.MainWeather
import com.codingclemo.weatherfinder.data.model.Weather
import com.codingclemo.weatherfinder.data.model.Wind
import com.codingclemo.weatherfinder.data.model.Coordinates
import com.codingclemo.weatherfinder.data.model.Clouds
import com.codingclemo.weatherfinder.data.model.CurrentWeatherSys
import com.codingclemo.weatherfinder.data.model.City
import com.codingclemo.weatherfinder.data.model.Sys
import com.codingclemo.weatherfinder.data.model.WeatherForecast
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.Instant

class WeatherMapperTest {
    private lateinit var mapper: WeatherMapper

    @Before
    fun setup() {
        mapper = WeatherMapper()
    }

    @Test
    fun `current weather response is correctly mapped`() {
        // Given
        val response = CurrentWeatherResponse(
            coord = Coordinates(48.3069, 14.2858),
            weather = listOf(
                Weather(
                    id = 800,
                    main = "Clear",
                    description = "clear sky",
                    icon = "01d"
                )
            ),
            base = "stations",
            main = MainWeather(
                temp = 21.5,
                feelsLike = 22.0,
                tempMin = 20.0,
                tempMax = 23.0,
                pressure = 1013,
                seaLevel = 1013,
                groundLevel = 1013,
                humidity = 65,
                tempKf = 0.0
            ),
            visibility = 10000,
            wind = Wind(
                speed = 3.5,
                directionInDegrees = 180,
                gust = 4.0
            ),
            clouds = Clouds(all = 0),
            dt = Instant.now().epochSecond,
            sys = CurrentWeatherSys(
                type = 1,
                id = 1,
                country = "AT",
                sunrise = Instant.now().epochSecond,
                sunset = Instant.now().plusSeconds(43200).epochSecond
            ),
            timezone = 7200,
            id = 1,
            name = "Linz",
            cod = 200
        )

        // When
        val result = mapper.toWeather(response)

        // Then
        assertEquals(21.5, result.temperature, 0.01)
        assertEquals(22.0, result.feelsLike, 0.01)
        assertEquals(65, result.humidity)
        assertEquals(1013, result.pressure)
        assertEquals("clear sky", result.description)
        assertEquals("01d", result.iconCode)
        assertEquals(3.5, result.windSpeed, 0.01)
        assertEquals(180, result.windDirection)
        assertEquals(10000, result.visibility)
        assertEquals("Linz", result.cityName)
    }

    @Test
    fun `forecasts by day are grouped correctly`() {
        // Given
        val now = Instant.now()
        val response = ForecastWeatherResponse(
            cod = "200",
            message = 0,
            cnt = 4,
            list = listOf(
                // First day
                createForecastItem(
                    temp = 20.0,
                    description = "clear sky",
                    icon = "01d",
                    timestamp = now.epochSecond
                ),
                createForecastItem(
                    temp = 22.0,
                    description = "clear sky",
                    icon = "01d",
                    timestamp = now.plusSeconds(3600).epochSecond
                ),
                // Second day
                createForecastItem(
                    temp = 18.0,
                    description = "few clouds",
                    icon = "02d",
                    timestamp = now.plusSeconds(86400).epochSecond
                ),
                createForecastItem(
                    temp = 19.0,
                    description = "few clouds",
                    icon = "02d",
                    timestamp = now.plusSeconds(90000).epochSecond
                )
            ),
            city = City(
                id = 1,
                name = "Linz",
                coordinates = Coordinates(48.3069, 14.2858),
                country = "AT",
                population = 200000,
                timezone = 7200,
                sunrise = now.epochSecond,
                sunset = now.plusSeconds(43200).epochSecond
            )
        )

        // When
        val result = mapper.toWeatherForecast(response)

        // Then
        assertEquals(2, result.dailyForecasts.size)
        
        // First day
        val firstDay = result.dailyForecasts[0]
        assertEquals(21.0, firstDay.averageTemperature, 0.01) // (20.0 + 22.0) / 2
        assertEquals("clear sky", firstDay.description)
        assertEquals("01d", firstDay.iconCode)
        assertEquals(2, firstDay.hourlyForecasts.size)

        // Second day
        val secondDay = result.dailyForecasts[1]
        assertEquals(18.5, secondDay.averageTemperature, 0.01) // (18.0 + 19.0) / 2
        assertEquals("few clouds", secondDay.description)
        assertEquals("02d", secondDay.iconCode)
        assertEquals(2, secondDay.hourlyForecasts.size)
    }

    @Test
    fun `daily statistics are calculated correctly`() {
        // Given
        val now = Instant.now()
        val response = ForecastWeatherResponse(
            cod = "200",
            message = 0,
            cnt = 2,
            list = listOf(
                createForecastItem(
                    temp = 15.0,
                    tempMin = 15.0,
                    tempMax = 15.0,
                    humidity = 60,
                    windSpeed = 3.0,
                    timestamp = now.epochSecond
                ),
                createForecastItem(
                    temp = 25.0,
                    tempMin = 25.0,
                    tempMax = 25.0,
                    humidity = 80,
                    windSpeed = 5.0,
                    timestamp = now.plusSeconds(3600).epochSecond
                )
            ),
            city = City(
                id = 1,
                name = "Linz",
                coordinates = Coordinates(48.3069, 14.2858),
                country = "AT",
                population = 200000,
                timezone = 7200,
                sunrise = now.epochSecond,
                sunset = now.plusSeconds(43200).epochSecond
            )
        )

        // When
        val result = mapper.toWeatherForecast(response)

        // Then
        val day = result.dailyForecasts[0]
        assertEquals(20.0, day.averageTemperature, 0.01) // (15.0 + 25.0) / 2
        assertEquals(15.0, day.minTemperature, 0.01)
        assertEquals(25.0, day.maxTemperature, 0.01)
        assertEquals(70, day.humidity) // (60 + 80) / 2
        assertEquals(5.0, day.maxWindSpeed, 0.01)
    }

    private fun createForecastItem(
        temp: Double,
        tempMin: Double = temp,
        tempMax: Double = temp,
        feelsLike: Double = temp,
        humidity: Int = 65,
        description: String = "clear sky",
        icon: String = "01d",
        windSpeed: Double = 3.5,
        windDirection: Int = 180,
        probabilityOfPrecipitation: Double = 0.0,
        visibility: Int = 10000,
        pressure: Int = 1013,
        timestamp: Long
    ) = WeatherForecast(
        dt = timestamp,
        main = MainWeather(
            temp = temp,
            feelsLike = feelsLike,
            tempMin = tempMin,
            tempMax = tempMax,
            pressure = pressure,
            seaLevel = pressure,
            groundLevel = pressure,
            humidity = humidity,
            tempKf = 0.0
        ),
        weather = listOf(
            Weather(
                id = 800,
                main = "Clear",
                description = description,
                icon = icon
            )
        ),
        clouds = Clouds(all = 0),
        wind = Wind(
            speed = windSpeed,
            directionInDegrees = windDirection,
            gust = windSpeed + 0.5
        ),
        visibility = visibility,
        probabilityOfPrecipitation = probabilityOfPrecipitation,
        rain = null,
        snow = null,
        sys = Sys(pod = "d"),
        dtTxt = Instant.ofEpochSecond(timestamp).toString()
    )
}
