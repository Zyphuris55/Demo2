package com.lasley.demo2.activity

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Bundle
import com.lasley.demo2.R
import com.lasley.demo2.contracts.SearchContract
import com.lasley.demo2.fragment.SearchLocation
import com.lasley.demo2.util.Constants
import com.lasley.demo2.util.Constants.BUNDLE_DATA

class SplashScreen : BaseActivity(), SearchContract {
    override val layout: Int = R.layout.activity_splash_screen
    internal var searchFragment: SearchLocation = SearchLocation()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragment(searchFragment)
        lastLoc = sharedPref?.getStringSet(Constants.KEY_LASTLOCATION, null)
        if (lastLoc != null) {
            postRetrieveLocation()
        }
    }

    override fun postRetrieveLocation() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(BUNDLE_DATA, searchFragment.getLocationString())
        startActivity(intent)
        finish()
    }
}