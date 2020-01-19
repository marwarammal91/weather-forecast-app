package com.example.weatherforecast.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View.VISIBLE
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.R
import com.example.weatherforecast.adapters.CityAdapter
import com.example.weatherforecast.application.App
import com.example.weatherforecast.models.City
import com.example.weatherforecast.models.CityDao
import com.example.weatherforecast.models.CityReporsitory
import kotlinx.android.synthetic.main.activity_select_city.*
import kotlinx.android.synthetic.main.layout_header.*


class SelectCityActivity : AppCompatActivity() {

    private lateinit var cityAdapter: CityAdapter
    private lateinit var cityRepository: CityReporsitory

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
        saveTextView.setOnClickListener{
            save()
        }

        val llManager = LinearLayoutManager(this)
        rvMultiSelect.layoutManager = llManager

        // get selected cities
        val selectedItems = cityRepository.getAllFavoriteCities()
        val cityLists = cityRepository.getAllCities()
        cityAdapter = CityAdapter(ArrayList(cityLists), ArrayList(selectedItems))
        rvMultiSelect.adapter = cityAdapter

        // search view
        citySearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty() && query != searchQuery) {
                    searchQuery = query
                    generateList(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty() && newText != searchQuery) {
                    searchQuery = newText
                    generateList(newText)
                }
                return false
            }
        })
    }

    private fun generateList(search: String) {
        var filteredDataList = cityRepository.findSearchedCity(search)
        if (search.isEmpty()) {
            filteredDataList = cityRepository.getAllCities()
        }

        val size = cityAdapter.cityListItems.size
        cityAdapter.cityListItems.clear()
        cityAdapter.notifyItemRangeRemoved(0, size)
        cityAdapter.updateList(ArrayList(filteredDataList))
        cityAdapter.notifyItemInserted(filteredDataList.size)
        cityAdapter.notifyDataSetChanged()
    }

    private fun save() {
        val arrayOfSelectedItems = ArrayList<City>()
        for (i in 0 until cityAdapter.itemCount) {
            if (cityAdapter.getItem(i).isFavorite) {
                val updateCity = cityAdapter.getItem(i)
                cityRepository.updateCity(updateCity.isFavorite, updateCity.cityId)
                arrayOfSelectedItems.add(cityAdapter.getItem(i))
            }
        }
        val output = Intent()
        setResult(Activity.RESULT_OK, output)
        finish()
    }
}
