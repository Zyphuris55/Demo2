package com.lasley.jpmc.demo.activity

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Bundle
import com.lasley.jpmc.demo.R
import com.lasley.jpmc.demo.contracts.SearchContract
import com.lasley.jpmc.demo.fragment.SearchLocation
import com.lasley.jpmc.demo.util.Constants
import com.lasley.jpmc.demo.util.Constants.BUNDLE_DATA

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