package com.marwarammal.weatherforecast.beans

import com.google.gson.annotations.SerializedName

class WeatherList {

    @SerializedName("main")
    var main: Main? = null

    @SerializedName("weather")
    var weather: List<Weather>? = null

    @SerializedName("wind")
    var wind: Wind? = null

    @SerializedName("dt_txt")
    var dtTxt: String? = null
}
