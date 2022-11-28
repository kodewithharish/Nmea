package com.accord.nmea.ui.live

import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.accord.nmea.R
import com.accord.nmea.base.BaseFragment
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