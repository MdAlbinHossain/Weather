package bd.com.albin.weather.location

import android.Manifest
import android.annotation.SuppressLint
import androidx.annotation.RequiresPermission
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.EditLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WindPower
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bd.com.albin.weather.R
import bd.com.albin.weather.data.model.Clouds
import bd.com.albin.weather.data.model.Coord
import bd.com.albin.weather.data.model.CurrentWeather
import bd.com.albin.weather.data.model.Main
import bd.com.albin.weather.data.model.Sys
import bd.com.albin.weather.data.model.Weather
import bd.com.albin.weather.data.model.Wind
import bd.com.albin.weather.ui.theme.WeatherTheme
import coil.compose.AsyncImage
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.math.max

@SuppressLint("MissingPermission")
@Composable
fun CurrentWeatherScreen() {
    val permissions = listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )
    PermissionBox(
        permissions = permissions,
        requiredPermissions = listOf(permissions.first()),
        onGranted = {
            CurrentWeatherContent()
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresPermission(
    anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION],
)
@Composable
fun CurrentWeatherContent() {
    val scope = rememberCoroutineScope()
    val locationViewModel = hiltViewModel<LocationViewModel>()


    val context = LocalContext.current
    val locationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoading = true

        val result = locationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token,
        ).await()


        result?.let { fetchedLocation ->
            locationViewModel.getCurrentWeather(
                lat = fetchedLocation.latitude, lon = fetchedLocation.longitude
            )
        }

        isLoading = false
    }

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        CenterAlignedTopAppBar(title = { Text(stringResource(R.string.weather)) },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(Icons.Filled.Search, null)
                }
            },
            actions = {
                IconButton(
                    onClick = {
                        scope.launch(Dispatchers.IO) {

                            isLoading = true

                            val result = locationClient.getCurrentLocation(
                                Priority.PRIORITY_HIGH_ACCURACY,
                                CancellationTokenSource().token,
                            ).await()


                            result?.let { fetchedLocation ->
                                locationViewModel.getCurrentWeather(
                                    lat = fetchedLocation.latitude, lon = fetchedLocation.longitude
                                )
                            }

                            isLoading = false

                        }
                    },
                ) {
                    Icon(Icons.Filled.EditLocation, null)
                }
            })
    }) { innerPadding ->
        when (val uiState = locationViewModel.weatherUiState) {
            LocationUiState.Loading -> {
                LoadingScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            is LocationUiState.Error -> {
                ErrorScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding), uiState.message
                )
            }

            is LocationUiState.Success -> {
                WeatherScreen(
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding), uiState.data
                )
            }
        }
    }

}

@Composable
private fun ErrorScreen(
    modifier: Modifier, message: String
) {
    Column(
        modifier
            .animateContentSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(message)
    }
}

@Composable
private fun LoadingScreen(modifier: Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun WeatherScreen(
    modifier: Modifier, uiState: CurrentWeather
) {
    Column(
        modifier
            .animateContentSize()
            .padding(16.dp),
    ) {
        Spacer(Modifier.size(16.dp))
        uiState.name?.let {
            Text(it, style = MaterialTheme.typography.displaySmall, textAlign = TextAlign.Justify)
        }
        uiState.dt?.let {
            val localDateTime = Instant.fromEpochMilliseconds(it)
                .toLocalDateTime(TimeZone.currentSystemDefault()).date.format(format = LocalDate.Format {
                    dayOfWeek(names = DayOfWeekNames.ENGLISH_ABBREVIATED)
                    char(',')
                    char(' ')
                    monthName(names = MonthNames.ENGLISH_ABBREVIATED)
                    char(' ')
                    dayOfMonth(padding = Padding.NONE)
                })
            Text(localDateTime)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalArrangement = Arrangement.spacedBy(
                space = 32.dp,
                alignment = Alignment.Start
            ),
            verticalAlignment = Alignment.CenterVertically,

            ) {
            AsyncImage(
                modifier = Modifier.size(120.dp),
                model = "https://openweathermap.org/img/wn/${uiState.weather.first().icon}@2x.png",
                placeholder = BrushPainter(
                    Brush.linearGradient(
                        colors = listOf(
                            Color.White,
                            Color.Red,
                        )
                    )
                ),
                contentScale = ContentScale.FillBounds,
                contentDescription = null
            )
            uiState.main.temp?.let {
                Column {
                    Row {
                        Text(
                            (max(it - 272.15, 0.0)).toInt().toString(),
                            style = MaterialTheme.typography.displayLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text("°C")
                    }
                    uiState.weather.first().main?.let {
                        Text(it)
                    }
                }
            }

        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                elevation = CardDefaults.elevatedCardElevation(4.dp)
            ) {
                ListItem(leadingContent = {
                    Icon(Icons.Filled.WaterDrop, null)
                }, headlineContent = { Text("Humidity") }, trailingContent = {
                    Text(uiState.main.humidity.toString() + "%")
                })
            }
            ElevatedCard(elevation = CardDefaults.elevatedCardElevation(4.dp)) {
                ListItem(leadingContent = {
                    Icon(Icons.Filled.WindPower, null)
                }, headlineContent = { Text("Wind") }, trailingContent = {
                    Text(uiState.wind.speed.toString() + " kmp/h")
                })
            }
            ElevatedCard(elevation = CardDefaults.elevatedCardElevation(4.dp)) {
                ListItem(leadingContent = {
                    Icon(Icons.Default.Air, null)
                }, headlineContent = { Text("Pressure") }, trailingContent = {
                    Text(uiState.main.pressure.toString())
                })
            }
        }
    }
}


@Preview(showSystemUi = true, showBackground = true, device = "id:pixel_9")
@Composable
fun WeatherScreenPreview() {
    WeatherTheme {
        WeatherScreen(
            modifier = Modifier.fillMaxSize(), CurrentWeather(
                coord = Coord(),
                listOf(Weather(main = "Rainy")),
                main = Main(temp = 19.0),
                wind = Wind(),
                clouds = Clouds(),
                sys = Sys(),
                name = LoremIpsum(words = 1).values.toList().first().toString()
            )
        )
    }
}