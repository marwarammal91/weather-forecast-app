package com.example.weatherforecast.activities

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View.VISIBLE
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.R
import com.example.weatherforecast.adapters.SelectCityAdapter
import com.example.weatherforecast.models.City
import com.example.weatherforecast.models.CityReporsitory
import kotlinx.android.synthetic.main.activity_select_city.*
import kotlinx.android.synthetic.main.layout_header.*


class SelectCityActivity : AppCompatActivity() {

    private lateinit var selectCityAdapter: SelectCityAdapter
    private lateinit var cityRepository: CityReporsitory
    private lateinit var selectedFavoriteCities: List<City>
    private lateinit var citiesList: List<City>

    var searchQuery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_city)

        cityRepository = CityReporsitory(this)

        // header view
        titleTextView.text = getString(R.string.select_city)
        backBtn.setOnClickListener {
            finish()
        }
        saveTextView.visibility = VISIBLE
        saveTextView.setOnClickListener {
            save()
        }

        val llManager = LinearLayoutManager(this)
        rvMultiSelect.layoutManager = llManager

        // get selected cities
        doAsync {
            selectedFavoriteCities = cityRepository.getAllFavoriteCities()
        }.execute().get()

        doAsync {
            citiesList = cityRepository.getAllCities()
        }.execute().get()

        selectCityAdapter = SelectCityAdapter(this, ArrayList(citiesList), ArrayList(selectedFavoriteCities))
        rvMultiSelect.adapter = selectCityAdapter

        // search view
        citySearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                    searchQuery = "%$query%"
                    generateList(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                    searchQuery = "%$newText%"
                    generateList(newText)
                return false
            }
        })
    }

    private fun generateList(search: String) {
        var filteredDataList = cityRepository.findSearchedCity(search)
        if (search.isEmpty()) {
            filteredDataList = citiesList
        }

        val size = selectCityAdapter.cityListItems.size
        selectCityAdapter.cityListItems.clear()
        selectCityAdapter.notifyItemRangeRemoved(0, size)
        selectCityAdapter.updateList(ArrayList(filteredDataList))
        selectCityAdapter.notifyItemInserted(filteredDataList.size)
        selectCityAdapter.notifyDataSetChanged()
    }

    private fun save() {
        for (i in 0 until selectCityAdapter.itemCount) {
            if (selectCityAdapter.getItem(i).isFavorite) {
                val updateCity = selectCityAdapter.getItem(i)
                cityRepository.updateCity(updateCity.isFavorite, updateCity.id)
            }
        }
        val output = Intent()
        setResult(Activity.RESULT_OK, output)
        finish()
    }

    class doAsync(val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            handler()
            return null
        }
    }
}
