package com.example.weatherforecast.activities

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherforecast.R
import com.example.weatherforecast.application.AppDatabase
import com.example.weatherforecast.models.City
import com.example.weatherforecast.utils.Utils
import org.json.JSONArray
import org.json.JSONException


class SplashActivity : AppCompatActivity() {

    lateinit var cityList: ArrayList<City>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        loadCities()
    }

    private fun loadCities() {
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
            AsyncTask.execute {
                // Insert City Data
                AppDatabase(this).cityDao().insertAll(cityList)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        } finally {
            navigateToCitiesActivity()
        }
    }


    private fun navigateToCitiesActivity () {
        val intent = Intent(this, CitiesActivity::class.java)
        startActivity(intent)
        finish()
    }
}
