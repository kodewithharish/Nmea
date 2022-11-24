package com.accord.Nmea.service

import android.Manifest
import android.app.*
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.OnNmeaMessageListener
import android.os.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import com.accord.Nmea.R
import com.accord.Nmea.parser.NmeaParsingManager
import org.greenrobot.eventbus.EventBus


@RequiresApi(Build.VERSION_CODES.N)
class NmeaService : Service(), OnNmeaMessageListener,LocationListener {

    var nmeaMessageLiveData=MutableLiveData<String>("")

    private val mBinder: IBinder = LocalBinder()

    lateinit var locationManger:LocationManager

    val nmeaparsemanager=NmeaParsingManager(this)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(applicationContext,"Premission denied", Toast.LENGTH_LONG).show()
        }

        try {
            locationManger.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000L,0.0f,this)
            locationManger.addNmeaListener(this, Handler(Looper.myLooper()!!))
        } catch (e: Exception) {
            e.stackTrace
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        locationManger=getSystemService(LOCATION_SERVICE)as LocationManager
        showNotification()
    }


    override fun onNmeaMessage(message: String?, timestamp: Long) {

      //  getNmeaMessage().value=message

        EventBus.getDefault().post(message)

        nmeaparsemanager.logNmea(message,timestamp)

        //  Log.d("Nmea","Message"+message)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification() {

        val NOTIFICATION_CHANNEL_ID = "com.accord.nmea"
        val channelName = "Nmea"
        val chan = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager = (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(chan)
        val stopSelf = Intent(this, NmeaService::class.java)
        stopSelf.action = "stop"
        val pStopSelf =
            PendingIntent.getService(this, 0, stopSelf, PendingIntent.FLAG_IMMUTABLE)
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        notificationBuilder.addAction(R.drawable.ic_satilite, "STOP", pStopSelf)
        val notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.drawable.ic_settings_datums)
            .setContentTitle("Nmea is running")
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(2, notification)

    }

    override fun onLocationChanged(location: Location) {
    }

    class LocalBinder : Binder() {
        fun getService(): NmeaService {
            return NmeaService()
        }
    }



}