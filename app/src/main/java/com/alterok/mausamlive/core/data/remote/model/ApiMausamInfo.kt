package com.alterok.mausamlive.core.data.remote.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class ApiMausamInfo(
    @SerializedName("temperature")
    @Expose
    val temperature: Double?,
    @SerializedName("humidity")
    @Expose
    val humidity: Double?,
    @SerializedName("wind_speed")
    @Expose
    val windSpeed: Double?,
    @SerializedName("wind_direction")
    @Expose
    val windDirection: Double?,
    @SerializedName("rain_accumulation")
    @Expose
    val rainAccumulation: Double?,
    @SerializedName("rain_intensity")
    @Expose
    val rainIntensity: Double?,
)