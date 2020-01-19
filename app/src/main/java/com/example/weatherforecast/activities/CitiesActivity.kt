package com.example.weatherforecast.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.example.weatherforecast.R
import com.example.weatherforecast.fragments.InfoDialog
import kotlinx.android.synthetic.main.activity_cities.*

class CitiesActivity : AppCompatActivity() {

    var inforDialogFragment: InfoDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cities)


        // set click listeners
        addCityBtn.setOnClickListener(onClickListers)
        infoBtn.setOnClickListener(onClickListers)


        showInfoDialog()
    }

    private val onClickListers =  View.OnClickListener { view ->
        when (view.id) {
            R.id.addCityBtn -> {

            }
            R.id.infoBtn -> {
                showInfoDialog()
            }
        }
    }

    fun showInfoDialog() {
        if (!isFinishing) {
            inforDialogFragment = InfoDialog.newInstance()
            inforDialogFragment!!.show(supportFragmentManager, "inforDialog")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        inforDialogFragment?.dismissAllowingStateLoss()
    }
}
