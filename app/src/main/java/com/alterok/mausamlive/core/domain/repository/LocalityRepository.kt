package com.alterok.mausamlive.core.domain.repository

import com.alterok.dataresult.DataResult
import com.alterok.mausamlive.core.domain.model.LocalityDomainModel
import kotlinx.coroutines.flow.Flow

interface LocalityRepository {
    suspend fun getAllLocalities(): Flow<DataResult<List<LocalityDomainModel>>>
}