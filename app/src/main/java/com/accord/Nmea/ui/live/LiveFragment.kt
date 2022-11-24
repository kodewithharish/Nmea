package com.accord.Nmea.ui.live

import android.content.Intent
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.accord.Nmea.R
import com.accord.Nmea.base.BaseFragment
import com.accord.Nmea.service.NmeaService
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class LiveFragment:BaseFragment<LiveViewModel>() {

    lateinit var nmea_tv:TextView
    val stringBuilder = StringBuilder()
    override fun provideLayoutId(): Int= R.layout.live_fragment
    @RequiresApi(Build.VERSION_CODES.N)
    override fun setupView(view: View) {

        nmea_tv=view.findViewById(R.id.nmealog)

        requireActivity().startService(Intent(activity, NmeaService::class.java))


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(nmea: String) {

        if (stringBuilder.length >= 512 * 1) {
            stringBuilder.setLength(0)
        }else {
            nmea_tv.bringPointIntoView(0)
            nmea_tv.text = stringBuilder
            stringBuilder.append(nmea).append("")
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this);
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this);
    }
}