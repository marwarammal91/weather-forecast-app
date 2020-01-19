package com.example.weatherforecast.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city")
data class City(val cityId: Int, val cityName: String, val cityCountry: String) {

    @PrimaryKey val id: Int = 0
    @ColumnInfo(name = "name") val name: String = ""
    @ColumnInfo(name = "country") val country: String = ""

}