package com.codingclemo.weatherfinder.data.model

import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(
    val coord: Coordinates,
    val weather: List<Weather>,
    val base: String,
    val main: MainWeather,
    val visibility: Int,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Long,
    val sys: CurrentWeatherSys,
    val timezone: Int,
    val id: Int,
    val name: String,
    val cod: Int
)

data class CurrentWeatherSys(
    val type: Int,
    val id: Int,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)

data class ForecastWeatherResponse(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<WeatherForecast>,
    val city: City
)

data class WeatherForecast(
    val dt: Long,
    val main: MainWeather,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility: Int,
    @SerializedName("pop")
    val probabilityOfPrecipitation: Double,
    val rain: Rain?,
    val snow: Snow?,
    val sys: Sys,
    @SerializedName("dt_txt")
    val dtTxt: String
)

data class MainWeather(
    val temp: Double,
    @SerializedName("feels_like")
    val feelsLike: Double,
    @SerializedName("temp_min")
    val tempMin: Double,
    @SerializedName("temp_max")
    val tempMax: Double,
    val pressure: Int,
    @SerializedName("sea_level")
    val seaLevel: Int,
    @SerializedName("grnd_level")
    val groundLevel: Int,
    val humidity: Int,
    @SerializedName("temp_kf")
    val tempKf: Double
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Clouds(
    val all: Int
)

data class Wind(
    val speed: Double,
    @SerializedName("deg")
    val directionInDegrees: Int,
    val gust: Double
)

data class Rain(
    @SerializedName("1h")
    val volumeLastHourInMm: Double
)

data class Snow(
    @SerializedName("1h")
    val volumeLastHourInMm: Double
)

data class Sys(
    val pod: String
)

data class City(
    val id: Int,
    val name: String,
    @SerializedName("coord")
    val coordinates: Coordinates,
    val country: String,
    val population: Int,
    val timezone: Int,
    val sunrise: Long,
    val sunset: Long
)

data class Coordinates(
    val lat: Double,
    val lon: Double
) 