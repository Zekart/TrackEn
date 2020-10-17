package com.zekart.tracken.utils

import android.util.Log
import java.io.IOException

class NetworkUtil {
    companion object{
        fun isOnline(): Boolean? {
            val runtime = Runtime.getRuntime()
            try {
                val ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8")
                val exitValue = ipProcess.waitFor()
                return exitValue == 0
            } catch (e: IOException) {
                Log.d("test", "IOException EXCEPTION");
                e.printStackTrace()
            } catch (e: InterruptedException) {
                Log.d("test", "InterruptedException EXCEPTION");
                e.printStackTrace()
            }
            return false
        }
    }
}