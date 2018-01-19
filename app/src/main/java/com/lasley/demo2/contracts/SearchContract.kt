package com.lasley.demo2.contracts

import com.lasley.demo2.model.UserLocationModel

interface SearchContract {
    fun updateUserLocation(userLocationModel: UserLocationModel)
}