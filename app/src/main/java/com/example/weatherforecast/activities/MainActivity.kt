package com.example.weatherforecast.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.R
import com.example.weatherforecast.adapters.CityAdapter
import com.example.weatherforecast.application.App
import com.example.weatherforecast.fragments.InfoDialog
import com.example.weatherforecast.models.City
import com.example.weatherforecast.models.CityDao
import com.example.weatherforecast.models.Coord
import com.example.weatherforecast.utils.PermissionUtils
import com.example.weatherforecast.utils.Utils
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    var infoDialogFragment: InfoDialog? = null
    private lateinit var cityDao: CityDao
    lateinit var cityAdapter: CityAdapter
    lateinit var activity: Activity
    lateinit var favoriteItems: List<City>
    var currentCity: City? = null
    val SELECT_CITY_CODE = 100
    val ACTIVITY_RESULT_ENABLE_LOCATION_SETTINGS = 333
    lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activity = this
        cityDao = App.appDatabase.cityDao()

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // set click listeners
        addCityBtn.setOnClickListener(onClickListeners)
        infoBtn.setOnClickListener(onClickListeners)

        val llManager = LinearLayoutManager(this)
        favoriteCitiesRecycleView.layoutManager = llManager

        cityAdapter = CityAdapter(this, arrayListOf())
        favoriteCitiesRecycleView.adapter = cityAdapter

        GlobalScope.launch(Dispatchers.Main) {
            favoriteItems = cityDao.getAllFavoriteCities()

            if (favoriteItems.isEmpty()) {
                showInfoDialog()
            } else {
                favoriteCitiesRecycleView.visibility = VISIBLE

                cityAdapter = CityAdapter(activity = activity, favoriteCityList = ArrayList(favoriteItems))
                favoriteCitiesRecycleView.adapter = cityAdapter
            }
        }

        // get current location
        currentCity = cityDao.getCurrentCity()
        displayCurrentCity()

        checkLocation(true)

        currentlocLayout.setOnClickListener {
            val intent = Intent(activity, CitiesWeatherActivity::class.java)
            intent.putExtra("selectedCity", 0)
            intent.putExtra("isCurrent", true)
            intent.putExtra("latitude", currentCity!!.coord?.lat)
            intent.putExtra("longitude", currentCity!!.coord?.lon)
            activity.startActivity(intent)
        }
    }

    private val onClickListeners =  View.OnClickListener { view ->
        when (view.id) {
            R.id.addCityBtn -> {
                val intent = Intent(this, SelectCityActivity::class.java)
                startActivityForResult(intent, SELECT_CITY_CODE)
            }
            R.id.infoBtn -> {
                showInfoDialog()
            }
        }
    }

    private fun showInfoDialog() {
        if (!isFinishing) {
            infoDialogFragment = InfoDialog.newInstance()
            infoDialogFragment!!.show(supportFragmentManager, "infoDialog")
        }
    }

    private fun checkLocation (showDialog: Boolean) {
        if(Utils.isLocationEnabled(this)) {
            // get current location
            mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                val location: Location? = task.result
                if (location == null) {
                    requestNewLocationData()
                } else {
                    val coord = Coord(location.longitude, location.latitude)
                    fetchCurrentCity(coord)
                }
            }
        } else if (showDialog) {
            PermissionUtils.openLocationSettingsDialog(
                this,
                getString(R.string.turn_location),
                ACTIVITY_RESULT_ENABLE_LOCATION_SETTINGS
            )
        }

    }

    fun displayCurrentCity () {
        if (currentCity != null) {
            currentlocLayout.visibility = VISIBLE
            currentCityText.text = currentCity!!.name
        } else {
            currentlocLayout.visibility = GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTIVITY_RESULT_ENABLE_LOCATION_SETTINGS) {
            checkLocation(false)
        }
        // handle selected cities
        if (resultCode == Activity.RESULT_OK) {
            favoriteItems = cityDao.getAllFavoriteCities()

            if (favoriteItems.isEmpty()) {
                favoriteCitiesRecycleView.visibility = GONE
                showInfoDialog()
            } else {
                favoriteCitiesRecycleView.visibility = VISIBLE
                val size = cityAdapter.favoriteCityList.size
                cityAdapter.favoriteCityList.clear()
                cityAdapter.notifyItemRangeRemoved(0, size)
                cityAdapter.updateList(ArrayList(favoriteItems))
                cityAdapter.notifyItemInserted(favoriteItems.size)
                cityAdapter.notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            val coord = Coord(mLastLocation.longitude, mLastLocation.latitude)
            fetchCurrentCity(coord)
        }
    }

    fun fetchCurrentCity(coord: Coord) {
        currentCity = cityDao.fetchCurrentCity(coord.lat, coord.lon)
        if (currentCity != null) {
            cityDao.updateCurrentCity(true, currentCity!!.id)
        } else {
            currentCity = Utils.getCurrentLocationAddress(activity = activity, coord = coord)
        }
        currentCity!!.coord = coord
        displayCurrentCity()
    }

    override fun onDestroy() {
        super.onDestroy()
        infoDialogFragment?.dismissAllowingStateLoss()
    }

    class doAsync(val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            handler()
            return null
        }
    }
}
