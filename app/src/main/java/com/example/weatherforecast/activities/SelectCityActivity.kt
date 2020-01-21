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
import com.example.weatherforecast.application.App
import com.example.weatherforecast.models.City
import com.example.weatherforecast.models.CityDao
import kotlinx.android.synthetic.main.activity_select_city.*
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.internal.wait


class SelectCityActivity : AppCompatActivity() {

    private lateinit var selectCityAdapter: SelectCityAdapter
    private lateinit var cityDao: CityDao
    private lateinit var activity: Activity
    private lateinit var selectedFavoriteCities: List<City>
    private lateinit var citiesList: List<City>

    var searchQuery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_city)

        activity = this
        cityDao = App.appDatabase.cityDao()

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
        GlobalScope.launch(Dispatchers.Main) {
            selectedFavoriteCities = cityDao.getAllFavoriteCities()
            citiesList = cityDao.getAll()
            selectCityAdapter = SelectCityAdapter(activity, ArrayList(citiesList), ArrayList(selectedFavoriteCities))
            rvMultiSelect.adapter = selectCityAdapter
        }

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
        var filteredDataList = cityDao.findByNameCountry(search, search)
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
                cityDao.updateCity(updateCity.isFavorite, updateCity.id)
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
