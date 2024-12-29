package bd.com.albin.weather.di

import bd.com.albin.weather.data.network.WeatherApiService
import bd.com.albin.weather.data.repository.WeatherRepository
import bd.com.albin.weather.data.repository.WeatherRepositoryImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl("https://api.openweathermap.org/data/2.5/").build()

    @Provides
    @Singleton
    fun providesWeatherApiService(retrofit: Retrofit): WeatherApiService =
        retrofit.create(WeatherApiService::class.java)


    @Provides
    @Singleton
    fun providesWeatherRepository(
        weatherApiService: WeatherApiService,
    ): WeatherRepository =
        WeatherRepositoryImpl(
            weatherApiService,
        )
}