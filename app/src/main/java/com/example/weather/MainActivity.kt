package com.example.weather

import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.example.weather.weather.presentation.WeatherScreen
import com.example.weather.weather.presentation.WeatherViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

var isNetwork = true
class MainActivity : ComponentActivity() {

    private val viewModel: WeatherViewModel by viewModel() //Inject the viewmodel using Koin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val context = LocalContext.current
            val locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager

            // State to hold the coordinates
            var coordinates by remember { mutableStateOf<Pair<Double, Double>?>(null) }

            // Permission launcher
            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                if (isGranted) {
                    // Get the location
                    val location: Location? =
                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    location?.let {
                        coordinates = Pair(it.latitude, it.longitude)

                        // Fetch weather after getting location
                        viewModel.fetchWeather(it.latitude, it.longitude)
                    }
                }
            }

            // Ask for permission when the screen starts
            LaunchedEffect(Unit) {
                val permission = android.Manifest.permission.ACCESS_FINE_LOCATION
                if (ContextCompat.checkSelfPermission(context, permission) != PermissionChecker.PERMISSION_GRANTED) {
                    permissionLauncher.launch(permission)
                } else {
                    // If already granted, fetch the location
                    val location: Location? =
                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    location?.let {
                        coordinates = Pair(it.latitude, it.longitude)
                        viewModel.fetchWeather(it.latitude, it.longitude)
                    }
                }
            }

            // Observe ViewModel data
            val weatherUiState by viewModel.weatherUi.collectAsState()
            val state by viewModel.state.collectAsState()

            // Display UI
            WeatherScreen(
                state = state,
                weatherUi = weatherUiState ?: return@setContent,
                modifier = Modifier
            )
        }
    }
}