package com.example.weatherforecast.beans

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Wind: Serializable {
    @SerializedName("speed")
    val speed: Double? = null
    @SerializedName("deg")
    val deg: Int? = null
}