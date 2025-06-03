package com.codingclemo.weatherfinder.util

import java.text.SimpleDateFormat
import java.util.Locale

object Formatter {

    fun formatWindSpeed(windSpeed: Double, isMetric : Boolean = true) : String {
        return if(isMetric) {
            "${windSpeed.toInt()} km/h"
        } else {
            "${windSpeed.toInt()} mi/h"
        }
    }

    fun formatHumidity(humidity: Int): String {
        return "$humidity%"
    }

    fun formatProbabilityOfPrecipitation(probability: Double): String {
        return "${(probability*100).toInt()}%"
    }

    fun formatTemperature(temperature: Double): String {
        return "${temperature.toInt()}°"
    }

    fun formatTimestamp(timestamp: Long, pattern: String = "hh:mm a"): String {
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        val text = formatter.format(timestamp * 1000)
        return text
    }
}