package com.example.weatherforecast.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.R
import com.example.weatherforecast.adapters.CityAdapter
import com.example.weatherforecast.adapters.SelectCityAdapter
import com.example.weatherforecast.fragments.InfoDialog
import com.example.weatherforecast.models.City
import com.example.weatherforecast.models.CityReporsitory
import kotlinx.android.synthetic.main.activity_cities.*
import kotlinx.android.synthetic.main.activity_select_city.*

class CitiesActivity : AppCompatActivity() {

    var infoDialogFragment: InfoDialog? = null
    private lateinit var cityRepository: CityReporsitory
    lateinit var cityAdapter: CityAdapter
    lateinit var favoriteItems: List<City>
    val SELECT_CITY_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cities)

        cityRepository = CityReporsitory(this)
        cityAdapter = CityAdapter(this, arrayListOf())

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
                showInfoDialog()
            } else {
                val size = cityAdapter.favoriteCityList.size
                cityAdapter.favoriteCityList.clear()
                cityAdapter.notifyItemRangeRemoved(0, size)
                cityAdapter.updateList(ArrayList(favoriteItems))
                cityAdapter.notifyItemInserted(favoriteItems.size)
                cityAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        infoDialogFragment?.dismissAllowingStateLoss()
    }
}
