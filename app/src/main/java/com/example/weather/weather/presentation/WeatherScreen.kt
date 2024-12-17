package com.example.weather.weather.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather.R
import com.example.weather.weather.presentation.models.WeatherUi
import org.koin.androidx.compose.koinViewModel

@Composable
fun WeatherScreen(
    state: WeatherHomeState,
    weatherUi: WeatherUi,
    viewModel: WeatherViewModel = koinViewModel(),
    modifier: Modifier
) {
    val cityWeatherUi by viewModel.cityWeatherUi.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchWeatherForCities()
    }

    if(state.isLoading) {
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }else {
        Scaffold(
            modifier = modifier
                .fillMaxSize(),
            containerColor = colorResource(R.color.sky_blue)
        ) { innerPadding ->
            LazyColumn(
                modifier = modifier
                    .padding(innerPadding)
                    .padding(6.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    WeatherHeader(cityName = weatherUi.name, date = weatherUi.date)
                    Spacer(modifier = Modifier.height(10.dp))
                }
                item {
                    TemperatureDisplay(temperature = weatherUi.temp, condition = weatherUi.mainCondition)
                    Spacer(modifier = Modifier.height(10.dp))
                }
                item {
                    DailySummary(summary = "Now it feels like ${weatherUi.feelTemp}°, actually ${weatherUi.temp}°.\nToday, the temperature is felt in the range from ${weatherUi.tempMin}° to ${weatherUi.tempMax}°.")
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    WeatherConditions(wind = weatherUi.wind, humidity = weatherUi.humidity, visibility = weatherUi.visibility)
                    Spacer(modifier = Modifier.height(32.dp))
                }
                item {
                    WeeklyForecast(cityWeatherUi)
                }
            }

        }
    }
}

@Composable
fun WeatherHeader(cityName: String, date: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = cityName,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Color.Black
        )
        Spacer(Modifier.height(8.dp))
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF000000))
        ) {
            Text(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                text = date,
                fontSize = 13.sp,
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(R.color.sky_blue)
            )
        }
    }
}

@Composable
fun TemperatureDisplay(temperature: Int, condition: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = condition,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            fontSize = 20.sp,
            color = Color.Black
        )
        Text(
            text = "$temperature°",
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            fontSize = 150.sp,
            color = Color.Black
        )
    }
}

@Composable
fun DailySummary(summary: String) {
    Text(
        text = summary,
        style = MaterialTheme.typography.bodyMedium,
        color = Color.Black,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@Composable
fun WeatherConditions(wind: Int, humidity: Int, visibility: Int) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF000000)),
        modifier = Modifier.padding(horizontal = 30.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            WeatherConditionItem(value = wind.toString(), label = "Wind")
            WeatherConditionItem(value = humidity.toString(), label = "Humidity")
            WeatherConditionItem(value = visibility.toString(), label = "Visibility")
        }
    }
}

@Composable
fun WeatherConditionItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (label) {
                    "Wind" -> {
                        Image(
                            painter = painterResource(R.drawable.wind),
                            contentDescription = "Wind",
                            modifier = Modifier.size(40.dp),
                            colorFilter = ColorFilter.tint(colorResource(R.color.sky_blue))
                        )
                    }
                    "Humidity" -> {
                        Image(
                            painter = painterResource(R.drawable.humidity),
                            contentDescription = "Humidity",
                            modifier = Modifier.size(40.dp),
                            colorFilter = ColorFilter.tint(colorResource(R.color.sky_blue))
                        )
                    }
                    else -> {
                        Image(
                            painter = painterResource(R.drawable.visibility),
                            contentDescription = "Visibility",
                            modifier = Modifier.size(40.dp),
                            colorFilter = ColorFilter.tint(colorResource(R.color.sky_blue))
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    when (label) {
                        "Wind" -> {
                            "$value km/h"
                        }
                        "Humidity" -> {
                            "$value%"
                        }
                        else -> {
                            "$value m"
                        }
                    },
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = colorResource(R.color.sky_blue)
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = colorResource(R.color.sky_blue)
                )
            }
        }
    }
}

@Composable
fun WeeklyForecast(
    cityWeatherUi: Map<String, WeatherUi>
    ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        cityWeatherUi.forEach { (cityName, weatherUi) ->
            ForecastDayItem(city = cityName, temp = "${weatherUi.temp}°")
        }
    }
}

@Composable
fun ForecastDayItem(city: String, temp: String) {
    Card(
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = Modifier
            .size(width = 80.dp, height = 100.dp)
            .border(
                width = 2.dp,
                color = Color.Black,
                shape = RoundedCornerShape(14.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = temp,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                fontSize = 22.sp,
                color = Color.Black
            )
            Text(
                text = city,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                fontSize = 13.sp,
                color = Color.Black
            )
        }
    }
}

@Preview
@Composable
fun WeatherScreenPreview(
    modifier: Modifier = Modifier
) {
    val mockWeatherUi = WeatherUi(
        name = "Pune",
        date = "Friday, 20 January",
        description = "Very sunny and cloudy also",
        temp = 31,
        mainCondition = "Sunny",
        feelTemp = 35,
        tempMin = 27,
        tempMax = 31,
        wind = 4,
        humidity = 48,
        visibility = 1600
    )
    val mockState = WeatherHomeState(
        isLoading = false
    )
    WeatherScreen(
        state = mockState,
        weatherUi = mockWeatherUi,
        modifier = Modifier
    )
}