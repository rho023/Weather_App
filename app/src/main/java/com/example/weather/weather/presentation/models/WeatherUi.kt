package com.example.weather.weather.presentation.models

import com.example.weather.weather.domain.WeatherResponse
import java.time.format.DateTimeFormatter
import java.util.Locale

data class WeatherUi(
    val description: String,
    val mainCondition: String,
    val temp: Int,
    val humidity: Int,
    val name: String,
    val date: String,
    val tempMin: Int,
    val tempMax: Int,
    val feelTemp: Int,
    val wind: Int,
    val visibility: Int,
)

fun WeatherResponse.toWeatherUi(): WeatherUi {
    val formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM", Locale.getDefault())
    val formattedDate = dt.format(formatter) // Directly format ZonedDateTime
    return WeatherUi(
        name = name,
        description = weather.first().description,
        mainCondition = weather.first().main,
        temp = main.temp - 273,
        humidity = main.humidity,
        date = formattedDate,
        tempMin = main.temp_min - 273,
        tempMax = main.temp_max - 273,
        feelTemp = main.feels_like - 273,
        wind = wind.speed,
        visibility = visibility
    )
}
