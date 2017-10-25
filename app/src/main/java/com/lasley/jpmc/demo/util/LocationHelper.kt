package com.lasley.jpmc.demo.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.support.v4.app.ActivityCompat
import com.lasley.jpmc.demo.model.UserLocationModel
import java.io.IOException
import java.util.*

class LocationHelper {
    companion object {
        fun getLatLongFromInput(context: Context, location: String): UserLocationModel? {

            val geocoder = Geocoder(context, Locale.getDefault())
            try {
                val addresses = geocoder.getFromLocationName(location, 3)
                if (addresses.size > 0) {
                    val address = addresses[0]
                    var builtAddress = ""
                    val numOfLines = address.maxAddressLineIndex
                    if (numOfLines > 0) {
                        for (i in 0 until numOfLines) {
                            builtAddress += address.getAddressLine(i)
                            if (i < numOfLines - 1) {
                                builtAddress += ", "
                            }
                        }
                    } else {
                        builtAddress = if (address.getAddressLine(0) != null) address.getAddressLine(0) else address.locality
                    }

                    return UserLocationModel(builtAddress,
                            "%.6f".format(address.latitude).toDouble(),
                            "%.6f".format(address.longitude).toDouble())
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return null
        }

        fun checkLocationPermission(context: Context): Boolean {
            if (ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context as Activity,
                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                        Constants.GPS_PERMISSION)
                return false
            }
            return true
        }
    }
}