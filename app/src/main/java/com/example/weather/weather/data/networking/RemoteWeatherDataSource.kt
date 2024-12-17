package com.example.weather.weather.data.networking

import com.example.weather.core.data.networking.constructUrl
import com.example.weather.core.data.networking.safeCall
import com.example.weather.core.domain.util.NetworkError
import com.example.weather.core.domain.util.Result
import com.example.weather.core.domain.util.map
import com.example.weather.weather.data.mappers.toWeatherResponse
import com.example.weather.weather.data.networking.dto.WeatherDto
import com.example.weather.weather.domain.WeatherDataSource
import com.example.weather.weather.domain.WeatherResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class RemoteWeatherDataSource(
    private val httpClient: HttpClient
): WeatherDataSource {

    private val apiKey = ""

    override suspend fun getWeather(
        lat: Double,
        long: Double
    ): Result<WeatherResponse, NetworkError> {
        return safeCall<WeatherDto> {
            httpClient.get(
                urlString = constructUrl("lat=${lat}&lon=${long}&appid=${apiKey}")
            )
        }.map { response ->
            response.toWeatherResponse()
        }
    }
}
