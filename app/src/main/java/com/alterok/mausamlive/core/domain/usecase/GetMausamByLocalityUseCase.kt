package com.alterok.mausamlive.core.domain.usecase

import com.alterok.dataresult.DataResult
import com.alterok.mausamlive.core.base.usecase.SuspendableUsecase
import com.alterok.mausamlive.core.domain.model.LocalityDomainModel
import com.alterok.mausamlive.core.domain.model.MausamDomainModel
import com.alterok.mausamlive.core.domain.repository.MausamRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetMausamByLocalityUseCase(private val repository: MausamRepository) :
    SuspendableUsecase<LocalityDomainModel, Flow<DataResult<MausamDomainModel>>> {

    override suspend fun invoke(data: LocalityDomainModel): Flow<DataResult<MausamDomainModel>> {
        return withContext(Dispatchers.IO) {
            repository.getMousamByLocality(data)
        }
    }
}