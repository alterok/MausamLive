package com.alterok.mausamlive.dashboard.ui.util

import com.alterok.mausamlive.core.util.TintScheme

class MausamTintHelper {
    companion object {
        // Temperature levels
        fun getTemperatureTint(tempValue: Double, isDarkMode: Boolean): TintScheme.Temperature {
            return when {
                tempValue >= 35.0 -> if (isDarkMode) TintScheme.Temperature.VeryHighDark else TintScheme.Temperature.VeryHighLight
                tempValue >= 30.0 -> if (isDarkMode) TintScheme.Temperature.HighDark else TintScheme.Temperature.HighLight
                tempValue >= 20.0 -> if (isDarkMode) TintScheme.Temperature.MediumDark else TintScheme.Temperature.MediumLight
                tempValue >= 10.0 -> if (isDarkMode) TintScheme.Temperature.LowDark else TintScheme.Temperature.LowLight
                else -> if (isDarkMode) TintScheme.Temperature.VeryLowDark else TintScheme.Temperature.VeryLowLight
            }
        }

        // Wind levels
        fun getWindTint(windSpeedValue: Double, isDarkMode: Boolean): TintScheme.Wind {
            return when {
                windSpeedValue >= 30.0 -> if (isDarkMode) TintScheme.Wind.VeryHighDark else TintScheme.Wind.VeryHighLight
                windSpeedValue >= 20.0 -> if (isDarkMode) TintScheme.Wind.HighDark else TintScheme.Wind.HighLight
                windSpeedValue >= 12.0 -> if (isDarkMode) TintScheme.Wind.MediumDark else TintScheme.Wind.MediumLight
                windSpeedValue >= 3.0 -> if (isDarkMode) TintScheme.Wind.LowDark else TintScheme.Wind.LowLight
                else -> if (isDarkMode) TintScheme.Wind.VeryLowDark else TintScheme.Wind.VeryLowLight
            }
        }

        // Humidity levels
        fun getHumidityTint(humidityValue: Double, isDarkMode: Boolean): TintScheme.Humidity {
            return when {
                humidityValue >= 80.0 -> if (isDarkMode) TintScheme.Humidity.VeryHighDark else TintScheme.Humidity.VeryHighLight
                humidityValue >= 60.0 -> if (isDarkMode) TintScheme.Humidity.HighDark else TintScheme.Humidity.HighLight
                humidityValue >= 40.0 -> if (isDarkMode) TintScheme.Humidity.MediumDark else TintScheme.Humidity.MediumLight
                humidityValue >= 20.0 -> if (isDarkMode) TintScheme.Humidity.LowDark else TintScheme.Humidity.LowLight
                else -> if (isDarkMode) TintScheme.Humidity.VeryLowDark else TintScheme.Humidity.VeryLowLight
            }
        }

        // Rain Intensity levels
        fun getRainIntensityTint(
            rainIntensityValue: Double,
            isDarkMode: Boolean,
        ): TintScheme.RainIntensity {
            return when {
                rainIntensityValue >= 30.0 -> if (isDarkMode) TintScheme.RainIntensity.VeryHighDark else TintScheme.RainIntensity.VeryHighLight
                rainIntensityValue >= 20.0 -> if (isDarkMode) TintScheme.RainIntensity.HighDark else TintScheme.RainIntensity.HighLight
                rainIntensityValue >= 10.0 -> if (isDarkMode) TintScheme.RainIntensity.MediumDark else TintScheme.RainIntensity.MediumLight
                rainIntensityValue >= 3.0 -> if (isDarkMode) TintScheme.RainIntensity.LowDark else TintScheme.RainIntensity.LowLight
                else -> if (isDarkMode) TintScheme.RainIntensity.VeryLowDark else TintScheme.RainIntensity.VeryLowLight
            }
        }

        // Total Rain levels
        fun getTotalRainTint(totalRainValue: Double, isDarkMode: Boolean): TintScheme.TotalRain {
            return when {
                totalRainValue >= 160.0 -> if (isDarkMode) TintScheme.TotalRain.VeryHighDark else TintScheme.TotalRain.VeryHighLight
                totalRainValue >= 80.0 -> if (isDarkMode) TintScheme.TotalRain.HighDark else TintScheme.TotalRain.HighLight
                totalRainValue >= 40.0 -> if (isDarkMode) TintScheme.TotalRain.MediumDark else TintScheme.TotalRain.MediumLight
                totalRainValue >= 8.0 -> if (isDarkMode) TintScheme.TotalRain.LowDark else TintScheme.TotalRain.LowLight
                else -> if (isDarkMode) TintScheme.TotalRain.VeryLowDark else TintScheme.TotalRain.VeryLowLight
            }
        }
    }
}
