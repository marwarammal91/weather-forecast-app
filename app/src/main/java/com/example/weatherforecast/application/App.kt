package com.example.weatherforecast.application

import android.app.Application

class App: Application() {

    companion object {
        lateinit var appDatabase: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()

        appDatabase = AppDatabase(this)

    }
}