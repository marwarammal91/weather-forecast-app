package com.example.weatherforecast.models

import androidx.room.*

@Entity(tableName = "city")
data class City(val cityId: Int, val cityName: String, val cityCountry: String) {

    @PrimaryKey var id: Int = 0
    @ColumnInfo(name = "name") var name: String = ""
    @ColumnInfo(name = "country") var country: String = ""
    @ColumnInfo(name = "isFavorite") var isFavorite: Boolean = false
    @Embedded var coord: Coord? = null

    init {
        this.id = cityId
        this.name = cityName
        this.country = cityCountry
    }

    override fun toString(): String {
        return "City: $name\nCountry: $country"
    }
}

@Entity(tableName = "coord")
data class Coord(
    @ColumnInfo(name = "lon") val lon: Double,
    @ColumnInfo(name = "lat") val lat: Double
)