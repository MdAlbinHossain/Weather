package bd.com.albin.weather.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class CurrentWeather(
    val coord: Coord,
    val weather: List<Weather>,
    val base: String? = null,
    val main: Main,
    val visibility: Long? = null,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Long? = 1013,
    val sys: Sys,
    val timezone: Long? = null,
    val id: Long? = null,
    val name: String? = null,
    val cod: Long? = null,
)

@Serializable
data class Coord(
    val lon: Double? = null,
    val lat: Double? = null,
)

@Serializable
data class Weather(
    val id: Long? = null,
    val main: String? = null,
    val description: String? = null,
    val icon: String? = null,
)

@Serializable
data class Main(
    val temp: Double? = null,
    @SerialName("feels_like")
    val feelsLike: Double? = null,
    @SerialName("temp_min")
    val tempMin: Double? = null,
    @SerialName("temp_max")
    val tempMax: Double? = null,
    val pressure: Long? = null,
    val humidity: Long? = null,
    @SerialName("sea_level")
    val seaLevel: Long? = null,
    @SerialName("grnd_level")
    val grndLevel: Long? = null,
)

@Serializable
data class Wind(
    val speed: Double? = null,
    val deg: Long? = null,
    val gust: Double? = null,
)

@Serializable
data class Clouds(
    val all: Long? = null,
)

@Serializable
data class Sys(
    val type: Long? = null,
    val id: Long? = null,
    val country: String? = null,
    val sunrise: Long? = null,
    val sunset: Long? = null,
)