package com.alterok.mausamlive.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alterok.mausamlive.core.data.local.model.MausamEntity

@Dao
interface MausamDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMausamData(mausam: MausamEntity)

    @Query("Select * from mausam_data where localityId=:localityId")
    suspend fun getMausamByLocalityId(localityId: String): MausamEntity?
}