package com.alterok.mausamlive.dashboard.domain.usecase

import com.alterok.mausamlive.dashboard.domain.repository.CompassRepository
import javax.inject.Inject

class StartCompassUseCase @Inject constructor(private val compassRepository: CompassRepository) {
    operator fun invoke(){
        compassRepository.startSensor()
    }
}