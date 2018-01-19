package com.lasley.demo2.service

import com.lasley.demo2.model.WeatherJSON
import com.lasley.demo2.util.Constants
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface OWMCalls {
    companion object {
        fun create(): OWMCalls {
            val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(Constants.BASE_URL)
                    .client(createClient())
                    .build()

            return retrofit.create(OWMCalls::class.java)
        }

        private fun createClient(): OkHttpClient {
            val httpClient = OkHttpClient.Builder()

            httpClient.addInterceptor { chain ->
                val original = chain.request()
                val url = original.url().newBuilder()
                        .addQueryParameter("units", "imperial")
                        .addQueryParameter("appid", Constants.API_ID)
                        .build()

                val request = original.newBuilder()
                        .tag(Request.Builder().url(url))
                        .url(url).build()

                chain.proceed(request)
            }

            return httpClient.build()
        }
    }

    @GET(".")
    fun getWeather_ByCity(@Query("q") CityName: String): Call<WeatherJSON>

    @GET(".")
    fun getWeather_ByLatLon(@Query("lat") Lat: Double, @Query("lon") Lon: Double): Call<WeatherJSON>

    @GET(".")
    fun getWeather_ByZip(@Query("zip") Zipcode: Int): Call<WeatherJSON>
}
