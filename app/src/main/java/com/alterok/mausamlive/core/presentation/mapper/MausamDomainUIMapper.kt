package com.alterok.mausamlive.core.presentation.mapper

import com.alterok.mausamlive.core.base.mapper.BaseDomainUIModelMapper
import com.alterok.mausamlive.core.domain.model.MausamDomainModel
import com.alterok.mausamlive.core.ui.model.MausamUIModel

class MausamDomainUIMapper(
    val localityDomainUIMapper: LocalityDomainUIMapper,
    val mousamInfoDomainUIMapper: MousamInfoDomainUIMapper,
) : BaseDomainUIModelMapper<MausamDomainModel, MausamUIModel> {
    override fun toUIModel(domainModel: MausamDomainModel): MausamUIModel {
        return MausamUIModel(
            localityDomainUIMapper.toUIModel(domainModel.locality),
            mousamInfoDomainUIMapper.toUIModel(domainModel.mousamInfo)
        )
    }

    override fun toDomainModel(uiModel: MausamUIModel): MausamDomainModel {
        return MausamDomainModel(
            localityDomainUIMapper.toDomainModel(uiModel.locality),
            mousamInfoDomainUIMapper.toDomainModel(uiModel.mausamInfo)
        )
    }
}