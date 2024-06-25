package com.alterok.mausamlive.core.data.mapper

import com.alterok.mausamlive.core.data.local.model.MausamEntity
import com.alterok.mausamlive.core.data.remote.model.ApiMausamInfo
import java.util.Date

internal fun ApiMausamInfo.toEntity(localityId: String): MausamEntity {
    return MausamEntity(
        localityId,
        temperature ?: Double.NaN,
        humidity ?: Double.NaN,
        windSpeed ?: Double.NaN,
        windDirection ?: Double.NaN,
        rainIntensity ?: Double.NaN,
        rainAccumulation ?: Double.NaN,
        Date().time
    )
}
