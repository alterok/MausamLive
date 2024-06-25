package com.alterok.mausamlive.core.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alterok.mausamlive.core.data.local.MausamDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesMausamDatabase(@ApplicationContext context: Context): MausamDatabase {
        return Room.databaseBuilder(context, MausamDatabase::class.java, "db-mausamlive")
            .fallbackToDestructiveMigration()
            .build()
    }
}