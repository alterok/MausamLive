package com.alterok.mausamlive.dashboard.domain.repository

import kotlinx.coroutines.flow.Flow

interface CompassRepository {
    val currentRotation : Flow<Float>
    fun startSensor()
    fun stopSensor()
}