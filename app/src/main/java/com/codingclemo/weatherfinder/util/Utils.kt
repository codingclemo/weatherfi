package com.codingclemo.weatherfinder.util

import java.text.SimpleDateFormat
import java.util.Locale

object Utils {
    fun getIconUrl(iconId: String): String {
        return "https://openweathermap.org/img/wn/$iconId@2x.png"
    }
}