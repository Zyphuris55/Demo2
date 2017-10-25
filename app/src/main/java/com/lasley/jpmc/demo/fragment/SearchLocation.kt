package com.lasley.jpmc.demo.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.lasley.jpmc.demo.R
import com.lasley.jpmc.demo.contracts.SearchContract
import com.lasley.jpmc.demo.util.LocationHelper

class SearchLocation(var location: String = "") : Fragment() {

    @BindView(R.id.location_enter_manual)
    internal lateinit var mEnterManual: EditText

    @BindView(R.id.location_enter_auto)
    internal lateinit var mEnterAuto: ImageView

    @BindView(R.id.location_bottom_line)
    internal lateinit var mBottomLine: View

    @BindView(R.id.address_format_error_text_view)
    internal lateinit var mErrorFormat: TextView

    private var unbinder: Unbinder? = null
    private var searchContract: SearchContract? = null

    fun getLocationString(): String {
        if(searchContract == null)
            return "";
        if (mEnterManual.text.isNotEmpty())
            return mEnterManual.text.toString()
        return ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.searchlocation_view, container, false)
        unbinder = ButterKnife.bind(this, view)

        setErrorOnAddress(false)

        if (location.length > 0)
            mEnterManual.setText(location)
        mEnterManual.setOnEditorActionListener { _, actionId, event ->
            // hitting enter submits the address
            if (actionId == EditorInfo.IME_ACTION_DONE
                    || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                submitManualAddress()
                // hide the keyboard
                val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
                true
            } else {
                false
            }
        }
        mEnterManual.setFocusableInTouchMode(true)
        mEnterManual.requestFocus()
        return view;
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            searchContract = context as SearchContract
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + " must implement SearchContract")
        }
    }

    fun submitManualAddress() {
        val userLocation = LocationHelper.getLatLongFromInput(context, mEnterManual.getText().toString())
        if (userLocation != null) {
            setErrorOnAddress(false)
            searchContract?.updateUserLocation(userLocation)
        } else {
            setErrorOnAddress(true)
        }
    }

    @OnClick(R.id.location_enter_auto)
    fun useCurrentAddress() {//todo; add geolocation
        if (LocationHelper.checkLocationPermission(context)) {
        }
    }

    private fun setErrorOnAddress(isError: Boolean) {
        if (isError) {
            mBottomLine.setBackgroundColor(Color.RED)
            mErrorFormat.setVisibility(View.VISIBLE)
        } else {
            mBottomLine.setBackgroundColor(Color.GRAY)
            mErrorFormat.setVisibility(View.GONE)
        }
    }
}