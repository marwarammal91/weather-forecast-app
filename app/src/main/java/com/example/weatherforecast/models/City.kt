package com.example.weatherforecast.models

import androidx.room.*
import java.io.Serializable

@Entity(tableName = "city")
data class City(
    @PrimaryKey var id: Int = 0,
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "country") var country: String = "",
    @ColumnInfo(name = "isFavorite") var isFavorite: Boolean = false,
    @ColumnInfo(name = "isCurrent") var isCurrent: Boolean = false,
    @Embedded var coord: Coord? = null ): Serializable {

    override fun toString(): String {
        return "City: $name\nCountry: $country"
    }
}

data class Coord(
    var lon: Double = 0.0,
    var lat: Double = 0.0
): Serializable