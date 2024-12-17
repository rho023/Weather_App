package com.example.weather.weather.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class WeatherDto(
    val weather: List<Weather>,
    val main: Main,
    val name: String,
    val dt: Long,
    val visibility: Int,
    val wind: Wind
) {
    @Serializable
    data class Weather(
        val description: String,
        val icon: String,
        val main: String
    )

    @Serializable
    data class Main(
        val temp: Double,
        var humidity: Int,
        val feels_like: Double,
        val temp_min: Double,
        val temp_max: Double
    )

    @Serializable
    data class Wind(
        val speed: Double
    )
}