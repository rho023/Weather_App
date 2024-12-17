package com.example.weather.weather.domain

import java.time.ZonedDateTime

data class WeatherResponse(
    val weather: List<Weather>,
    val main: Main,
    val name: String,
    val dt: ZonedDateTime,
    val visibility: Int,
    val wind: Wind
) {
    data class Weather(
        val description: String,
        val icon: String,
        val main: String
    )

    data class Main(
        val temp: Int,
        val humidity: Int,
        val feels_like: Int,
        val temp_min: Int,
        val temp_max: Int
    )

    data class Wind(
            val speed: Int
    )
}