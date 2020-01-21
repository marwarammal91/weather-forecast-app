package com.example.weatherforecast.beans

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetCurrentWeatherResult : Serializable {

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
    @SerializedName("id")
    val id: Int? = null
    @SerializedName("name")
    val name: String? = null
}
