package bd.com.albin.weather.location

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import bd.com.albin.weather.data.model.CurrentWeather
import bd.com.albin.weather.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject


sealed interface LocationUiState {
    data class Success(val data: CurrentWeather) : LocationUiState
    data class Error(val message: String) : LocationUiState
    data object Loading : LocationUiState
}

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
) : ViewModel() {
    var weatherUiState: LocationUiState by mutableStateOf(LocationUiState.Loading)
        private set

    suspend fun getCurrentWeather(lat: Double, lon: Double) {

        weatherUiState = LocationUiState.Loading
        weatherUiState = try {
            LocationUiState.Success(weatherRepository.getWeather(lat = lat, lon = lon))
        } catch (e: IOException) {
            LocationUiState.Error(e.localizedMessage ?: "IOException")
        } catch (e: HttpException) {
            LocationUiState.Error(e.localizedMessage ?: "HttpException")
        } catch (e: Exception) {
            LocationUiState.Error(e.localizedMessage ?: "HttpException")
        }

    }
}
