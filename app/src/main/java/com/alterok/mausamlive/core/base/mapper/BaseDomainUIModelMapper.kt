package com.alterok.mausamlive.core.base.mapper

interface BaseDomainUIModelMapper<D, U> {
    fun toUIModel(domainModel: D): U
    fun toDomainModel(uiModel: U): D
}