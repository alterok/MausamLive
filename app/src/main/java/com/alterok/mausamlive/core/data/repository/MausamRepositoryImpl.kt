package com.alterok.mausamlive.core.data.repository

import android.text.format.DateUtils
import com.alterok.dataresult.DataResult
import com.alterok.dataresult.error.NetworkResultError
import com.alterok.dataresult.runCatchingForDataResult
import com.alterok.dataresult.wrapInSuccessDataResult
import com.alterok.mausamlive.core.util.asDataResult
import com.alterok.mausamlive.core.data.local.MausamDatabase
import com.alterok.mausamlive.core.data.mapper.toDomain
import com.alterok.mausamlive.core.data.mapper.toEntity
import com.alterok.mausamlive.core.data.remote.MausamApiService
import com.alterok.mausamlive.core.data.remote.model.ApiMausamInfo
import com.alterok.mausamlive.core.domain.model.LocalityDomainModel
import com.alterok.mausamlive.core.domain.model.MausamDomainModel
import com.alterok.mausamlive.core.domain.repository.MausamRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

private const val TAG = "MausamRepositoryImpl"

class MausamRepositoryImpl(
    db: MausamDatabase,
    private val mausamApi: MausamApiService,
) :    MausamRepository {

    private val mausamDao = db.getMausamDao()
    private val refreshInterval:Long = DateUtils.MINUTE_IN_MILLIS * 5

    override suspend fun getMousamByLocality(locality: LocalityDomainModel): Flow<DataResult<MausamDomainModel>> =
        flow {
            emit(DataResult.Loading())

            val cachedMausamData = mausamDao.getMausamByLocalityId(locality.localityId)

            if (cachedMausamData == null || isStale(cachedMausamData.fetchedAt)) {
                val mausamFromRemote = fetchMousamFromApi(locality.localityId)
                mausamFromRemote.map {
                    val mausamEntity = it.toEntity(locality.localityId)
                    //cache the data
                    mausamDao.addMausamData(mausamEntity)
                    mausamEntity.toDomain(locality)
                }.apply {
                    this.onFailure { iError, mausamDomainModel ->
                        emit(DataResult.Failure(iError, cachedMausamData?.toDomain(locality)))
                    }.onSuccess {
                        emit(this)
                    }
                }
            } else {
                //cache found and is valid.
                emit(cachedMausamData.toDomain(locality).wrapInSuccessDataResult())
            }
        }

    private fun isStale(fetchedAt: Long): Boolean {
        return (fetchedAt + refreshInterval) <= System.currentTimeMillis()
    }

    private suspend fun fetchMousamFromApi(localityId: String): DataResult<ApiMausamInfo> {

        val apiResponseResult = runCatchingForDataResult {
            mausamApi.getMausamByLocalityId(localityId)
        }

        val result: DataResult<ApiMausamInfo> = if (apiResponseResult.isSuccess) {
            val apiMausamResult = apiResponseResult.getOrNull().asDataResult()
            apiMausamResult.map {
                it.localityWeatherData
            }
        } else if (apiResponseResult is DataResult.Failure<*, DataResult.IError>) {
            DataResult.Failure(apiResponseResult.error)
        } else {
            DataResult.Failure(NetworkResultError.fromCode(404))
        }

        return result
    }
}