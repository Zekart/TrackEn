package com.zekart.tracken.ui.dialogs

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.zekart.tracken.R
import com.zekart.tracken.ui.listeners.OnAlertDialogClick


class CustomAlertDialog{
    companion object{

        fun createAlertCustomDialogBuilder(context: Context, mListener: OnAlertDialogClick, title:String, message:String): AlertDialog.Builder {
            val listener: OnAlertDialogClick? = mListener
            val builder = AlertDialog.Builder(context)
            builder.setTitle(title)
            builder.setMessage(message)

            builder.setPositiveButton(android.R.string.ok) { _, _ ->
                listener?.onOkButtonClick()
            }

            builder.setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }

            return builder

        }
    }
}