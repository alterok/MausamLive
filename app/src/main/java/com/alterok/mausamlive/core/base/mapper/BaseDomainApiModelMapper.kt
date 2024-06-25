package com.alterok.mausamlive.core.base.mapper

interface BaseDomainApiModelMapper<D, A> {
    fun toApiModel(domainModel: D): A
    fun toDomainModel(apiModel: A): D
}