package com.alterok.mausamlive.core.di

import android.app.Application
import com.alterok.mausamlive.core.MousamLiveApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesApplication(): Application = MousamLiveApplication()
}