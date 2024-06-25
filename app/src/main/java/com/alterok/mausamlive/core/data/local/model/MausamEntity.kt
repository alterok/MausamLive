package com.alterok.mausamlive.core.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "mausam_data",
    foreignKeys = [ForeignKey(
        entity = LocalityEntity::class,
        parentColumns = ["localityId"],
        childColumns = ["localityId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class MausamEntity(
    @PrimaryKey
    @SerializedName("locality_id")
    val localityId: String,
    @SerializedName("temperature")
    val temperature: Double?,
    @SerializedName("humidity")
    val humidity: Double?,
    @SerializedName("wind_speed")
    val windSpeed: Double?,
    @SerializedName("wind_direction")
    val windDirection: Double?,
    @SerializedName("rain_intensity")
    val rainIntensity: Double?,
    @SerializedName("rain_accumulation")
    val rainAccumulation: Double?,
    @SerializedName("fetch_at")
    val fetchedAt: Long,
)