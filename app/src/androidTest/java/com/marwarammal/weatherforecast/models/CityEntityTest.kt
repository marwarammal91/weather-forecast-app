package com.marwarammal.weatherforecast.models

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.marwarammal.weatherforecast.application.AppDatabase
import java.io.IOException
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CityEntityTest {
    private lateinit var cityDao: CityDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().context
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java).build()
        cityDao = db.cityDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() {
        // insert a city object
        val coord = Coord(35.494419, 33.888939)
        val city = City(276781, "Beirut", "Lebanon")
        city.coord = coord
        val cityList = ArrayList<City>()
        cityList.add(city)
        cityDao.insertAll(cityList)
        // read inserted object
        val cityItem = cityDao.findByNameCountry(city.name, city.country)[0]
        // make sure it's the same object as inserted
        assertThat(cityItem, equalTo(city))
    }
}
