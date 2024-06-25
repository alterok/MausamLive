package com.alterok.mausamlive.core.di

import android.content.Context
import com.alterok.mausamlive.core.data.local.CsvReader
import com.alterok.mausamlive.core.data.local.LocalityDataCsvReaderImpl
import com.alterok.mausamlive.core.data.local.MausamDatabase
import com.alterok.mausamlive.core.data.local.model.LocalityEntity
import com.alterok.mausamlive.core.data.remote.MausamApiService
import com.alterok.mausamlive.core.data.repository.LocalityRepositoryImpl
import com.alterok.mausamlive.core.data.repository.MausamRepositoryImpl
import com.alterok.mausamlive.core.domain.repository.LocalityRepository
import com.alterok.mausamlive.core.domain.repository.MausamRepository
import com.alterok.mausamlive.dashboard.data.repository.CompassRepositoryImpl
import com.alterok.mausamlive.dashboard.domain.repository.CompassRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providesLocalityRepository(
        @ApplicationContext context: Context,
        database: MausamDatabase,
        localityDataCsvReader: CsvReader<List<LocalityEntity>>,
    ): LocalityRepository = LocalityRepositoryImpl(
        context,
        database,
        localityDataCsvReader
    )

    @Provides
    @Singleton
    fun providesMousamRepository(
        database: MausamDatabase,
        apiService: MausamApiService,
    ): MausamRepository = MausamRepositoryImpl(
        database,
        apiService
    )

    @Provides
    @Singleton
    fun providesCompassRepository(
        @ApplicationContext context: Context,
    ): CompassRepository = CompassRepositoryImpl(context)

    @Provides
    @Singleton
    fun providesLocalityReaderFromFile(): LocalityDataCsvReaderImpl {
        return LocalityDataCsvReaderImpl()
    }

}