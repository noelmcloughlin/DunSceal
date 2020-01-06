package ie.noel.dunsceal.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationRequest

object Permissions {

    private const val REQUEST_PERMISSIONS_REQUEST_CODE = 34

    fun checkLocationPermissions(activity: Activity) : Boolean {
        return if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSIONS_REQUEST_CODE)
            false
        }
    }

    fun isPermissionGranted(code: Int, grantResults: IntArray): Boolean {
        var permissionGranted = false
        if (code == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                grantResults.isEmpty() -> Log.i("Location.kt", "User interaction was cancelled.")
                (grantResults[0] == PackageManager.PERMISSION_GRANTED) -> {
                    permissionGranted = true
                    Log.i("Location.kt", "Permission Granted.")
                }
                else -> Log.i("Location.kt", "Permission Denied.")
            }
        }
        return permissionGranted
    }

    @SuppressLint("RestrictedApi")
    fun createDefaultLocationRequest() : LocationRequest {
        val locationRequest = LocationRequest().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        return locationRequest
    }
}