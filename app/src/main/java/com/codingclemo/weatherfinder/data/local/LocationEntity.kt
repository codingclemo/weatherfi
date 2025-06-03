package com.codingclemo.weatherfinder.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.codingclemo.weatherfinder.data.model.Location

@Entity(tableName = "locations")
data class LocationEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val country: String,
    val lat: Double?,
    val lng: Double?
) {
    fun toLocation() = Location(
        id = id,
        name = name,
        country = country,
        lat = lat,
        lng = lng
    )

    companion object {
        fun fromLocation(location: Location) = LocationEntity(
            id = location.id,
            name = location.name,
            country = location.country,
            lat = location.lat,
            lng = location.lng
        )
    }
} 