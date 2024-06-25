package com.alterok.mausamlive.core.base.mapper

interface BaseDomainEntityModelMapper<Domain, Entity> {
    fun toDomain(entity: Entity): Domain
    fun toEntity(domain: Domain): Entity
}