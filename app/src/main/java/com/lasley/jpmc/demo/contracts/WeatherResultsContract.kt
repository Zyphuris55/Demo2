package com.lasley.jpmc.demo.contracts

import com.lasley.jpmc.demo.model.WeatherJSON

interface WeatherResultsContract {
    fun weatherResults(results: WeatherJSON?)
    fun resultsError()
}