package com.example.weatherforecast.beans

import com.example.weatherforecast.models.Coord
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetCurrentWeatherResult: Serializable {

    @SerializedName("coord")
    val coord: Coord? = null
    @SerializedName("weather")
    val weather: List<Weather>? = null
    @SerializedName("main")

    val main: Main? = null
    @SerializedName("visibility")
    val visibility: Int? = null
    @SerializedName("wind")
    val wind: Wind? = null
    @SerializedName("dt")
    val dt: Int? = null
    @SerializedName("timezone")
    val timezone: Int? = null
    @SerializedName("id")
    val id: Int? = null
    @SerializedName("name")
    val name: String? = null
    @SerializedName("cod")
    val cod: Int? = null
}

