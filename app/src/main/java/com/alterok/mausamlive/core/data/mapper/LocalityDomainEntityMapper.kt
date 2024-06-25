package com.alterok.mausamlive.core.data.mapper

import com.alterok.mausamlive.core.data.local.model.LocalityEntity
import com.alterok.mausamlive.core.domain.model.LocalityDomainModel


internal fun LocalityEntity.toDomain(): LocalityDomainModel {
    return LocalityDomainModel(
        city, localityName, localityId, latitude, longitude, deviceType
    )
}

internal fun LocalityDomainModel.toEntity(domain: LocalityDomainModel): LocalityEntity {
    return LocalityEntity(
        city,
        localityName,
        localityId,
        latitude,
        longitude,
        deviceType
    )
}