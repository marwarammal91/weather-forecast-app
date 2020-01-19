package com.example.weatherforecast.models

import androidx.room.*

@Dao
interface CityDao {

    @Query("SELECT * FROM city")
    fun getAll() : List<City>

    @Query("SELECT * FROM city WHERE name LIKE :name AND "
            + "country LIKE :country")
    fun findByNameCountry(name: String, county: String): List<City>

    // replase the existing item
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(cities: List<City>)

    @Delete
    fun delete(city: City)
}