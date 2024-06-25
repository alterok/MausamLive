package com.alterok.mausamlive.dashboard.data.repository

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.alterok.mausamlive.dashboard.domain.repository.CompassRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

private const val TAG = "CompassRepositoryImpl"

class CompassRepositoryImpl(context: Context) : CompassRepository, SensorEventListener {
    private val compassManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val rotationSensor = compassManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
    private val _currentRotation = MutableStateFlow(0.0f)
    override val currentRotation: Flow<Float>
        get() = _currentRotation


    override fun startSensor() {
        compassManager.registerListener(this, rotationSensor, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null)
            return

        if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
            val rotationMatrix = FloatArray(9)
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
            val orientation = FloatArray(3)
            SensorManager.getOrientation(rotationMatrix, orientation)
            val azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
            _currentRotation.value = azimuth
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun stopSensor() {
        compassManager.unregisterListener(this)
    }

}