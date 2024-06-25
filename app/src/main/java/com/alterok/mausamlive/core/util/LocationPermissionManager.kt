package com.alterok.mausamlive.core.util

import android.Manifest
import android.app.Activity
import android.content.Context
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object LocationPermissionManager {
    private const val REQUEST_CODE_PERMISSION_COARSE_LOCATION = 50
    private var hasInitialized = false
    private val _locationPermissionFlow = MutableStateFlow(false)
    val locationPermissionFlow: StateFlow<Boolean>
        get() {
            if (!hasInitialized) {
                throw IllegalStateException(
                    "LocationPermissionManager has not been initialized! " +
                            "Initialize with init(context) before using it."
                )
            }
            return _locationPermissionFlow
        }


    fun init(context: Context) {
        hasInitialized = true
        hasCoarseLocationPermission(context).apply {
            _locationPermissionFlow.value = this
        }
    }

    private fun update(granted: Boolean) {
        _locationPermissionFlow.value = granted
    }

    fun recheck(context: Context) {
        hasCoarseLocationPermission(context).apply {
            update(this)
        }
    }

    fun requestCoarseLocationPermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            REQUEST_CODE_PERMISSION_COARSE_LOCATION
        )
    }

    fun handleOnRequestPermissionsResult(
        responseCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        if (responseCode == REQUEST_CODE_PERMISSION_COARSE_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PERMISSION_GRANTED }) {
                update(true)
            } else {
                update(false)
            }
        }
    }

    private fun hasCoarseLocationPermission(context: Context) = (ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PERMISSION_GRANTED)
}