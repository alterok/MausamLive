package com.alterok.mausamlive.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.alterok.mausamlive.core.data.local.model.LocalityEntity
import com.alterok.mausamlive.core.data.local.model.LocalityWithMausam
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalityDao {
    @Insert
    suspend fun addLocality(locality: LocalityEntity)

    @Insert
    suspend fun addLocalities(localities: List<LocalityEntity>)

    @Query("Select * from localities")
    fun getAllLocalities(): Flow<List<LocalityEntity>>

    @Query("Select * from localities where localityId=:localityId")
    suspend fun getLocalityById(localityId: String): LocalityEntity?

    @Query("Select * from localities")
    fun getAllLocalitiesWithMausam(): Flow<List<LocalityWithMausam>>
}