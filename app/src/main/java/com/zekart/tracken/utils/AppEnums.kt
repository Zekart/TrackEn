package com.zekart.tracken.utils

object AppEnums {

    enum class ActivityMode{
        CREATE_NEW,
        CHANGE_CURRENT
    }

    enum class DialogMessage{
        DELETE,
        ERROR
    }

    enum class ToastMessage(val id:Int){
        NULL(1),
        EMPTY(2),
        UNKNOWN(3)
    }
}