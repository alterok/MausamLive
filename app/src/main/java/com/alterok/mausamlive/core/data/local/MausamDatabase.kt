package com.alterok.mausamlive.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alterok.mausamlive.core.data.local.dao.LocalityDao
import com.alterok.mausamlive.core.data.local.dao.MausamDao
import com.alterok.mausamlive.core.data.local.model.LocalityEntity
import com.alterok.mausamlive.core.data.local.model.MausamEntity

@Database(
    entities = [
        LocalityEntity::class,
        MausamEntity::class
    ], version = 1, exportSchema = false
)
abstract class MausamDatabase : RoomDatabase() {
    abstract fun getLocalityDao(): LocalityDao
    abstract fun getMausamDao(): MausamDao
}
