package com.zekart.tracken.utils

import android.text.Editable

object Parsing {

    fun fromEditableToString(editable: Editable):String{
        return editable.toString()
    }

    fun fromEditableToInt(editable: Editable?):Int?{
        return editable.toString().toIntOrNull()
    }
}