package com.lasley.jpmc.demo.service

import com.lasley.jpmc.demo.model.WeatherJSON
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.*


class NetworkCaller {
    private val mExecutorList = ArrayList<Executor>()

    fun addCall(call: Call<WeatherJSON>): Executor {
        val executor = Executor(call)
        mExecutorList.add(executor)
        return executor
    }

    private fun removeExecutorCall(executor: Executor) {
        executor.onDestroy()
        mExecutorList.remove(executor)
    }

    inner class Executor(var mCall: Call<WeatherJSON>) {
        fun MakeCall(callback: Callback<WeatherJSON>?) {
            if (callback != null) {
                mCall.clone().enqueue(object : Callback<WeatherJSON> {
                    override fun onResponse(call: Call<WeatherJSON>, response: Response<WeatherJSON>) {
                        Timber.d("Code = %d", response.code())
                        Timber.d("onResponse Call = %s", call.request())
                        if (response.errorBody() != null) {
                            Timber.d("onResponse Response Error = %s", response.errorBody().toString())
                        }
                        if (response.code() == 401) {
                            // show "no internet available" message
                            return
                        }
                        if (response.isSuccessful()) {
                            callback.onResponse(call, response)
                            removeExecutorCall(this@Executor)
                        } else {
                            onFailure(call, Throwable(
                                    String.format(Locale.US, "Response was not Successful %d",
                                            response.code())))
                        }
                    }

                    /*
                     * Callback should not be doing any UI work which may cause a crash
                     */
                    override fun onFailure(fCall: Call<WeatherJSON>, t: Throwable) {
                        Timber.d("onFailure Call = %s", fCall.request())
                        callback.onFailure(fCall, t)
                    }
                })
            }
        }

        fun onDestroy() {
            mCall.cancel()
        }
    }
}