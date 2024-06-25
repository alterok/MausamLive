package com.alterok.mausamlive.dashboard.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.alterok.mausamlive.dashboard.domain.usecase.GetCompassRotationUseCase
import com.alterok.mausamlive.dashboard.domain.usecase.StartCompassUseCase
import com.alterok.mausamlive.dashboard.domain.usecase.StopCompassUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CompassWatcherViewModel @Inject constructor(
    getCompassRotationUseCase: GetCompassRotationUseCase,
    private val stopCompassUseCase: StopCompassUseCase,
    private val startCompassUseCase: StartCompassUseCase,
) : ViewModel() {
    private val compassRotationFlow: Flow<Float> = getCompassRotationUseCase()
    val compassRotationLD: LiveData<Float> = compassRotationFlow.asLiveData()

    fun startSensor() {
        startCompassUseCase()
    }
    fun stopSensor() {
        stopCompassUseCase()
    }
}