package com.zekart.tracken.utils

import android.text.Editable

class Parsing {

    companion object{
        fun fromEditableToString(editable: Editable):String{
            return editable.toString()
        }

        fun fromEditableToInt(value: Editable?):Int?{
            return value.toString().toIntOrNull()
        }

        fun fromStringToInt(value: String?):Int?{
            return value?.toIntOrNull()
        }
    }

}