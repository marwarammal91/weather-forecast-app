package com.example.weatherforecast.activities

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.weatherforecast.R
import com.example.weatherforecast.adapters.CityAdapter
import com.example.weatherforecast.application.App
import com.example.weatherforecast.application.AppDatabase
import com.example.weatherforecast.models.City
import com.example.weatherforecast.utils.Utils
import kotlinx.android.synthetic.main.activity_select_city.*
import org.json.JSONArray
import org.json.JSONException
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class SplashActivity : AppCompatActivity() {

    lateinit var cityList: ArrayList<City>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val cityDao = App.appDatabase.cityDao()

        if (cityDao.getAll().isEmpty()) {
            // Insert City Data
            cityDao.insertAll(loadCities())

            navigateToCitiesActivity()
        } else {
            navigateToCitiesActivity()
        }
    }

    private fun loadCities(): ArrayList<City> {
        cityList = ArrayList()
        // load cities and save to database
        try {
            val citiesArray = JSONArray(Utils.readJSONFromAsset(this)!!)
            for (i in 0 until citiesArray.length()) {
                val jsonObject = citiesArray.getJSONObject(i)
                val id = jsonObject.getInt("id")
                val name = jsonObject.getString("name")
                val country = jsonObject.getString("country")
                cityList.add(City(id, name, country))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return cityList
    }


    private fun navigateToCitiesActivity () {
        val intent = Intent(this, CitiesActivity::class.java)
        startActivity(intent)
        finish()
    }
}
