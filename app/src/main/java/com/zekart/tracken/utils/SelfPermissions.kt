package com.zekart.tracken.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class SelfPermissions {
    companion object{
        private const val PERMISSION_REQUEST_CODE = 1

        fun checkAndRequestPermissions(activity: Activity): Boolean {
            val coarseLocation =
                ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
            val fineLocation =
                ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
            val listPermissionsNeeded: MutableList<String> = ArrayList()

            if (fineLocation != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            if (coarseLocation != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
            if (listPermissionsNeeded.isNotEmpty()) {
                ActivityCompat.requestPermissions(
                    activity,
                    listPermissionsNeeded.toTypedArray(),
                    PERMISSION_REQUEST_CODE
                )
                return false
            }
            return true
        }
    }
}