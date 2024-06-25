package com.alterok.mausamlive.core.data.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ApiMousamResponse(
    @SerializedName("device_type")
    @Expose
    val deviceType: Int?,
    @SerializedName("locality_weather_data")
    @Expose
    val localityWeatherData: ApiMausamInfo?,
    @SerializedName("message")
    @Expose
    val message: String,
    @SerializedName("status")
    @Expose
    val status: String,
){
    companion object{
        const val TEMPORARILY_UNAVAILABLE = "temporarily unavailable"
    }
}