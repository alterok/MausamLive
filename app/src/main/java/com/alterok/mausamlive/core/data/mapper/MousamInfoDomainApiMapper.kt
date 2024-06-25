package com.alterok.mausamlive.core.data.mapper

import com.alterok.mausamlive.core.base.mapper.BaseDomainApiModelMapper
import com.alterok.mausamlive.core.data.remote.model.ApiMausamInfo
import com.alterok.mausamlive.core.domain.model.MousamInfoDomainModel
import java.util.Date

class MousamInfoDomainApiMapper : BaseDomainApiModelMapper<MousamInfoDomainModel, ApiMausamInfo> {
    override fun toApiModel(domainModel: MousamInfoDomainModel): ApiMausamInfo {
        return with(domainModel) {
            ApiMausamInfo(
                temperature = temperature,
                humidity = humidity,
                windSpeed = windSpeed,
                windDirection = windDirection,
                rainAccumulation = rainAccumulation,
                rainIntensity = rainIntensity
            )
        }
    }

    override fun toDomainModel(apiModel: ApiMausamInfo): MousamInfoDomainModel {
        return with(apiModel) {
            MousamInfoDomainModel(
                temperature = temperature ?: Double.NaN,
                humidity = humidity ?: Double.NaN,
                windSpeed = windSpeed ?: Double.NaN,
                windDirection = windDirection ?: Double.NaN,
                rainIntensity = rainIntensity ?: Double.NaN,
                rainAccumulation = rainAccumulation ?: Double.NaN,
                fetchedAt = Date().time
            )
        }
    }
}