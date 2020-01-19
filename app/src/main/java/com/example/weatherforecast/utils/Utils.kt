package com.example.weatherforecast.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.location.LocationManager
import android.os.Handler
import java.io.InputStream

object Utils {

    fun readJSONFromAsset(context: Context): String? {
        var json: String? = null
        try {
            val  inputStream: InputStream = context.assets.open("city.list.json")
            json = inputStream.bufferedReader().use{it.readText()}
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    fun isLocationEnabled(activity: Activity): Boolean {
        val locationManager: LocationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    // region "alert dialogs"
    fun showDialogActions(activity: Activity, msg: String, posAction: String, negAction: String, posRunnable: () -> Unit, negRunnable: (() -> Unit)?): AlertDialog? {
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