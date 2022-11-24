package com.accord.Nmea.utils

import java.io.File

class Common {

    companion object {

        const val _ROOT_FOLDER_NAME_ = "nmea"
        const val _ROOT_SYS = "/sdcard"
        val _NMEA_ROOT = File(_ROOT_SYS + File.separator + _ROOT_FOLDER_NAME_)
        val _NMEA_LOG =
            File(_ROOT_SYS + File.separator + _ROOT_FOLDER_NAME_ + File.separator + "logs")


    }

}