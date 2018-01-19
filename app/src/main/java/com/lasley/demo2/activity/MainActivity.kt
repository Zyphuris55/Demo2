package com.lasley.demo2.activity

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import com.lasley.demo2.R
import com.lasley.demo2.contracts.WeatherResultsContract
import com.lasley.demo2.fragment.SearchLocation
import com.lasley.demo2.model.WeatherJSON
import com.lasley.demo2.util.Constants
import com.lasley.demo2.util.Constants.BUNDLE_DATA
import com.lasley.demo2.util.RetrieveWeather


class MainActivity : BaseActivity(), WeatherResultsContract {

    @BindView(R.id.SwipeRefreshView)
    lateinit var refreshView: SwipeRefreshLayout
    @BindView(R.id.text_city_country)
    lateinit var mCityCountry: TextView
    @BindView(R.id.text_coords)
    lateinit var mCoords: TextView
    @BindView(R.id.img_weatherType)
    lateinit var mWeatherImg: ImageView
    @BindView(R.id.text_description)
    lateinit var mWeatherDes: TextView
    @BindView(R.id.temp_min)
    lateinit var mTempMin: TextView
    @BindView(R.id.temp_max)
    lateinit var mTempMax: TextView
    @BindView(R.id.temp_avg)
    lateinit var mTempAvg: TextView
    @BindView(R.id.lastUpdate)
    lateinit var mLastUpdate: TextView

    private var getWeather: RetrieveWeather? = null

    override val layout: Int = R.layout.results_layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        refreshView.setOnRefreshListener { collectWeather() }

        val intent = intent
        val searchStr = intent.getStringExtra(BUNDLE_DATA)
        lastLoc = sharedPref?.getStringSet(Constants.KEY_LASTLOCATION, null)

        // try using the splashScreen's search term
        val locationStr = if (searchStr != null && !searchStr.isEmpty())
            searchStr
        else if (lastLoc == null)
            ""
        else
        // or try using the previous location
            lastLoc!!.toString().replace("\\[|\\]".toRegex(), "")//.replaceAll(","," ");

        if (locationStr.isEmpty()) {
            // todo: return to splash screen?
        }

        getWeather = RetrieveWeather(this)
        if (lastLoc == null)
            getWeather!!.collectWeather(locationStr)
        else
            collectWeather()

        setFragment(SearchLocation(locationStr))
    }

    override fun postRetrieveLocation() {
        lastLoc = sharedPref?.getStringSet(Constants.KEY_LASTLOCATION, null)
        if (lastLoc == null) {
            resultsError()
        } else
            collectWeather()
    }

    private fun collectWeather() {
        refreshView.isRefreshing = false
        getWeather!!.collectWeather(lastLoc)
    }

    override fun weatherResults(results: WeatherJSON?) {
        if (results == null) {
            resultsError()
        } else {
            mCityCountry.text = results.name ?: results.message
            mCoords.text = results.getCoordsAsString()

            val weather = if (results.weather.isEmpty()) null else results.weather[0]
            val resID = resources.getIdentifier("w_" + weather?.icon, "drawable", packageName)
            mWeatherImg.setImageResource(resID)
            mWeatherDes.text = resources.getString(R.string.current_weather, weather?.description ?: "-")

            mTempMin.text = resources.getString(R.string.temperature_str, "Min", results.main?.tempMin)
            mTempMax.text = resources.getString(R.string.temperature_str, "Max", results.main?.tempMax)
            mTempAvg.text = resources.getString(R.string.temperature_str, "Avg", results.main?.temp)
            mLastUpdate.text = resources.getString(R.string.last_update_str, results.getLastUpdate())
        }
    }

    override fun resultsError() {
        mCityCountry.text = resources.getString(R.string.error_msg)
        mCoords.text = resources.getString(R.string.empty_coords)

        val resID = resources.getIdentifier("w_000", "drawable", packageName)
        mWeatherImg.setImageResource(resID)
        mWeatherDes.text = resources.getString(R.string.current_weather, "-")

        mTempMin.text = resources.getString(R.string.temperature_str, "Min", 0.0)
        mTempMax.text = resources.getString(R.string.temperature_str, "Max", 0.0)
        mTempAvg.text = resources.getString(R.string.temperature_str, "Avg", 0.0)
        mLastUpdate.text = resources.getString(R.string.last_update_str, "-")
    }
}