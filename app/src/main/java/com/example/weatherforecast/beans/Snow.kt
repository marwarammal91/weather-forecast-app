package com.example.weatherforecast.beans

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Snow {
    @SerializedName("3h")
    @Expose
    private var _3h: Double? = null

    fun get3h(): Double? {
        return _3h
    }

    fun set3h(_3h: Double?) {
        this._3h = _3h
    }
}
