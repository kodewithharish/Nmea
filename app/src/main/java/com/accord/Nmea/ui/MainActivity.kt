package com.accord.Nmea.ui

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.accord.Nmea.R
import com.accord.Nmea.base.BaseActivity
import com.accord.Nmea.service.NmeaService
import com.accord.Nmea.ui.live.ViewpagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.components.ActivityComponent


@RequiresApi(Build.VERSION_CODES.N)
class MainActivity : BaseActivity<MainViewModel>() {
    companion object {
        const val TAG = "LoginActivity"
    }

    lateinit var viewpager: ViewPager2
    lateinit var bottomNavBar: BottomNavigationView
    lateinit var fab: FloatingActionButton
    lateinit var mainService: NmeaService

    override fun provideLayoutId(): Int = R.layout.activity_main
    override fun injectDependencies(activityComponent: ActivityComponent) {
    }

    override fun setupView(savedInstanceState: Bundle?) {

        viewpager = findViewById(R.id.viewpager)
        bottomNavBar = findViewById(R.id.bottom_nav_bar)
        fab = findViewById(R.id.fab_icon)

        viewpager.adapter = ViewpagerAdapter(supportFragmentManager, lifecycle)


        fab.setOnClickListener(View.OnClickListener {

           // startService(Intent(this, NmeaService::class.java))
        })


       // bindService(Intent(applicationContext, NmeaService::class.java), serviceConnection, BIND_AUTO_CREATE)

        bottomNavBar.setOnItemSelectedListener { it ->

            when (it.itemId) {
                R.id.page_1 -> {
                    viewpager.setCurrentItem(0, true)
                    true
                }
                R.id.page_2 -> {

                    viewpager.setCurrentItem(1, true)
                    true
                }
                R.id.page_3 -> {

                    viewpager.setCurrentItem(2, true)

                    true
                }
                else -> false
            }
        }


        viewpager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        bottomNavBar.selectedItemId = R.id.page_1
                    }
                    1 -> {
                        bottomNavBar.selectedItemId = R.id.page_2
                    }
                    2 -> {
                        bottomNavBar.selectedItemId = R.id.page_3
                    }
                }

            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })


    }


   /* val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            mainService = (service as NmeaService.LocalBinder).getService()
            initObserversDependentOnMainService()

        }
        override fun onServiceDisconnected(name: ComponentName) {}
    }
*/

    private fun initObserversDependentOnMainService() {

        mainService.nmeaMessageLiveData.observe(this, Observer<String> { msg: String? ->
            Log.d("MainActivity", "" + msg.toString());
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)

        menuInflater.inflate(R.menu.menu_main, menu)


    }




}