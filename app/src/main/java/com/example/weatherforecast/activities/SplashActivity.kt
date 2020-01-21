package com.example.weatherforecast.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.View.VISIBLE
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.weatherforecast.R
import com.example.weatherforecast.application.App
import com.example.weatherforecast.models.City
import com.example.weatherforecast.models.CityDao
import com.example.weatherforecast.utils.PermissionUtils
import com.example.weatherforecast.utils.Utils
import kotlinx.android.synthetic.main.activity_splash.progressBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private lateinit var cityDao: CityDao
    private lateinit var activity: Activity
    val PERMISSION_ID = 200
    val ACTIVITY_RESULT_LOCATION_PERMISSION_SETTINGS = 300

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        cityDao = App.appDatabase.cityDao()
        activity = this

        val isPermissionGranted =
            PermissionUtils.isPermissionGranted(this, Manifest.permission.ACCESS_COARSE_LOCATION) &&
                    PermissionUtils.isPermissionGranted(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
        if (!isPermissionGranted) {
            // request permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    PERMISSION_ID
                )
            }
        } else {
            progressBar.visibility = VISIBLE
            checkCities()
        }
    }

    private fun checkCities() {
        GlobalScope.launch(Dispatchers.IO) {
            if (cityDao.getAll().isEmpty()) {
                LoadCitiesTask(cityDao, activity, progressBar).execute()
            } else {
                navigateToCitiesActivity()
            }
        }
    }

    private fun navigateToCitiesActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                val showRationale =
                    shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) ||
                            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
                if (!showRationale) {
                    // Don't ask again
                    PermissionUtils.openSettingsDialog(
                        this,
                        getString(R.string.settings_location_message),
                        ACTIVITY_RESULT_LOCATION_PERMISSION_SETTINGS
                    )
                } else {
                    Utils.showAlertDialog(
                        this,
                        getString(R.string.denying_location_message),
                        "Ok",
                        false
                    )
                    progressBar.visibility = VISIBLE
                    checkCities()
                }
            } else {
                progressBar.visibility = VISIBLE
                checkCities()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTIVITY_RESULT_LOCATION_PERMISSION_SETTINGS) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Utils.showAlertDialog(
                    this,
                    getString(R.string.denying_location_message),
                    "Ok",
                    false
                )
                progressBar.visibility = VISIBLE
                checkCities()
            } else {
                progressBar.visibility = VISIBLE
                checkCities()
            }
        }
    }

    private class LoadCitiesTask constructor(
        val cityDao: CityDao,
        val activity: Activity,
        val progressBar: RelativeLayout
    ) :
        AsyncTask<List<City>, Void, Void?>() {

        override fun doInBackground(vararg params: List<City>): Void? {

            val cityList: ArrayList<City>
            // load cities and save to database
            try {
                // convert json into a list of Users
                val json = Utils.readJSONFromAsset(activity)!!
                cityList = Utils.fromJson(json)

                // Insert City Data
                cityDao.insertAll(cityList)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }
}
