package com.example.weather.weather.presentation

import androidx.compose.runtime.Immutable

@Immutable
data class WeatherHomeState(
    val isLoading: Boolean = false
)