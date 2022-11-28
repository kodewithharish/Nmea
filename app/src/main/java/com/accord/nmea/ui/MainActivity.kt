package com.accord.nmea.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.accord.nmea.R
import com.accord.nmea.base.BaseActivity
import com.accord.nmea.databinding.ActivityMainBinding
import com.accord.nmea.service.NmeaService
import com.accord.nmea.ui.live.ViewpagerAdapter
import com.accord.nmea.utils.PermisssionUtils
import com.accord.nmea.utils.SharedPref
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener


@RequiresApi(Build.VERSION_CODES.N)
class MainActivity : BaseActivity<MainViewModel>() {

    companion object {
        const val TAG = "LoginActivity"
    }

    lateinit var locationManger: LocationManager
    lateinit var mainService: NmeaService
    lateinit var mBinding: ActivityMainBinding
    lateinit var viewModel: MainViewModel


    override fun provideLayoutId(): Int {
        return  R.layout.activity_main
    }

    override fun injectDependencies() {
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun setupView(savedInstanceState: Bundle?) {

        mBinding = mViewDataBinding as ActivityMainBinding


        mBinding.viewpager.adapter = ViewpagerAdapter(supportFragmentManager, lifecycle)

        viewModel = ViewModelProvider(this, defaultViewModelProviderFactory)[MainViewModel::class.java]

        if (SharedPref(this).getValueInt(SharedPref.KEY_NAMES.LOG_Button_Status) == 0) {
            mBinding.fabIcon.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24)
        } else {
            mBinding.fabIcon.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)

        }

        mBinding.fabIcon.setOnClickListener(View.OnClickListener {

            if (SharedPref(this).getValueInt(SharedPref.KEY_NAMES.LOG_Button_Status) == 0) {
                mBinding.fabIcon.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
                SharedPref(this).save(SharedPref.KEY_NAMES.LOG_Button_Status, 1)
            } else {
                mBinding.fabIcon.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24)
                SharedPref(this).save(SharedPref.KEY_NAMES.LOG_Button_Status, 0)

            }
        })

        // bindService(Intent(applicationContext, NmeaService::class.java), serviceConnection, BIND_AUTO_CREATE)

        mBinding.bottomNavBar.setOnItemSelectedListener { it ->

            when (it.itemId) {
                R.id.page_1 -> {
                    mBinding.viewpager.setCurrentItem(0, true)
                    true
                }
                R.id.page_2 -> {

                    mBinding.viewpager.setCurrentItem(1, true)
                    true
                }
                R.id.page_3 -> {

                    mBinding.viewpager.setCurrentItem(2, true)

                    true
                }
                else -> false
            }
        }


        mBinding.viewpager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
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
                        mBinding.bottomNavBar.selectedItemId = R.id.page_1
                    }
                    1 -> {
                        mBinding.bottomNavBar.selectedItemId = R.id.page_2
                    }
                    2 -> {
                        mBinding.bottomNavBar.selectedItemId = R.id.page_3
                    }
                }

            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })


    }


    override fun setupObservers() {
        super.setupObservers()

        viewModel.fab_btnLivedata.observe(this, Observer {
            if (it) {
                if (SharedPref(this).getValueInt(SharedPref.KEY_NAMES.LOG_Button_Status) == 0) {
                    mBinding.fabIcon.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
                    SharedPref(this).save(SharedPref.KEY_NAMES.LOG_Button_Status, 1)
                } else {
                    mBinding.fabIcon.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24)
                    SharedPref(this).save(SharedPref.KEY_NAMES.LOG_Button_Status, 0)

                }
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


    fun checkPermission() {
        Dexter.withContext(this@MainActivity)
            .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()) {
                            Log.d(TAG, "OK")
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    p1: PermissionToken?
                ) {

                    if (p1 != null) {
                        p1.continuePermissionRequest()
                    }
                }

            })
            .withErrorListener {
                Log.d(TAG, "" + it.name)
            }
            .check()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1000 && grantResults.isNotEmpty()) {
            var denidepermissionlist = mutableListOf<String>()

            denidepermissionlist.clear()

            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("UserInfo", " Accepted ${permissions[i]}")
                } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    denidepermissionlist.add(permissions[i])
                }

            }

            var pemissionnames = ""

            for (j in denidepermissionlist) {
                pemissionnames = "$pemissionnames\\$j"
            }

            if (pemissionnames.isNotEmpty()) {
                PermisssionUtils(this).showBackgroundPermissionAlert(this, pemissionnames)
            }


        }


    }


}