package com.zekart.tracken.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

class ViewUtil {
    companion object{
        fun showToastApplication(context: Context, typeMessage:String){
            Toast.makeText(context,typeMessage,Toast.LENGTH_SHORT).show()
        }

        fun showSnackBar(view: View,message:String){
            Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .show()
        }
    }
}