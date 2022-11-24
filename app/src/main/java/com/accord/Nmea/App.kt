package com.accord.Nmea

import android.app.Application
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.accord.Nmea.service.NmeaService
import com.accord.Nmea.utils.Common
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import java.util.*

@AndroidEntryPoint
class App : Application() {

    private lateinit var file: File
    private var instance: App? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate() {
        super.onCreate()
        instance = this

        startService(Intent(applicationContext, NmeaService::class.java))

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun logNmea(str: String) {
        if (file == null) {
            file = File(
                Common._NMEA_ROOT,
                File.separator + "Logs" + File.separator + "current.txt"
            )
            // If file doesn't exists, then create it
            if (file.exists()) {
                try {
                    file.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        try {
            Files.write(
                file.toPath(),
                Arrays.asList(System.currentTimeMillis().toString() + " : " + str),
                StandardCharsets.UTF_8,
                StandardOpenOption.APPEND,
                StandardOpenOption.CREATE
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


}