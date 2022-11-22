package com.accord.Nmea.base

import androidx.lifecycle.ViewModel

abstract class BaseViewModel:ViewModel() {


    protected open fun forcedLogoutUser() {
        // do something
    }

    abstract fun onCreate()
}