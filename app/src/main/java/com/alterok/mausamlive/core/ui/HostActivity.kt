package com.alterok.mausamlive.core.ui

import android.content.Context
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.asLiveData
import com.alterok.kotlin_essential_extensions.ifFalse
import com.alterok.mausamlive.core.util.LocationPermissionManager
import com.alterok.mausamlive.databinding.LayoutActivityHostBinding
import com.mapbox.common.MapboxOptions
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "HostActivity"

@AndroidEntryPoint
class HostActivity : AppCompatActivity() {
    private lateinit var viewBinding: LayoutActivityHostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hideSystemBars()

        MapboxOptions.accessToken =
            "pk.eyJ1Ijoia3Jpc2hhbnNoYXJtYTQ3IiwiYSI6ImNsZXBodmIxZTA4amkzc21qcjJvMThlZnoifQ.jfZSQVUDYkeasdJ2mfg7AQ"

        LocationPermissionManager.init(this)
        LocationPermissionManager.locationPermissionFlow.asLiveData().observe(this){
            it.ifFalse {
                LocationPermissionManager.requestCoarseLocationPermission(this)
            }
        }

        setContentView(LayoutActivityHostBinding.inflate(layoutInflater).apply {
            viewBinding = this
        }.root)
    }

    private fun hideSystemBars() {
        enableEdgeToEdge()

        WindowInsetsControllerCompat(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemBars()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        LocationPermissionManager.handleOnRequestPermissionsResult(
            requestCode, permissions, grantResults
        )
    }
}

fun Context.isDarkThemeOn(): Boolean {
    return resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
}
