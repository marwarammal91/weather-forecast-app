package com.example.weatherforecast.beans

import com.google.gson.annotations.SerializedName

class Main {
    @SerializedName("temp")
    var temp: Double? = null
    @SerializedName("temp_min")
    var tempMin: Double? = null
    @SerializedName("temp_max")
    var tempMax: Double? = null
}
