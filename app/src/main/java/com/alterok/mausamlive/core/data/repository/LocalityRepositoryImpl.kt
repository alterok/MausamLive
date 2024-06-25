package com.alterok.mausamlive.core.data.repository

import android.content.Context
import android.util.Log
import com.alterok.dataresult.DataResult
import com.alterok.dataresult.wrapInSuccessDataResult
import com.alterok.kotlin_essential_extensions.ifFalse
import com.alterok.mausamlive.core.data.local.CsvReader
import com.alterok.mausamlive.core.data.local.MausamDatabase
import com.alterok.mausamlive.core.data.local.model.LocalityEntity
import com.alterok.mausamlive.core.data.mapper.toDomain
import com.alterok.mausamlive.core.domain.model.LocalityDomainModel
import com.alterok.mausamlive.core.domain.repository.LocalityRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "LocalityRepositoryImpl"

class LocalityRepositoryImpl @Inject constructor(
    context: Context,
    private val db: MausamDatabase,
    private val localityCsvReader: CsvReader<List<LocalityEntity>>
) : LocalityRepository {
    private val repoScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var localityFileReaderJob:Job? = null

    init {
        Log.i(TAG, "localityrespositoryImpl: init()")
        localityFileReaderJob = repoScope.launch {
            hasLocalities().collectLatest {
                it.ifFalse {
                    val localities = localityCsvReader.getData(context)
                    Log.i(TAG, "readLocalitiesFromCSV: $localities")
                    db.getLocalityDao().addLocalities(localities)

                    localityFileReaderJob?.cancel()
                }
            }
        }
    }


    private fun hasLocalities() = db.getLocalityDao().getAllLocalities().map {
        it.isNotEmpty()
    }

    override suspend fun getAllLocalities(): Flow<DataResult<List<LocalityDomainModel>>> = channelFlow {
        send(DataResult.Loading())

        db.getLocalityDao().getAllLocalities().collectLatest {
            send(it.map { it.toDomain() }.wrapInSuccessDataResult())
        }
    }
}