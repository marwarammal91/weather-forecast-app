package com.example.weatherforecast.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import com.example.weatherforecast.models.City
import com.example.weatherforecast.models.Coord
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Utils {

    fun readJSONFromAsset(context: Context): String? {
        val json: String
        try {
            val inputStream: InputStream = context.assets.open("city.list.json")
            json = inputStream.bufferedReader().use { it.readText() }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    inline fun <reified T> fromJson(json: String): T {
        return Gson().fromJson(json, object : TypeToken<T>() {}.type)
    }

    fun isLocationEnabled(activity: Activity): Boolean {
        val locationManager: LocationManager =
            activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    fun getCurrentLocationAddress(activity: Activity, coord: Coord): City {
        val geocoder = Geocoder(activity, Locale.getDefault())
        return try {
            val addresses = geocoder.getFromLocation(coord.lat, coord.lon, 1)
            val city = addresses[0].locality
            val country = addresses[0].countryName
            City(1, city, country)
        } catch (ex: java.lang.Exception) {
            City()
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun convertDate(dateInSeconds: Long, dateFormat: String): String {
        return try {
            val formatter = SimpleDateFormat(dateFormat)
            val cal = Calendar.getInstance()
            cal.timeInMillis = dateInSeconds * 1000 // get date to millis
            formatter.format(cal.time)
        } catch (ex: Exception) {
            print(ex.message)
            return dateInSeconds.toString()
        }
    }

    fun formatDateString(date: String, inputFormat: String, desiredFormat: String): String {
        try {
            val oneWayTripDate: Date?
            val input = SimpleDateFormat(inputFormat, Locale.US)
            val output = SimpleDateFormat(desiredFormat, Locale.US)
            oneWayTripDate = input.parse(date)
            return output.format(oneWayTripDate!!)
        } catch (e: Exception) {
            print(e.message)
        }
        return date
    }

    fun checkNetwork(activity: Activity): Boolean {
        (activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
            return getNetworkCapabilities(activeNetwork)?.run {
                when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } ?: false
        }
    }

    // region "alert dialogs"
    fun showDialogActions(
        activity: Activity,
        msg: String,
        posAction: String,
        negAction: String,
        posRunnable: () -> Unit,
        negRunnable: (() -> Unit)?
    ): AlertDialog? {
        val diag = AlertDialog.Builder(activity)
        diag.setMessage(msg)
        diag.setCancelable(false)
        diag.setPositiveButton(posAction) { dialog, _ ->
            dialog.dismiss()
            Handler().post(posRunnable)
        }
        diag.setNegativeButton(negAction) { dialog, _ ->
            dialog.dismiss()
            Handler().post(negRunnable)
        }
        return if (activity.isFinishing || activity.isDestroyed) null else diag.show()
    }

    fun showAlertDialog(activity: Activity, msg: String, action: String, finishActivity: Boolean) {
        val diag = AlertDialog.Builder(activity)
        diag.setMessage(msg)
        diag.setCancelable(false)
        diag.setPositiveButton(action) { dialog, _ ->
            dialog.dismiss()
            if (finishActivity) {
                activity.finish()
            }
        }
        if (!activity.isFinishing) {
            diag.show()
        }
    }
    // endregion
}
