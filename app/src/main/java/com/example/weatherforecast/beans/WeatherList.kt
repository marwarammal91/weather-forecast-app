package com.example.weatherforecast.beans

import com.google.gson.annotations.SerializedName

class WeatherList {
    @SerializedName("dt")
    var dt: Int? = null

    @SerializedName("main")
    var main: Main? = null

    @SerializedName("weather")
    var weather: List<Weather>? = null

    @SerializedName("clouds")
    var clouds: Clouds? = null

    @SerializedName("wind")
    var wind: Wind? = null

    @SerializedName("dt_txt")
    var dtTxt: String? = null
}
