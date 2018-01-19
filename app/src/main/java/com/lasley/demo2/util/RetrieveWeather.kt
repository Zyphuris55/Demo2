package com.lasley.demo2.util

import com.lasley.demo2.contracts.WeatherResultsContract
import com.lasley.demo2.model.WeatherJSON
import com.lasley.demo2.service.NetworkCaller
import com.lasley.demo2.service.OWMCalls
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


class RetrieveWeather(val resultsView: WeatherResultsContract) {
    private val networkCaller: NetworkCaller = NetworkCaller()

    fun collectWeather(location: Set<String>?) {
        try {
            if (location == null || location.isEmpty()) return
            val loc_array = location.toTypedArray()
            collectWeather(loc_array[1].toDouble(), loc_array[0].toDouble())
        } catch (ex: Exception) {
            Timber.e(ex.message)
        }
    }

    fun collectWeather(location: String) {
        networkCaller.addCall(OWMCalls.create().getWeather_ByCity(location))
                .MakeCall(object : Callback<WeatherJSON> {
                    override fun onResponse(call: Call<WeatherJSON>, response: Response<WeatherJSON>) {
                        Timber.d("Response:" + response.message())
                        resultsView.weatherResults(response.body())
                    }

                    override fun onFailure(call: Call<WeatherJSON>, t: Throwable) {
                        Timber.d("Failure:" + t.message)
                        resultsView.resultsError()
                    }
                })
    }

    fun collectWeather(lat: Double, lon: Double) {
        networkCaller.addCall(OWMCalls.create().getWeather_ByLatLon(lat, lon))
                .MakeCall(object : Callback<WeatherJSON> {
                    override fun onResponse(call: Call<WeatherJSON>, response: Response<WeatherJSON>) {
                        Timber.d("Response:" + response.message())
                        resultsView.weatherResults(response.body())
                    }

                    override fun onFailure(call: Call<WeatherJSON>, t: Throwable) {
                        Timber.d("Failure:" + t.message)
                        resultsView.resultsError()
                    }
                })
    }
}
