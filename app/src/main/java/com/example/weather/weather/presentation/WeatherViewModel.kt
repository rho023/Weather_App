package com.example.weather.weather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.core.domain.util.Result
import com.example.weather.isNetwork
import com.example.weather.weather.domain.WeatherDataSource
import com.example.weather.weather.presentation.models.WeatherUi
import com.example.weather.weather.presentation.models.toWeatherUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val weatherDataSource: WeatherDataSource,
    private val weatherPre: WeatherPreferences
): ViewModel() {
    private val _state = MutableStateFlow(WeatherHomeState())
    private val _weatherUi = MutableStateFlow<WeatherUi?>(null)
    private val _cityWeatherUi = MutableStateFlow<Map<String, WeatherUi>>(emptyMap())

    val state: StateFlow<WeatherHomeState> get() = _state
    val cityWeatherUi: StateFlow<Map<String, WeatherUi>> get() = _cityWeatherUi
    val weatherUi: StateFlow<WeatherUi?> get() = _weatherUi

    init {
        //Load saved data on instantiation
        viewModelScope.launch {
            weatherPre.weatherFlow.collect { weatherUi ->
                _weatherUi.value = weatherUi
            }
        }
    }

    fun fetchWeather(lat: Double, lon: Double) {
        if(isNetwork) {
            viewModelScope.launch {
                when(val result = weatherDataSource.getWeather(lat, lon)) {
                    is Result.Error -> {
                        _weatherUi.value= null
                        _state.value = state.value.copy(isLoading = false)
                    }
                    is Result.Success -> {
                        val weatherUi = result.data.toWeatherUi()
                        _weatherUi.value = weatherUi

                        weatherPre.saveWeather(weatherUi) //Save fetched data
                        _state.value = state.value.copy(isLoading = false)
                    }
                }
            }
        }
    }
    fun fetchWeatherForCities() {
        val cities = mapOf(
            "New York" to Pair(40.7128, -74.0060),
            "Delhi" to Pair(28.7041, 77.1025),
            "Singapore" to Pair(1.3521, 103.8198)
        )

        if (isNetwork) {
            viewModelScope.launch {
                _state.value = state.value.copy(isLoading = true)
                val weatherResults = mutableMapOf<String, WeatherUi>()
                for ((cityName, coordinates) in cities) {
                    val (lat, lon) = coordinates
                    when (val result = weatherDataSource.getWeather(lat, lon)) {
                        is Result.Success -> {
                            weatherResults[cityName] = result.data.toWeatherUi()
                        }

                        is Result.Error -> {
                            // Adding a placeholder
                            weatherResults[cityName] = WeatherUi(
                                name = cityName,
                                date = "N/A",
                                description = "Unavailable",
                                temp = 0,
                                humidity = 0,
                                feelTemp = 0,
                                tempMin = 0,
                                tempMax = 0,
                                wind = 0,
                                visibility = 0,
                                mainCondition = "Unavailable"
                            )
                        }
                    }
                }
                _cityWeatherUi.value = weatherResults
                _state.value = state.value.copy(isLoading = false)
            }
        }
    }
}