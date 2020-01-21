package com.example.weatherforecast.beans

import com.google.gson.annotations.SerializedName

class City {
    @SerializedName("id")
    var id: Int? = null
    @SerializedName("name")
    var name: String? = null
}
