package com.example.weather.weather.di

import com.example.weather.core.data.networking.HttpClientFactory
import com.example.weather.weather.data.networking.RemoteWeatherDataSource
import com.example.weather.weather.domain.WeatherDataSource
import com.example.weather.weather.presentation.WeatherPreferences
import com.example.weather.weather.presentation.WeatherViewModel
import io.ktor.client.engine.cio.CIO
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Provide HttpClient
    single { HttpClientFactory.create(CIO.create()) }

    // Provide WeatherDataSource
    single { RemoteWeatherDataSource( get()) } // Pass HttpClient
    single<WeatherDataSource> { get<RemoteWeatherDataSource>() } // Bind interface

    // Provide WeatherPreferences
    single { WeatherPreferences(get()) } // Assume Context is provided by Koin

    // Provide WeatherViewModel
    viewModel { WeatherViewModel(get(), get()) } // Pass WeatherDataSource and WeatherPreferences
}