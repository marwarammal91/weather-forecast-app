package com.example.weatherforecast.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.R
import com.example.weatherforecast.adapters.CityAdapter
import com.example.weatherforecast.fragments.InfoDialog
import com.example.weatherforecast.models.City
import com.example.weatherforecast.models.CityReporsitory
import com.example.weatherforecast.models.Coord
import com.example.weatherforecast.utils.PermissionUtils
import com.example.weatherforecast.utils.Utils
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var infoDialogFragment: InfoDialog? = null
    private lateinit var cityRepository: CityReporsitory
    lateinit var cityAdapter: CityAdapter
    lateinit var favoriteItems: List<City>
    val SELECT_CITY_CODE = 100
    lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cityRepository = CityReporsitory(this)
        cityAdapter = CityAdapter(this, arrayListOf())

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // set click listeners
        addCityBtn.setOnClickListener(onClickListeners)
        infoBtn.setOnClickListener(onClickListeners)
        nextBtn.setOnClickListener(onClickListeners)

        favoriteItems = cityRepository.getAllFavoriteCities()

        if (favoriteItems.isEmpty()) {
            showInfoDialog()
        } else {
            citiesCardView.visibility = VISIBLE
            val llManager = LinearLayoutManager(this)
            favoriteCitiesRecycleView.layoutManager = llManager

            cityAdapter = CityAdapter(activity = this, favoriteCityList = ArrayList(favoriteItems))
            favoriteCitiesRecycleView.adapter = cityAdapter
        }

        // get current location
        if(Utils.isLocationEnabled(this)) {
            // get current location
            mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                val location: Location? = task.result
                if (location == null) {
                    requestNewLocationData()
                } else {
                    val coord = Coord(location.longitude, location.latitude)
                }
            }
        } else {
            Utils.showAlertDialog(
                this,
                getString(R.string.turn_location),
                "Ok",
                false
            )
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
            R.id.nextBtn -> {
                if (favoriteItems.size < 3) {
                    showInfoDialog()
                } else {
                    // go to weather display page
                }
            }
        }
    }

    private fun showInfoDialog() {
        if (!isFinishing) {
            infoDialogFragment = InfoDialog.newInstance()
            infoDialogFragment!!.show(supportFragmentManager, "infoDialog")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // handle selected cities
        if (resultCode == Activity.RESULT_OK) {
            favoriteItems = cityRepository.getAllFavoriteCities()

            if (favoriteItems.isEmpty()) {
                citiesCardView.visibility = GONE
                showInfoDialog()
            } else {
                citiesCardView.visibility = VISIBLE
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
        var mLocationRequest = LocationRequest()
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
            var mLastLocation: Location = locationResult.lastLocation
            val coord = Coord(mLastLocation.longitude, mLastLocation.latitude)

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        infoDialogFragment?.dismissAllowingStateLoss()
    }
}
