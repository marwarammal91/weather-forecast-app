package com.marwarammal.weatherforecast.utils

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat

object PermissionUtils {

    fun isPermissionGranted(activity: Activity, permission: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    fun openSettingsDialog(activity: Activity, message: String, requestCode: Int) {
        Utils.showDialogActions(activity, message,
            "Take Me To SETTINGS", "Cancel", {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", activity.packageName, null)
                intent.data = uri
                activity.startActivityForResult(intent, requestCode)
            }, null)
    }

    fun openLocationSettingsDialog(activity: Activity, message: String, requestCode: Int) {
        Utils.showDialogActions(activity, message,
            "Turn on location", "Cancel", {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                activity.startActivityForResult(intent, requestCode)
            }, null)
    }
}
