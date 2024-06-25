package com.alterok.mausamlive.core.domain.repository

import com.alterok.dataresult.DataResult
import com.alterok.mausamlive.core.domain.model.LocalityDomainModel
import com.alterok.mausamlive.core.domain.model.MausamDomainModel
import kotlinx.coroutines.flow.Flow

interface MausamRepository {
    suspend fun getMousamByLocality(locality: LocalityDomainModel): Flow<DataResult<MausamDomainModel>>
}