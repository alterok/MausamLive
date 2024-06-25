package com.alterok.mausamlive.core.di

import android.content.Context
import android.content.SharedPreferences
import com.alterok.mausamlive.BuildConfig
import com.alterok.mausamlive.core.constant.AppConstants
import com.alterok.mausamlive.core.constant.NetworkConstants
import com.alterok.mausamlive.core.data.remote.ApiKeyManager
import com.alterok.mausamlive.core.data.remote.MausamApiService
import com.alterok.mausamlive.core.data.remote.okhttp.MousamApiInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.applicationContext.getSharedPreferences(
            AppConstants.TAG_PREF_FILE_APP, Context.MODE_PRIVATE
        )
    }

    @Provides
    @Singleton
    fun providesApiKeyManager(sharedPreferences: SharedPreferences): ApiKeyManager {
        return ApiKeyManager(sharedPreferences)
    }

    @Provides
    @Singleton
    fun providesMousamApiInterceptor(apiKeyManager: ApiKeyManager): MousamApiInterceptor {
        return MousamApiInterceptor(apiKeyManager)
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(mousamApiServiceInterceptor: MousamApiInterceptor): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(mousamApiServiceInterceptor).apply {
            addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
            })
        }.build()

    @Provides
    @Singleton
    fun providesRetrofitMousamService(okHttpClient: OkHttpClient): MausamApiService =
        Retrofit.Builder().client(okHttpClient)
            .baseUrl(NetworkConstants.BASE_URL_API_SERVICE_MOUSAM)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(MausamApiService::class.java)
}