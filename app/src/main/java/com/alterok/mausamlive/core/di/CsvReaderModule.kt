package com.alterok.mausamlive.core.di

import com.alterok.mausamlive.core.data.local.CsvReader
import com.alterok.mausamlive.core.data.local.LocalityDataCsvReaderImpl
import com.alterok.mausamlive.core.data.local.model.LocalityEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CsvReaderModule {
    @Provides
    @Singleton
    fun providesLocalityCsvReader(): CsvReader<List<LocalityEntity>> = LocalityDataCsvReaderImpl()
}