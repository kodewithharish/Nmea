package com.accord.Nmea.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.accord.Nmea.R
import com.accord.Nmea.base.BaseActivity
import dagger.hilt.android.components.ActivityComponent

class SplashScreenActivity : BaseActivity<MainViewModel>() {

    override fun provideLayoutId(): Int = R.layout.activity_splash_screen

    override fun injectDependencies(activityComponent: ActivityComponent) {

    }

    override fun setupView(savedInstanceState: Bundle?) {

        Handler(Looper.getMainLooper()).postDelayed(Runnable { //This method will be executed once the timer is over
            // Start your app main activity
            startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
            // close this activity
            finish()
        }, 3000)


    }


}