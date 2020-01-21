package com.example.weatherforecast.models

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CityDao {

    @Query("SELECT *" +
            " FROM city")
    fun getAll(): List<City>

    @Query(
        "SELECT * FROM city WHERE name LIKE :nameStr or " +
                "country LIKE :countyStr"
    )
    fun findByNameCountry(nameStr: String = "", countyStr: String = ""): List<City>

    @Query(
        "SELECT * FROM city WHERE" +
                " isFavorite = 1"
    )
    fun getAllFavoriteCities(): List<City>

    // replace the existing item
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(cities: List<City>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCity(city: City)

    @Query(
        "UPDATE city SET isFavorite = :isFavorite " +
                " WHERE id =:cityId"
    )
    fun updateCity(isFavorite: Boolean, cityId: Int)

    @Delete
    fun delete(city: City)

    @Query(
        "DELETE FROM " +
                "city"
    )
    fun deleteAllCities()

    @Query(
        "SELECT * FROM city WHERE " +
                "lon LIKE :lon AND lat LIKE :lat"
    )
    fun fetchCurrentCity(lat: Double, lon: Double): City

    @Query(
        "UPDATE city SET isCurrent = :isCurrent " +
                " WHERE id =:cityId"
    )
    fun updateCurrentCity(isCurrent: Boolean, cityId: Int)

    @Query(
        "Select * from city where" +
                " isCurrent = 1"
    )
    fun getCurrentCity(): City
}
