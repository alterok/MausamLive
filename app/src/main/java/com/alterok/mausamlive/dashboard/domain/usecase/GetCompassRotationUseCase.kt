package com.alterok.mausamlive.dashboard.domain.usecase

import com.alterok.mausamlive.dashboard.domain.repository.CompassRepository
import kotlinx.coroutines.flow.Flow

class GetCompassRotationUseCase (private val compassRepository: CompassRepository) {
    operator fun invoke(): Flow<Float> {
        return compassRepository.currentRotation
    }
}