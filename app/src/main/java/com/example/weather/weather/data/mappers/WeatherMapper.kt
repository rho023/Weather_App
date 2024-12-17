package com.example.weather.weather.data.mappers

import com.example.weather.weather.data.networking.dto.WeatherDto
import com.example.weather.weather.domain.WeatherResponse
import java.time.Instant
import java.time.ZoneId

fun WeatherDto.toWeatherResponse(): WeatherResponse {
    return WeatherResponse(
        weather = weather.map { it.toWeather() }, // Map each WeatherDto.Weather to Weather
        main = main.toMain(), // Map MainDto to Main
        name = name, // Directly map the name
        dt = Instant
            .ofEpochMilli(dt)
            .atZone(ZoneId.systemDefault()),
        visibility = visibility,
        wind = wind.toWind() // Map WindDto to Wind
    )
}

fun WeatherDto.Weather.toWeather(): WeatherResponse.Weather {
    return WeatherResponse.Weather(
        description = description,
        icon = icon,
        main = main
    )
}

fun WeatherDto.Main.toMain(): WeatherResponse.Main {
    return WeatherResponse.Main(
        temp = temp.toInt(), // Convert Double to Int
        humidity = humidity,
        feels_like = feels_like.toInt(), // Convert Double to Int
        temp_min = temp_min.toInt(), // Convert Double to Int
        temp_max = temp_max.toInt() // Convert Double to Int
    )
}

fun WeatherDto.Wind.toWind(): WeatherResponse.Wind {
    return WeatherResponse.Wind(
        speed = speed.toInt() // Convert Double to Int
    )
}