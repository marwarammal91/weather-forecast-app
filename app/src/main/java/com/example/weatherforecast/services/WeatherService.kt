package com.example.weatherforecast.services

import android.app.Activity
import com.example.weatherforecast.R
import com.example.weatherforecast.beans.GetCurrentWeatherResult
import com.example.weatherforecast.beans.GetForecastWeatherResult
import com.example.weatherforecast.networking.RestAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherService(internal val activity: Activity) {

    private var getCurrentWeatherCall: Call<GetCurrentWeatherResult>? = null
    private var getForecastWeatherCall: Call<GetForecastWeatherResult>? = null

    fun getCurrentWeather(cityID: Int, handler: IWeatherHandler) {
        getCurrentWeatherCall = RestAPI().client.getCurrentWeather(cityID = cityID,
            appID = "518e0b6ac74798e6a599377aafa69590"
        )
        getCurrentWeatherCall!!.enqueue(object : Callback<GetCurrentWeatherResult> {
            override fun onResponse(call: Call<GetCurrentWeatherResult>, response: Response<GetCurrentWeatherResult>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        handler.onAPISuccess(response.body()!!)
                    } else {
                        handler.onAPIFailure(response.code(), response.message())
                    }
                } else {
                    handler.onAPIFailure(response.code(), response.message())
                }
            }

            override fun onFailure(call: Call<GetCurrentWeatherResult>, t: Throwable) {
                if (!call.isCanceled) {
                    handler.onAPIFailure(500, activity.getString(R.string.something_wrong))
                }
            }
        })
    }

    fun getForecastWeather(lat: Double, lon: Double, handler: IWeatherHandler) {
        getForecastWeatherCall = RestAPI().client.getForecastWeather(lat, lon, "518e0b6ac74798e6a599377aafa69590")
        getForecastWeatherCall!!.enqueue(object : Callback<GetForecastWeatherResult> {
            override fun onResponse(call: Call<GetForecastWeatherResult>, response: Response<GetForecastWeatherResult>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        handler.onAPISuccess(response.body()!!)
                    } else {
                        handler.onAPIFailure(response.code(), response.message())
                    }
                } else {
                    handler.onAPIFailure(response.code(), response.message())
                }
            }

            override fun onFailure(call: Call<GetForecastWeatherResult>, t: Throwable) {
                if (!call.isCanceled) {
                    handler.onAPIFailure(500, activity.getString(R.string.something_wrong))
                }
            }
        })
    }

    fun cancelCalls() {
        getForecastWeatherCall?.cancel()
        getCurrentWeatherCall?.cancel()
    }

    interface IWeatherHandler {
        fun onAPISuccess(response: Any)

        fun onAPIFailure(code: Int, message: String)
    }
}
