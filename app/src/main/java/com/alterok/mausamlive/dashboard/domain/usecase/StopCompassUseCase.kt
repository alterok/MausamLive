package com.alterok.mausamlive.dashboard.domain.usecase

import com.alterok.mausamlive.dashboard.domain.repository.CompassRepository

class StopCompassUseCase (private val compassRepository: CompassRepository) {
    operator fun invoke(){
        compassRepository.stopSensor()
    }
}