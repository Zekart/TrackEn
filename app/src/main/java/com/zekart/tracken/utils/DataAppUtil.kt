package com.zekart.tracken.utils

import android.content.Context

class DataAppUtil {

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

        fun getUserID(context: Context):Long{
            val sharedPref = context.getSharedPreferences("local_user_id", Context.MODE_PRIVATE)
            return sharedPref.getLong("user_id",0)
        }

        fun fromStringToInt(value: String?):Int?{
            return value?.toIntOrNull()
        }
    }
}