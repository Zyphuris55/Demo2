package com.lasley.demo2.contracts

import com.lasley.demo2.model.WeatherJSON

interface WeatherResultsContract {
    fun weatherResults(results: WeatherJSON?)
    fun resultsError()
}