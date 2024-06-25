package com.alterok.mausamlive.core.domain.repository

import com.alterok.dataresult.DataResult
import com.alterok.dataresult.error.NetworkResultError
import com.alterok.dataresult.runCatchingForDataResult
import com.alterok.mausamlive.core.util.asDataResult
import com.alterok.mausamlive.core.data.mapper.MousamInfoDomainApiMapper
import com.alterok.mausamlive.core.data.remote.MausamApiService
import com.alterok.mausamlive.core.data.remote.model.ApiMousamResponse
import com.alterok.mausamlive.core.domain.model.LocalityDomainModel
import com.alterok.mausamlive.core.domain.model.MausamDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

private const val TAG = "FakeMousamRepository"

class FakeMausamRepository @Inject constructor(
    private val apiService: MausamApiService,
    private val mousamInfoDomainApiMapper: MousamInfoDomainApiMapper,
) : MausamRepository {

    override suspend fun getMousamByLocality(locality: LocalityDomainModel): Flow<DataResult<MausamDomainModel>> =
        flow {
            emit(DataResult.Loading())

            val response =
                runCatchingForDataResult { apiService.getMausamByLocalityId(locality.localityId) }

            response.onFailure { iError, _ ->
                emit(DataResult.Failure(iError))
            }

            response.onSuccess {
                val dataResult = response.flatMap {
                    it?.asDataResult() ?: DataResult.Failure(NetworkResultError.NotFound)
                }.flatMap2 {
                    val dataResult = this
                    if (dataResult is DataResult.Success) {
                        val apiMousamResponse = dataResult.data
                        if (apiMousamResponse.message == ApiMousamResponse.TEMPORARILY_UNAVAILABLE) {
                            DataResult.Failure(NetworkResultError.NoContent)
                        } else if (apiMousamResponse.message.isNotEmpty()) {
                            DataResult.Failure(NetworkResultError.BadRequest)
                        } else
                            dataResult
                    } else {
                        dataResult
                    }
                }.map {
                    if (it.localityWeatherData != null) {

                        MausamDomainModel(
                            locality,
                            mousamInfoDomainApiMapper.toDomainModel(it.localityWeatherData)
                        )
                    } else
                        null
                }

                emit(dataResult)
            }
        }
}