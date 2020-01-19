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

    @Query("SELECT * FROM city WHERE isFavorite = 1")
    fun getAllFavoriteCities(): List<City>

    // replace the existing item
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(cities: List<City>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCity(city: City)

    @Query("UPDATE city SET isFavorite = :isFavorite  WHERE id =:cityId")
    fun updateCity(isFavorite: Boolean, cityId: Int)

    @Delete
    fun delete(city: City)
}