package com.example.weatherforecast.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.example.weatherforecast.R
import com.example.weatherforecast.application.App
import com.example.weatherforecast.fragments.InfoDialog
import com.example.weatherforecast.models.CityDao
import com.example.weatherforecast.models.CityReporsitory
import kotlinx.android.synthetic.main.activity_cities.*

class CitiesActivity : AppCompatActivity() {

    var infoDialogFragment: InfoDialog? = null
    private lateinit var cityRepository: CityReporsitory
    val SELECT_CITY_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cities)

        cityRepository = CityReporsitory(this)

        // set click listeners
        addCityBtn.setOnClickListener(onClickListers)
        infoBtn.setOnClickListener(onClickListers)

        if (cityRepository.getAllFavoriteCities().isEmpty()) {
            showInfoDialog()
        }
    }

    private val onClickListers =  View.OnClickListener { view ->
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // handle selected cities
        if (resultCode == Activity.RESULT_OK) {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        infoDialogFragment?.dismissAllowingStateLoss()
    }
}
