package bd.com.albin.weather.data.network

import bd.com.albin.weather.BuildConfig
import bd.com.albin.weather.data.model.CurrentWeather
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("weather")
    suspend fun getWeather(
        @Query("appid") apiKey: String = BuildConfig.API_KEY,
        @Query("lat") lat: Double? = 23.7104,
        @Query("lon") lon: Double? = 90.4074,
    ): CurrentWeather

}