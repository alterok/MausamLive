package com.alterok.mausamlive.core.presentation.mapper

import com.alterok.mausamlive.core.base.mapper.BaseDomainUIModelMapper
import com.alterok.mausamlive.core.domain.model.LocalityDomainModel
import com.alterok.mausamlive.core.ui.model.LocalityUIModel

class LocalityDomainUIMapper : BaseDomainUIModelMapper<LocalityDomainModel, LocalityUIModel> {
    override fun toUIModel(domainModel: LocalityDomainModel): LocalityUIModel {
        return with(domainModel) {
            LocalityUIModel(
                city = city,
                localityName = localityName,
                localityId = localityId,
                latitude = latitude,
                longitude = longitude,
                deviceType = deviceType
            )
        }
    }

    override fun toDomainModel(uiModel: LocalityUIModel): LocalityDomainModel {
        return with(uiModel) {
            LocalityDomainModel(
                city = city,
                localityName = localityName,
                localityId = localityId,
                latitude = latitude,
                longitude = longitude,
                deviceType = deviceType
            )
        }
    }
}