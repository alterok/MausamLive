package com.alterok.mausamlive.core.data.mapper

import com.alterok.mausamlive.core.data.local.model.LocalityEntity
import com.alterok.mausamlive.core.data.local.model.MausamEntity
import com.alterok.mausamlive.core.domain.model.LocalityDomainModel
import com.alterok.mausamlive.core.domain.model.MausamDomainModel
import com.alterok.mausamlive.core.domain.model.MousamInfoDomainModel


internal fun MausamEntity.toDomain(locality: LocalityEntity) = MausamDomainModel(
    locality = locality.toDomain(),
    mousamInfo = MousamInfoDomainModel(
        temperature ?: Double.NaN,
        humidity ?: Double.NaN,
        windSpeed ?: Double.NaN,
        windDirection ?: Double.NaN,
        rainIntensity ?: Double.NaN,
        rainAccumulation ?: Double.NaN,
        fetchedAt
    )
)
internal fun MausamEntity.toDomain(locality: LocalityDomainModel) = MausamDomainModel(
    locality = locality,
    mousamInfo = MousamInfoDomainModel(
        temperature ?: Double.NaN,
        humidity ?: Double.NaN,
        windSpeed ?: Double.NaN,
        windDirection ?: Double.NaN,
        rainIntensity ?: Double.NaN,
        rainAccumulation ?: Double.NaN,
        fetchedAt
    )
)