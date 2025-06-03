package com.codingclemo.weatherfinder.data.model

data class Location(
    val id: String,
    val name: String,
    val country: String,
    val lat: Double?,
    val lng: Double?,
) 