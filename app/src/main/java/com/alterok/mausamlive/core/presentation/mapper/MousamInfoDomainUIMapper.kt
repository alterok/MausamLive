package com.alterok.mausamlive.core.presentation.mapper

import com.alterok.mausamlive.core.base.mapper.BaseDomainUIModelMapper
import com.alterok.mausamlive.core.domain.model.MousamInfoDomainModel
import com.alterok.mausamlive.core.ui.model.MausamInfoUIModel

class MousamInfoDomainUIMapper : BaseDomainUIModelMapper<MousamInfoDomainModel, MausamInfoUIModel> {
    override fun toUIModel(domainModel: MousamInfoDomainModel): MausamInfoUIModel {
        return with(domainModel) {
            MausamInfoUIModel(
                temperature = temperature,
                humidity = humidity,
                windSpeed = windSpeed,
                windDirection = windDirection,
                rainIntensity = rainIntensity,
                rainAccumulation = rainAccumulation,
                fetchedAt = fetchedAt
            )
        }
    }

    override fun toDomainModel(uiModel: MausamInfoUIModel): MousamInfoDomainModel {
        return with(uiModel) {
            MousamInfoDomainModel(
                temperature = temperature,
                humidity = humidity,
                windSpeed = windSpeed,
                windDirection = windDirection,
                rainIntensity = rainIntensity,
                rainAccumulation = rainAccumulation,
                fetchedAt = fetchedAt
            )
        }
    }
}