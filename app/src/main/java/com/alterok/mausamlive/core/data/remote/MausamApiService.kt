package com.alterok.mausamlive.core.data.remote

import com.alterok.mausamlive.core.data.remote.model.ApiMousamResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MausamApiService {
    @GET("gw/weather/external/v0/get_locality_weather_data")
    suspend fun getMausamByLocalityId(@Query("locality_id") localityId: String): Response<ApiMousamResponse>
}