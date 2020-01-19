package com.example.weatherforecast.models

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CityDao {

    @Query("SELECT * FROM city")
    fun getAll() : List<City>

    @Query("SELECT * FROM city WHERE name LIKE :nameStr or "
            + "country LIKE :countyStr")
    fun findByNameCountry(nameStr: String = "", countyStr: String = ""): List<City>

    // replace the existing item
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(cities: List<City>)

    @Delete
    fun delete(city: City)
}