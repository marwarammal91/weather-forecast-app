package com.example.weatherforecast.models

import android.content.Context
import android.os.AsyncTask
import com.example.weatherforecast.application.App
import com.example.weatherforecast.application.AppDatabase

class CityReporsitory(context: Context) {

    var db: CityDao = AppDatabase(context).cityDao()

    fun getAllCities(): List<City> {
        return db.getAll()
    }

    fun deleteAllCities() {
        return db.deleteAllCities()
    }

    fun getAllFavoriteCities(): List<City> {
        return db.getAllFavoriteCities()
    }

    fun findSearchedCity(search: String): List<City> {
        return db.findByNameCountry(search, search)
    }

    fun insertAllCities(cities: List<City>) {
        insertAsyncTask(db).execute(cities).get()
    }

    fun updateCity(isFavorite: Boolean, cityId: Int) {
        db.updateCity(isFavorite, cityId)
    }

    private class insertAsyncTask internal constructor(private val cityDao: CityDao) :
        AsyncTask<List<City>, Void, Void?>() {

        override fun doInBackground(vararg params: List<City>): Void? {
            cityDao.insertAll(params[0])
            return null
        }
    }
}