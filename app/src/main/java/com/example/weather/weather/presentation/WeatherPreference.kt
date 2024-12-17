package com.example.weather.weather.presentation

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import com.example.weather.weather.presentation.models.WeatherUi
import com.google.gson.Gson

private val Context.dataStore by preferencesDataStore(name = "weather_prefs")

class WeatherPreferences(context: Context) {
    private val WEATHER_KEY = stringPreferencesKey("weather_data")
    private val gson = Gson()

    // Use the extension property to access the DataStore
    private val dataStore = context.dataStore

    // Save Weather Data
    suspend fun saveWeather(weatherUI: WeatherUi) {
        val json = gson.toJson(weatherUI)
        dataStore.edit { preferences ->
            preferences[WEATHER_KEY] = json
        }
    }

    // Load Weather Data
    val weatherFlow = dataStore.data.map { preferences ->
        preferences[WEATHER_KEY]?.let { json ->
            gson.fromJson(json, WeatherUi::class.java)
        }
    }
}