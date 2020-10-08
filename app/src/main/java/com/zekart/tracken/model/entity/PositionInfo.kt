package com.zekart.tracken.model.entity

import androidx.room.ColumnInfo

data class PositionInfo(
    @ColumnInfo(name = "address_info")
    val mAddressInfo:String,
    @ColumnInfo(name = "lat")
    val mLatitude:Double,
    @ColumnInfo(name = "lng")
    val mLongitude:Double
)