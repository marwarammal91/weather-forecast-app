package com.marwarammal.weatherforecast.networking

import com.marwarammal.weatherforecast.beans.GetCurrentWeatherResult
import com.marwarammal.weatherforecast.beans.GetForecastWeatherResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIInterfaceEndpoints {

    @GET("weather?")
    fun getCurrentWeather(
        @Query("id") cityID: Int,
        @Query("APPID") appID: String
    ): Call<GetCurrentWeatherResult>

    @GET("forecast?")
    fun getForecastWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("APPID") appID: String
    ): Call<GetForecastWeatherResult>
}
