package com.example.weatherforecast.utils

import android.content.Context
import java.io.InputStream

object Utils {

    fun readJSONFromAsset(context: Context): String? {
        var json: String? = null
        try {
            val  inputStream: InputStream = context.assets.open("city.list.json")
            json = inputStream.bufferedReader().use{it.readText()}
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }
        return json
    }
}