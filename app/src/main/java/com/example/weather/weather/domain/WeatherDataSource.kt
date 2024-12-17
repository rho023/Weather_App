package com.example.weather.weather.domain

import com.example.weather.core.domain.util.NetworkError
import com.example.weather.core.domain.util.Result

interface WeatherDataSource {
    suspend fun getWeather(lat: Double, long: Double): Result<WeatherResponse, NetworkError>
}