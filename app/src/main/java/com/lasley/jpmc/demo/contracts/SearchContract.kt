package com.lasley.jpmc.demo.contracts

import com.lasley.jpmc.demo.model.UserLocationModel

interface SearchContract {
    fun updateUserLocation(userLocationModel: UserLocationModel)
}