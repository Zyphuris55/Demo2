package com.lasley.jpmc.demo.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife
import com.lasley.jpmc.demo.R
import com.lasley.jpmc.demo.contracts.SearchContract
import com.lasley.jpmc.demo.model.UserLocationModel
import com.lasley.jpmc.demo.util.Constants


abstract class BaseActivity : AppCompatActivity(), SearchContract {
    companion object {
        //protected var lastLoc: Set<String>? = null
    }

    internal var lastLoc: Set<String>? = null
    internal var sharedPref: SharedPreferences? = null

    /**
     * This is so we can do the butterknife binding in one place.
     * Gets the layout, which is needed for butterknife binding. Must be over ridden by the activity.
     *
     * @return should be overridden If you get zero you did not override
     */
    internal abstract val layout: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPref = getPreferences(Context.MODE_PRIVATE)
        setContentView(layout)
        ButterKnife.bind(this)
    }

    abstract fun postRetrieveLocation()

    protected fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.search_container, fragment)
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .commit()
    }

    override fun updateUserLocation(userLocationModel: UserLocationModel) {
        val editor = sharedPref?.edit()
        if (editor == null)
            return
        val LotLonSet = HashSet<String>()
        LotLonSet.add(java.lang.Double.toString(userLocationModel.lng))
        LotLonSet.add(java.lang.Double.toString(userLocationModel.lat))

        editor.putStringSet(Constants.KEY_LASTLOCATION, LotLonSet)
        editor.commit()
        postRetrieveLocation()
    }
}