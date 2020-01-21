package com.example.weatherforecast.models

import android.content.Context
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
        db.insertAll(cities)
    }

    fun updateCity(isFavorite: Boolean, cityId: Int) {
        db.updateCity(isFavorite, cityId)
    }

    fun updateCurrentCity(isCurrent: Boolean, cityId: Int) {
        db.updateCurrentCity(isCurrent, cityId)
    }

    fun getCityByCoord(coord: Coord) : City{
        return db.fetchCurrentCity(coord.lat, coord.lon)
    }

    fun getCurrentCity(): City {
       return db.getCurrentCity()
    }


//    private class insertAsyncTask internal constructor(private val cityDao: CityDao) :
//        AsyncTask<List<City>, Void, Boolean>() {
//
//        override fun doInBackground(vararg params: List<City>): Boolean {
//            cityDao.insertAll(params[0])
//            if (cityDao.getAll().isEmpty()) {
//                return false
//            }
//            return true
//        }
//
//        override fun onPostExecute(result: Boolean) {
//            super.onPostExecute(result)
//            return result
//        }
//    }
}