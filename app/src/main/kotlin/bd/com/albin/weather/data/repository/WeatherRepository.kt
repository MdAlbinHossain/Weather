package bd.com.albin.weather.data.repository

import bd.com.albin.weather.data.model.CurrentWeather


interface WeatherRepository {
    suspend fun getWeather(lat: Double, lon: Double): CurrentWeather
}