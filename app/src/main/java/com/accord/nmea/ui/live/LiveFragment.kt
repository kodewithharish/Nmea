package com.accord.nmea.ui.live

import android.location.Location
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.accord.nmea.R
import com.accord.nmea.base.BaseFragment
import com.accord.nmea.ui.MainActivity.Companion.TAG
import com.github.petr_s.nmea.GpsSatellite
import com.github.petr_s.nmea.NMEAHandler
import com.github.petr_s.nmea.NMEAParser
import com.github.petr_s.nmea.basic.BasicNMEAHandler
import com.github.petr_s.nmea.basic.BasicNMEAParser
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


        val handler = object: BasicNMEAHandler {
            override fun onStart() {
            }

            override fun onRMC(
                date: Long?,
                time: Long?,
                posStatus: String?,
                latitude: Double?,
                longitude: Double?,
                speed: Float?,
                direction: Float?,
                magVar: Float?,
                magVarDir: String?,
                modeInc: String?,
                isGN: Boolean
            ) {
                Log.i("parser", "onRMC:1 ")

            }

            override fun onGGA(
                time: Long?,
                latitude: Double?,
                longitude: Double?,
                altitude: Float?,
                quality: BasicNMEAHandler.FixQuality?,
                satellites: Int?,
                hdop: Float?,
                age: Float?,
                station: Int?,
                isGN: Boolean
            ) {
                Log.i("parser", "onGGA:2 ")

            }

            override fun onGSV(
                satellites: Int?,
                index: Int?,
                prn: Int?,
                elevation: Float?,
                azimuth: Float?,
                snr: Int?,
                isGN: Boolean
            ) {
                Log.i("parser", "onGSV:1 ")

            }

            override fun onGSA(
                mode: String?,
                type: BasicNMEAHandler.FixType?,
                prns: Set<Int?>?,
                pdop: Float?,
                hdop: Float?,
                vdop: Float?,
                isGN: Boolean
            ) {
                Log.i("parser", "onGSA:1 ")

            }

            override fun onUnrecognized(sentence: String?) {
            }

            override fun onBadChecksum(expected: Int, actual: Int) {
            }

            override fun onException(e: Exception?) {
            }

            override fun onFinished() {
            }


        }

        val parser =  BasicNMEAParser(handler)
       // parser.parse("\$GNRMC,201603.00,A,1257.45022,N,07738.49119,E,0.487,,131222,,,D,V*12")
       // parser.parse("\$GNRMC,201603.00,A,1257.45022,N,07738.49119,E,0.487,,131222,,,D,V*12")
parser.parse("\$GPGSV,5,1,17,02,33,332,29,05,54,016,09,11,12,054,,12,53,197,32,1*6E")

       /* val parser = new BasicNMEAParser(handler)
        parser.parse("$GPRMC,163407.000,A,5004.7485,N,01423.8956,E,0.04,36.97,180416,,*38")

        val handler = object: NMEAHandler {
            override fun onStart() {
                Log.i("parser", "onStart:1 ")
            }

            override fun onLocation(location: Location?) {

                Log.i("parser", "onLocation "+location.toString())

            }


            override fun onSatellites(satellites: List<GpsSatellite?>?) {

                Log.i("parser", "satellites: "+satellites.toString())

            }

            override fun onUnrecognized(sentence: String?) {
            }

            override fun onBadChecksum(expected: Int, actual: Int) {
            }

            override fun onException(e: Exception?) {
            }

            override fun onFinish() {

                Log.i("parser", "onFinish ")

            }

        }
        val parser = NMEAParser(handler)
        //parser.parse("\$GPRMC,163407.000,A,5004.7485,N,01423.8956,E,0.04,36.97,180416,,*38")
        parser.parse("\$GPGGA,163407.000,5004.7485,N,01423.8956,E,1,07,1.7,285.7,M,45.5,M,,0000*5F")
*/
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
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}