package com.alterok.mausamlive.core.ui.model

import java.util.Date

data class MausamInfoUIModel(
    val temperature: Double,
    val humidity: Double,
    val windSpeed: Double,
    val windDirection: Double,
    val rainIntensity: Double,
    val rainAccumulation: Double,
    val fetchedAt: Long,
) {
    val temperatureRounded = temperature.toInt()
    val humidityRounded = humidity.toInt()
    val windSpeedRounded = windSpeed
    val windDirectionRounded = windDirection.toInt()
    val rainIntensityRounded = rainIntensity
    val rainAccumulationRounded = rainAccumulation

    enum class TemperatureLevel {
        VERY_LOW,
        LOW,
        MEDIUM,
        HIGH,
        VERY_HIGH;

        companion object {
            fun fromTemperature(temperature: Double) = when {
                temperature.isNaN() -> VERY_LOW
                temperature <= -10.0 -> VERY_LOW
                temperature <= 10.0 -> LOW
                temperature <= 30.0 -> MEDIUM
                temperature <= 40 -> HIGH
                else -> VERY_HIGH
            }
        }
    }

    fun hasEmptyInfo() = listOf(
        temperature,
        humidity,
        windSpeed,
        windDirection,
        rainIntensity,
        rainAccumulation
    ).all { it.isNaN() }

    fun hasOnlyRainSensors() = temperature.isNaN() && rainIntensity.isNaN().not()
    fun hasAllSensors() = listOf(
        temperature,
        humidity,
        windSpeed,
        windDirection,
        rainIntensity,
        rainAccumulation
    ).all { it.isNaN().not() }

    val windDirectionStr = "${windDirection.toInt()}° ".plus(
        when {
            windDirection > 360.0 -> "INVALID"
            windDirection >= 337.5 || windDirection < 22.5 -> "N"
            windDirection >= 22.5 && windDirection < 67.5 -> "NE"
            windDirection >= 67.5 && windDirection < 112.5 -> "E"
            windDirection >= 112.5 && windDirection < 157.5 -> "SE"
            windDirection >= 157.5 && windDirection < 202.5 -> "S"
            windDirection >= 202.5 && windDirection < 247.5 -> "SW"
            windDirection >= 247.5 && windDirection < 292.5 -> "W"
            else -> "NW"
        }
    )

    fun updatedAtStr() = ((Date().time - fetchedAt) / 1000).toInt().run {
        if (this >= 60) {
            "Updated " + (this / 60).let { if (it == 1) "$it min" else "$it mins" } + " ago"
        } else {
            "Updated $this seconds ago"
        }
    }

    fun temperatureRoundedStr() = temperatureRounded.toString().plus("°")

    fun humidityRoundedStr() = humidityRounded.toString().plus("%")

    val mausamSummary = generateMousamSummaryVersionCohesiveNatural(this)

    private fun generateMousamSummaryVersionCohesiveNatural(weatherData: MausamInfoUIModel): String {
        val temperatureText = when {
            weatherData.temperature.isNaN() -> ""
            weatherData.temperature < -10 -> "The cold snap continues, with temperatures plunging below zero. It's colder than a Kashmiri winter out there at ${weatherData.temperatureRounded}°C."
            weatherData.temperature < 0 -> "Brrr! Bundle up, it's freezing outside. The temperature is ${weatherData.temperatureRounded}°C."
            weatherData.temperature in 0.0..10.0 -> "It's a bit chilly, but nothing a warm sweater can't handle. The temperature is ${weatherData.temperatureRounded}°C."
            weatherData.temperature in 10.0..20.0 -> "It's cool and pleasant, perfect for a leisurely stroll. The temperature is ${weatherData.temperatureRounded}°C."
            weatherData.temperature in 20.0..30.0 -> "It's warm and comfortable, just the right weather for outdoor activities. The temperature is ${weatherData.temperatureRounded}°C."
            weatherData.temperature in 30.0..40.0 -> "It's getting hot out there! Stay cool and hydrated. The temperature is ${weatherData.temperatureRounded}°C."
            else -> "It's scorching outside! Seek shade and stay hydrated. The temperature is ${weatherData.temperatureRounded}°C."
        }

        val windText = when {
            weatherData.windSpeed.isNaN() -> ""
            weatherData.windSpeed < 1.0 -> "The air is calm, with barely a breeze stirring."
            weatherData.windSpeed < 5.0 -> "There's a gentle breeze blowing through the trees."
            weatherData.windSpeed < 10.0 -> "Hold onto your hats! The wind is picking up speed."
            weatherData.windSpeed < 15.0 -> "It's very windy out there, with gusts of wind swirling around."
            else -> "Whoa! Strong winds are sweeping across the region."
        }

        val humidityText = when {
            weatherData.humidity.isNaN() -> ""
            weatherData.humidity < 30 -> "The air is dry, with humidity levels dropping."
            weatherData.humidity in 30.0..60.0 -> "Humidity levels are moderate, making for comfortable conditions."
            else -> "It's quite humid out there, with moisture hanging in the air."
        }

        val rainText = when {
            weatherData.rainIntensity.isNaN() -> ""
            weatherData.rainIntensity == 0.0 -> "No rain expected for now, enjoy the dry spell."
            weatherData.rainIntensity < 2.0 -> "There's a light drizzle, barely enough to dampen the ground."
            weatherData.rainIntensity < 10.0 -> "Rain showers are moving in, so don't forget your umbrella."
            else -> "It's raining heavily, with precipitation rates picking up."
        }

        val totalRainText = when {
            weatherData.rainAccumulation.isNaN() -> ""
            weatherData.rainAccumulation == 0.0 -> "No significant rainfall recorded since midnight."
            weatherData.rainAccumulation < 5.0 -> "Since midnight, only a few drops of rain have fallen."
            weatherData.rainAccumulation < 20.0 -> "We've had some rain since midnight, but nothing too heavy."
            else -> "Since midnight, there has been substantial rainfall, so watch out for waterlogged areas."
        }

        return "$temperatureText $windText $humidityText $rainText $totalRainText".trim()
    }

    fun generateWeatherPhrase(): String {
        val weatherData: MausamInfoUIModel = this
        val temperaturePhrase = when {
            weatherData.temperature.isNaN() -> ""
            weatherData.temperature in -60.0..0.0 -> "Freezing"
            weatherData.temperature in 0.0..10.0 -> "Cold"
            weatherData.temperature in 10.0..20.0 -> "Cool"
            weatherData.temperature in 20.0..30.0 -> "Warm"
            weatherData.temperature in 30.0..40.0 -> "Hot"
            else -> "Scorching"
        }

        val windPhrase = when {
            weatherData.windSpeed.isNaN() -> ""
            weatherData.windSpeed < 5.0 -> "Breezy"
            weatherData.windSpeed < 10.0 -> "Windy"
            weatherData.windSpeed < 15.0 -> "Very Windy"
            else -> "Strong Winds"
        }

        val humidityPhrase = when {
            weatherData.humidity.isNaN() -> ""
            weatherData.humidity < 30 -> "Dry"
            weatherData.humidity in 30.0..60.0 -> ""
            else -> "Humid"
        }

        val rainPhrase = when {
            weatherData.rainIntensity.isNaN() -> ""
            weatherData.rainIntensity < 2.0 -> "Drizzling"
            weatherData.rainIntensity < 10.0 -> "Raining"
            else -> "Pouring"
        }

        val primaryConditions =
            listOf(rainPhrase, windPhrase, humidityPhrase).filter { it.isNotEmpty() }
        val primaryCondition = primaryConditions.firstOrNull() ?: ""
        val phrase =
            if (primaryCondition.isNotEmpty()) "$temperaturePhrase & $primaryCondition" else temperaturePhrase

        return phrase
    }

}