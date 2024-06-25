package com.alterok.mausamlive.core.util

import android.graphics.Color
import com.alterok.mausamlive.core.constant.Colors

sealed class TintScheme(
    val strokeColor: Int,
    val backgroundColor: Int,
    val foregroundColor: Int
) {
    data object Error : TintScheme(Color.RED, Color.RED, Color.RED)
    // Temperature levels
    sealed class Temperature(
        strokeColor: Int, backgroundColor: Int, foregroundColor: Int
    ) : TintScheme(strokeColor, backgroundColor, foregroundColor) {
        data object VeryHighLight : Temperature(Colors.Light.tempVeryHigh, Colors.Light.tempVeryHigh, Colors.Light.tempVeryHigh)
        data object HighLight : Temperature(Colors.Light.tempHigh, Colors.Light.tempHigh, Colors.Light.tempHigh)
        data object MediumLight : Temperature(Colors.Light.tempMedium, Colors.Light.tempMedium, Colors.Light.tempMedium)
        data object LowLight : Temperature(Colors.Light.tempLow, Colors.Light.tempLow, Colors.Light.tempLow)
        data object VeryLowLight : Temperature(Colors.Light.tempVeryLow, Colors.Light.tempVeryLow, Colors.Light.tempVeryLow)

        data object VeryHighDark : Temperature(Colors.Dark.tempVeryHigh, Colors.Dark.tempVeryHigh, Colors.Dark.tempVeryHigh)
        data object HighDark : Temperature(Colors.Dark.tempHigh, Colors.Dark.tempHigh, Colors.Dark.tempHigh)
        data object MediumDark : Temperature(Colors.Dark.tempMedium, Colors.Dark.tempMedium, Colors.Dark.tempMedium)
        data object LowDark : Temperature(Colors.Dark.tempLow, Colors.Dark.tempLow, Colors.Dark.tempLow)
        data object VeryLowDark : Temperature(Colors.Dark.tempVeryLow, Colors.Dark.tempVeryLow, Colors.Dark.tempVeryLow)
    }

    // Wind levels
    sealed class Wind(
        strokeColor: Int, backgroundColor: Int, foregroundColor: Int
    ) : TintScheme(strokeColor, backgroundColor, foregroundColor) {
        data object VeryHighLight : Wind(Colors.Light.windVeryHigh, Colors.Light.windVeryHigh, Colors.Light.windVeryHigh)
        data object HighLight : Wind(Colors.Light.windHigh, Colors.Light.windHigh, Colors.Light.windHigh)
        data object MediumLight : Wind(Colors.Light.windMedium, Colors.Light.windMedium, Colors.Light.windMedium)
        data object LowLight : Wind(Colors.Light.windLow, Colors.Light.windLow, Colors.Light.windLow)
        data object VeryLowLight : Wind(Colors.Light.windVeryLow, Colors.Light.windVeryLow, Colors.Light.windVeryLow)

        data object VeryHighDark : Wind(Colors.Dark.windVeryHigh, Colors.Dark.windVeryHigh, Colors.Dark.windVeryHigh)
        data object HighDark : Wind(Colors.Dark.windHigh, Colors.Dark.windHigh, Colors.Dark.windHigh)
        data object MediumDark : Wind(Colors.Dark.windMedium, Colors.Dark.windMedium, Colors.Dark.windMedium)
        data object LowDark : Wind(Colors.Dark.windLow, Colors.Dark.windLow, Colors.Dark.windLow)
        data object VeryLowDark : Wind(Colors.Dark.windVeryLow, Colors.Dark.windVeryLow, Colors.Dark.windVeryLow)
    }

    // Humidity levels
    sealed class Humidity(
        strokeColor: Int, backgroundColor: Int, foregroundColor: Int
    ) : TintScheme(strokeColor, backgroundColor, foregroundColor) {
        data object VeryHighLight : Humidity(Colors.Light.humidityVeryHigh, Colors.Light.humidityVeryHigh, Colors.Light.humidityVeryHigh)
        data object HighLight : Humidity(Colors.Light.humidityHigh, Colors.Light.humidityHigh, Colors.Light.humidityHigh)
        data object MediumLight : Humidity(Colors.Light.humidityMedium, Colors.Light.humidityMedium, Colors.Light.humidityMedium)
        data object LowLight : Humidity(Colors.Light.humidityLow, Colors.Light.humidityLow, Colors.Light.humidityLow)
        data object VeryLowLight : Humidity(Colors.Light.humidityVeryLow, Colors.Light.humidityVeryLow, Colors.Light.humidityVeryLow)

        data object VeryHighDark : Humidity(Colors.Dark.humidityVeryHigh, Colors.Dark.humidityVeryHigh, Colors.Dark.humidityVeryHigh)
        data object HighDark : Humidity(Colors.Dark.humidityHigh, Colors.Dark.humidityHigh, Colors.Dark.humidityHigh)
        data object MediumDark : Humidity(Colors.Dark.humidityMedium, Colors.Dark.humidityMedium, Colors.Dark.humidityMedium)
        data object LowDark : Humidity(Colors.Dark.humidityLow, Colors.Dark.humidityLow, Colors.Dark.humidityLow)
        data object VeryLowDark : Humidity(Colors.Dark.humidityVeryLow, Colors.Dark.humidityVeryLow, Colors.Dark.humidityVeryLow)
    }

    // Rain Intensity levels
    sealed class RainIntensity(
        strokeColor: Int, backgroundColor: Int, foregroundColor: Int
    ) : TintScheme(strokeColor, backgroundColor, foregroundColor) {
        data object VeryHighLight : RainIntensity(Colors.Light.rainIntensityVeryHigh, Colors.Light.rainIntensityVeryHigh, Colors.Light.rainIntensityVeryHigh)
        data object HighLight : RainIntensity(Colors.Light.rainIntensityHigh, Colors.Light.rainIntensityHigh, Colors.Light.rainIntensityHigh)
        data object MediumLight : RainIntensity(Colors.Light.rainIntensityMedium, Colors.Light.rainIntensityMedium, Colors.Light.rainIntensityMedium)
        data object LowLight : RainIntensity(Colors.Light.rainIntensityLow, Colors.Light.rainIntensityLow, Colors.Light.rainIntensityLow)
        data object VeryLowLight : RainIntensity(Colors.Light.rainIntensityVeryLow, Colors.Light.rainIntensityVeryLow, Colors.Light.rainIntensityVeryLow)

        data object VeryHighDark : RainIntensity(Colors.Dark.rainIntensityVeryHigh, Colors.Dark.rainIntensityVeryHigh, Colors.Dark.rainIntensityVeryHigh)
        data object HighDark : RainIntensity(Colors.Dark.rainIntensityHigh, Colors.Dark.rainIntensityHigh, Colors.Dark.rainIntensityHigh)
        data object MediumDark : RainIntensity(Colors.Dark.rainIntensityMedium, Colors.Dark.rainIntensityMedium, Colors.Dark.rainIntensityMedium)
        data object LowDark : RainIntensity(Colors.Dark.rainIntensityLow, Colors.Dark.rainIntensityLow, Colors.Dark.rainIntensityLow)
        data object VeryLowDark : RainIntensity(Colors.Dark.rainIntensityVeryLow, Colors.Dark.rainIntensityVeryLow, Colors.Dark.rainIntensityVeryLow)
    }

    // Total Rain levels
    sealed class TotalRain(
        strokeColor: Int, backgroundColor: Int, foregroundColor: Int
    ) : TintScheme(strokeColor, backgroundColor, foregroundColor) {
        data object VeryHighLight : TotalRain(Colors.Light.totalRainVeryHigh, Colors.Light.totalRainVeryHigh, Colors.Light.totalRainVeryHigh)
        data object HighLight : TotalRain(Colors.Light.totalRainHigh, Colors.Light.totalRainHigh, Colors.Light.totalRainHigh)
        data object MediumLight : TotalRain(Colors.Light.totalRainMedium, Colors.Light.totalRainMedium, Colors.Light.totalRainMedium)
        data object LowLight : TotalRain(Colors.Light.totalRainLow, Colors.Light.totalRainLow, Colors.Light.totalRainLow)
        data object VeryLowLight : TotalRain(Colors.Light.totalRainVeryLow, Colors.Light.totalRainVeryLow, Colors.Light.totalRainVeryLow)

        data object VeryHighDark : TotalRain(Colors.Dark.totalRainVeryHigh, Colors.Dark.totalRainVeryHigh, Colors.Dark.totalRainVeryHigh)
        data object HighDark : TotalRain(Colors.Dark.totalRainHigh, Colors.Dark.totalRainHigh, Colors.Dark.totalRainHigh)
        data object MediumDark : TotalRain(Colors.Dark.totalRainMedium, Colors.Dark.totalRainMedium, Colors.Dark.totalRainMedium)
        data object LowDark : TotalRain(Colors.Dark.totalRainLow, Colors.Dark.totalRainLow, Colors.Dark.totalRainLow)
        data object VeryLowDark : TotalRain(Colors.Dark.totalRainVeryLow, Colors.Dark.totalRainVeryLow, Colors.Dark.totalRainVeryLow)
    }
}
