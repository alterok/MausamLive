package com.alterok.mausamlive.core.di

import com.alterok.mausamlive.core.data.mapper.MousamInfoDomainApiMapper
import com.alterok.mausamlive.core.presentation.mapper.LocalityDomainUIMapper
import com.alterok.mausamlive.core.presentation.mapper.MausamDomainUIMapper
import com.alterok.mausamlive.core.presentation.mapper.MousamInfoDomainUIMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapperModule {

    @Provides
    @Singleton
    fun providesLocalityDomainUIMapper(): LocalityDomainUIMapper = LocalityDomainUIMapper()

    @Provides
    @Singleton
    fun providesMousamInfoDomainUIMapper(): MousamInfoDomainUIMapper = MousamInfoDomainUIMapper()

    @Provides
    @Singleton
    fun providesMousamDomainUIMapper(
        localityMapper: LocalityDomainUIMapper,
        mousamInfoMapper: MousamInfoDomainUIMapper,
    ): MausamDomainUIMapper = MausamDomainUIMapper(
        localityMapper,
        mousamInfoMapper
    )

    @Provides
    @Singleton
    fun providesMousamInfoDomainApiMapper(): MousamInfoDomainApiMapper = MousamInfoDomainApiMapper()

}