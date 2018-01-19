package com.lasley.demo2.model

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class WeatherJSON(
        @SerializedName("coord") val coord: Coord? = null,
        @SerializedName("weather") val weather: List<Weather> = arrayListOf(),
        @SerializedName("base") val base: String? = null, //stations
        @SerializedName("main") val main: Main? = null,
        @SerializedName("visibility") val visibility: Int? = null,
        @SerializedName("wind") val wind: Wind? = null,
        @SerializedName("clouds") val clouds: Clouds? = null,
        @SerializedName("dt") val dt: Long = 1L, //Unix Time
        @SerializedName("sys") val sys: Sys? = null,
        @SerializedName("id") val id: Int? = null,
        @SerializedName("name") val name: String? = null, //City
        @SerializedName("cod") val cod: Int? = null, //response type
        @SerializedName("message") val message: String? = null
) {
    fun getCoordsAsString(): String = String.format("(%1\$.4f, %2\$.4f)", coord?.lat, coord?.lon)

    fun getLastUpdate(): String {
        val date = Date(dt * 1000L)
        val sdf = SimpleDateFormat("hh:mm:ss z, MM/dd/yyyy") // the format of your date
        sdf.setTimeZone(TimeZone.getDefault())
        return sdf.format(date)
    }
}

data class Coord(
        @SerializedName("lon") val lon: Double,
        @SerializedName("lat") val lat: Double
)

data class Wind(
        @SerializedName("speed") val speed: Double, //4.1
        @SerializedName("deg") val deg: Double //80
)

data class Main(
        @SerializedName("temp") val temp: Double,
        @SerializedName("pressure") val pressure: Double,
        @SerializedName("humidity") val humidity: Int,
        @SerializedName("temp_min") val tempMin: Double,
        @SerializedName("temp_max") val tempMax: Double
)

data class Weather(
        @SerializedName("id") val id: Int, //300
        @SerializedName("main") val main: String, //Drizzle
        @SerializedName("description") val description: String, //light intensity drizzle
        @SerializedName("icon") val icon: String //09d
)

data class Sys(
        @SerializedName("type") val type: Int,
        @SerializedName("id") val id: Int,
        @SerializedName("message") val message: Double,
        @SerializedName("country") val country: String,
        @SerializedName("sunrise") val sunrise: Long, //UNIX time
        @SerializedName("sunset") val sunset: Long
)

data class Clouds(
        @SerializedName("all") val all: Int // percent
)