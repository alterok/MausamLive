package com.alterok.mausamlive.core.domain.usecase

import com.alterok.dataresult.DataResult
import com.alterok.mausamlive.core.base.usecase.SuspendableUsecase
import com.alterok.mausamlive.core.domain.model.LocalityDomainModel
import com.alterok.mausamlive.core.domain.repository.LocalityRepository
import kotlinx.coroutines.flow.Flow

class GetAllLocalitiesUseCase(private val localityRepository: LocalityRepository) :
    SuspendableUsecase<Unit, Flow<DataResult<List<LocalityDomainModel>>>> {
    override suspend fun invoke(data: Unit): Flow<DataResult<List<LocalityDomainModel>>> {
        return localityRepository.getAllLocalities()
    }
}