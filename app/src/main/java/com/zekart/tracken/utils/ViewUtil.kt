package com.zekart.tracken.utils

import android.content.Context
import android.widget.Toast
import com.zekart.tracken.R

class ViewUtil {
    companion object{
        fun showToastApplication(context: Context, typeMessage:AppEnums.ToastMessage){
            val listMessages = arrayOf(R.array.toast_error_array)
            val message = listMessages[typeMessage.id]
            Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
        }
    }
}