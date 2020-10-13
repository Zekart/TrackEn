package com.zekart.tracken.utils

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData

class LocalDataUtil {

    companion object{
        fun saveUserID(context: Context, id:Long){
            try {
                val sharedPref = context.getSharedPreferences("local_user_id", Context.MODE_PRIVATE)
                with (sharedPref.edit()) {
                    putLong("user_id", id)
                    commit()
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }

        fun getUserID(app: Application):Long{
            val sharedPref = app.getSharedPreferences("local_user_id", Context.MODE_PRIVATE)
            return sharedPref.getLong("user_id",0)
        }
    }
}