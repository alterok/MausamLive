package com.alterok.mausamlive.core.di

import com.alterok.mausamlive.core.domain.repository.LocalityRepository
import com.alterok.mausamlive.core.domain.repository.MausamRepository
import com.alterok.mausamlive.core.domain.usecase.GetAllLocalitiesUseCase
import com.alterok.mausamlive.core.domain.usecase.GetMausamByLocalityUseCase
import com.alterok.mausamlive.dashboard.domain.repository.CompassRepository
import com.alterok.mausamlive.dashboard.domain.usecase.GetCompassRotationUseCase
import com.alterok.mausamlive.dashboard.domain.usecase.StopCompassUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun providesGetMousamByLocalityIdUsecase(repository: MausamRepository): GetMausamByLocalityUseCase =
        GetMausamByLocalityUseCase(repository)


    @Provides
    @Singleton
    fun providesGetAllLocalitiesUsecase(repository: LocalityRepository): GetAllLocalitiesUseCase =
        GetAllLocalitiesUseCase(repository)


    @Provides
    @Singleton
    fun providesGetCompassRotationUsecase(compassRepository: CompassRepository): GetCompassRotationUseCase =
        GetCompassRotationUseCase(compassRepository)

    @Provides
    @Singleton
    fun providesStopCompassUsecase(compassRepository: CompassRepository): StopCompassUseCase =
        StopCompassUseCase(compassRepository)
}