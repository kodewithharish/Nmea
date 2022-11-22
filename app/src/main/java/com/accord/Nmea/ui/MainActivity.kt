package com.accord.Nmea.ui

import android.os.Bundle
import com.accord.Nmea.R
import com.accord.Nmea.base.BaseActivity
import dagger.hilt.android.components.ActivityComponent

class MainActivity : BaseActivity<MainViewModel>() {

    companion object {
        const val TAG = "LoginActivity"
    }


    override fun provideLayoutId(): Int =R.layout.activity_main

    override fun injectDependencies(activityComponent: ActivityComponent) {



    }

    override fun setupView(savedInstanceState: Bundle?) {
    }

}