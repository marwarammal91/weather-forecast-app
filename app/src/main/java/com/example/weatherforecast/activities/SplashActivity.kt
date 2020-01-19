package com.example.weatherforecast.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.weatherforecast.R
import com.example.weatherforecast.models.City
import com.example.weatherforecast.models.CityReporsitory
import com.example.weatherforecast.models.Coord
import com.example.weatherforecast.utils.PermissionUtils
import com.example.weatherforecast.utils.Utils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class SplashActivity : AppCompatActivity() {

    lateinit var cityList: ArrayList<City>
    private lateinit var cityRepository: CityReporsitory
    val PERMISSION_ID = 200
    val ACTIVITY_RESULT_LOCATION_PERMISSION_SETTINGS = 300

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        cityRepository = CityReporsitory(this)

        val isPermissionGranted = PermissionUtils.isPermissionGranted(this, Manifest.permission.ACCESS_COARSE_LOCATION) &&
                PermissionUtils.isPermissionGranted(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (!isPermissionGranted) {
            // request permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_ID)
            }
        } else {
            navigateToCitiesActivity()
        }
    }

    override fun onResume() {
        super.onResume()

        if (cityRepository.getAllCities().isEmpty()) {
            loadCities()
        }
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

                val coordJson = jsonObject.getJSONObject("coord")
                val lon = coordJson.getDouble("lon")
                val lat = coordJson.getDouble("lat")
                val coord = Coord(lon, lat)
                val city = City(id, name, country)
                city.coord = coord
                cityList.add(city)
            }
            // Insert City Data
            cityRepository.insertAllCities(cityList)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }


    private fun navigateToCitiesActivity () {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                val showRationale = shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) ||
                        shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
                if (!showRationale) {
                    // Don't ask again
                    PermissionUtils.openSettingsDialog(this, getString(R.string.settings_location_message), ACTIVITY_RESULT_LOCATION_PERMISSION_SETTINGS)
                } else {
                    Utils.showAlertDialog(this, getString(R.string.denying_location_message), "Ok", false)
                    navigateToCitiesActivity()
                }
            } else {
                navigateToCitiesActivity()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTIVITY_RESULT_LOCATION_PERMISSION_SETTINGS) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Utils.showAlertDialog(
                    this,
                    getString(R.string.denying_location_message),
                    "Ok",
                    false
                )
                navigateToCitiesActivity()
            } else {
                navigateToCitiesActivity()
            }
        }
    }
}
