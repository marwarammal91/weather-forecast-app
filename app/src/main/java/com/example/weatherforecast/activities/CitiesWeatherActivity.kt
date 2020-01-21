package com.example.weatherforecast.activities

import android.annotation.SuppressLint
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.R
import com.example.weatherforecast.adapters.CityAdapter
import com.example.weatherforecast.adapters.WeatherAdapter
import com.example.weatherforecast.beans.GetCurrentWeatherResult
import com.example.weatherforecast.beans.GetForecastWeatherResult
import com.example.weatherforecast.services.WeatherService
import com.example.weatherforecast.utils.Utils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_cities_weather.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.weather_row_item.*

class CitiesWeatherActivity : AppCompatActivity() {

    var weatherService: WeatherService? = null
    lateinit var activity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cities_weather)
        activity = this
        weatherService = WeatherService(this)

        val selectedCityID = intent.getIntExtra("selectedCity", 0)
        val isCurrentCity = intent.getBooleanExtra("isCurrent", false)
        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val longitude = intent.getDoubleExtra("longitude", 0.0)

        detailsView.visibility = GONE

        backBtn.setOnClickListener{
            onBackPressed()
        }

        if (!isCurrentCity) {
            getCurrentWeather(selectedCityID)
        } else {
            getForecastWeather(latitude, longitude)
        }
    }

    @SuppressLint("SetTextI18n")
    fun updateView(weatherResult: GetCurrentWeatherResult) {
        timeTxtView.text = weatherResult.dt.toString()
        cityTxtView.text = weatherResult.name
        tempTxtView.text = weatherResult.main?.temp.toString() + "°"
        dexrTxtView.text = weatherResult.weather?.get(0)?.description
        minTempTxtView.text = weatherResult.main?.tempMin.toString() + "°" + weatherResult.main?.tempMax.toString() + "°"
        windTxtView.text = weatherResult.wind?.speed.toString()

        Picasso.get()
            .load("https://api.openweathermap.org/img/w/" + weatherResult.weather?.get(0)?.icon + ".png")
            .into(iconImageView)
    }

    private fun getCurrentWeather(selectedCityID: Int) {
        progressBar.visibility = VISIBLE

        weatherService!!.getCurrentWeather(selectedCityID, object: WeatherService.IWeatherHandler {
            override fun onAPISuccess(response: Any) {
                progressBar.visibility = GONE
                detailsView.visibility = VISIBLE

                val getCurrentWeatherResult = response as GetCurrentWeatherResult
                titleTextView.text = getCurrentWeatherResult.name
                updateView(getCurrentWeatherResult)
            }

            override fun onAPIFailure(code: Int, message: String) {
                progressBar.visibility = GONE
                Utils.showAlertDialog(activity, message, "Ok", true )
            }
        })
    }

    private fun getForecastWeather(latitude: Double, longitude: Double) {
        progressBar.visibility = VISIBLE

        weatherService!!.getForecastWeather(latitude, longitude, object: WeatherService.IWeatherHandler {
            override fun onAPISuccess(response: Any) {
                progressBar.visibility = GONE

                val getForecastWeatherResult = response as GetForecastWeatherResult
                titleTextView.text = getForecastWeatherResult.city!!.name
                displayForecastWeather(getForecastWeatherResult)
            }

            override fun onAPIFailure(code: Int, message: String) {
                progressBar.visibility = GONE
                Utils.showAlertDialog(activity, message, "Ok", true )
            }

        })
    }

    fun displayForecastWeather(getForecastWeatherResult: GetForecastWeatherResult) {

        weatherRecycleView.visibility = VISIBLE

        val llManager = LinearLayoutManager(this)
        weatherRecycleView.layoutManager = llManager

        val weatherAdapter = WeatherAdapter(this, getForecastWeatherResult.list!!)
        weatherRecycleView.adapter = weatherAdapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        weatherService?.cancelCalls()
    }
}
