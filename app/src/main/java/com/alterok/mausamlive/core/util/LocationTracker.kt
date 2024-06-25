package com.alterok.mausamlive.core.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationManager
import android.os.CancellationSignal
import androidx.core.location.LocationManagerCompat
import androidx.core.util.Consumer
import com.alterok.dataresult.DataResult
import com.alterok.dataresult.error.NetworkResultError
import com.alterok.dataresult.wrapInSuccessDataResult
import com.alterok.kotlin_essential_extensions.ifNotNull
import com.alterok.kotlin_essential_extensions.ifNull
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.mapbox.geojson.Point
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean


object LocationTracker {
    @SuppressLint("MissingPermission")
    fun getLastLocation(context: Context, onLocationChange: (DataResult<Point?>) -> Unit) {
        onLocationChange(DataResult.Loading())
        val task: Task<Location?> =
            LocationServices.getFusedLocationProviderClient(context).lastLocation
        task.addOnSuccessListener {
            it.ifNull {
                onLocationChange(DataResult.Failure(NetworkResultError.NotFound))
            }.ifNotNull {
                onLocationChange(getLatLng(it).wrapInSuccessDataResult())
            }
        }.addOnFailureListener {
            onLocationChange(DataResult.Failure(NetworkResultError.NotFound))
        }.addOnCanceledListener {
            onLocationChange(DataResult.Failure(NetworkResultError.BadRequest))
        }
    }

    private fun getCurrentLocation(
        context: Context,
        onLocationChange: (Point?) -> Unit,
    ) {
        val locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
        val foundLocation = AtomicBoolean(false)

        val locationConsumer: Consumer<Location?> = Consumer { location ->
            if (!foundLocation.get()) {
                foundLocation.set(true)
                location.ifNotNull {
                    onLocationChange(getLatLng(it))
                }.ifNull {
                    onLocationChange(null)
                }
            }
        }

        registerLocationCallbackListener(
            locationManager,
            LocationManager.NETWORK_PROVIDER,
            locationConsumer
        )

        registerLocationCallbackListener(
            locationManager,
            LocationManager.PASSIVE_PROVIDER,
            locationConsumer
        )
    }

    @SuppressLint("MissingPermission")
    private fun registerLocationCallbackListener(
        locationManager: LocationManager,
        networkProvider: String,
        locationConsumer: Consumer<Location?>,
    ) {
        LocationManagerCompat.getCurrentLocation(
            locationManager,
            networkProvider,
            CancellationSignal(),
            Executors.newSingleThreadExecutor(),
            locationConsumer
        )
    }

    private fun getLatLng(location: Location): Point {
        return Point.fromLngLat(location.longitude, location.latitude)
    }

    interface OnLocationChangeListener {
        fun onComplete(lnglatPoint: Point?)
    }


}