package com.marwarammal.weatherforecast.beans

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Wind : Serializable {
    @SerializedName("speed")
    val speed: Double? = null
}
