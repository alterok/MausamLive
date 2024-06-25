package com.alterok.mausamlive.core.domain.model

data class MousamInfoDomainModel(
    val temperature: Double,
    val humidity: Double,
    val windSpeed: Double,
    val windDirection: Double,
    val rainIntensity: Double,
    val rainAccumulation: Double,
    val fetchedAt: Long,
)