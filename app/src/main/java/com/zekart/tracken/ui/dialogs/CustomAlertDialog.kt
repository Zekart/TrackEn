package com.zekart.tracken.ui.dialogs

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.zekart.tracken.ui.listeners.OnAlertDialogClick


class CustomAlertDialog{
    companion object{

        fun createAlertCustomDialogBuilder(context: Context, mListener: OnAlertDialogClick, title:String, message:String): AlertDialog.Builder {
            val listener: OnAlertDialogClick? = mListener
            val builder = AlertDialog.Builder(context)
            builder.setTitle(title)
            builder.setMessage(message)

            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                listener?.onOkButtonClick()
            }

            builder.setNegativeButton(android.R.string.no) { dialog, which ->
                dialog.dismiss()
            }

            return builder

        }
    }
}