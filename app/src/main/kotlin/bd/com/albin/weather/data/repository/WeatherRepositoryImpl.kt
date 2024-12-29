package bd.com.albin.weather.data.repository

import bd.com.albin.weather.data.model.CurrentWeather
import bd.com.albin.weather.data.network.WeatherApiService
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApiService: WeatherApiService,
) : WeatherRepository {


    override suspend fun getWeather(lat: Double, lon: Double): CurrentWeather {
        return weatherApiService.getWeather(lat = lat, lon = lon)
    }
}