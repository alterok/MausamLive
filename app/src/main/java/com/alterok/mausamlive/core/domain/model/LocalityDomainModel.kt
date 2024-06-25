package com.alterok.mausamlive.core.domain.model

data class LocalityDomainModel(
    val city: String,
    val localityName: String,
    val localityId: String,
    val latitude: String,
    val longitude: String,
    val deviceType: Int
)