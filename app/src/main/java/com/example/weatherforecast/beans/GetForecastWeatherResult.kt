package com.example.weatherforecast.beans

import com.google.gson.annotations.SerializedName
import kotlin.collections.List

class GetForecastWeatherResult {
    @SerializedName("cod")
    var cod: String? = null

    @SerializedName("message")
    var message: Double? = null

    @SerializedName("cnt")
    var cnt: Int? = null

    @SerializedName("city")
    var city: City? = null

    @SerializedName("list")
    var list: List<WeatherList>? = null
}