package com.alterok.mausamlive.core.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity("Localities")
data class LocalityEntity(
    @SerializedName("city")
    val city: String,
    @SerializedName("locality_name")
    val localityName: String,
    @PrimaryKey
    @SerializedName("locality_id")
    val localityId: String,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("device_type")
    val deviceType: Int,
)
