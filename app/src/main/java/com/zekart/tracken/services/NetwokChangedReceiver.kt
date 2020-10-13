package com.zekart.tracken.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import java.lang.NullPointerException


class NetwokChangedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, p1: Intent?) {
        try {
            if (context!=null) {
                if (checkConnection(context)){
                    Toast.makeText(context, "Network Available Do operations", Toast.LENGTH_LONG).show();
                }
            }
        }catch (e:NullPointerException){
            e.printStackTrace()
        }

    }

    private fun checkConnection(context:Context):Boolean{
        val serviceManager = ServiceManager(context)
        return serviceManager.isNetworkAvailable()

    }
}