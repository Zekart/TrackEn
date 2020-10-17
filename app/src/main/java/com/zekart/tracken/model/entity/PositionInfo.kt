package com.zekart.tracken.model.entity

import androidx.room.ColumnInfo

/**
 * Embeded class for gas station
 **/
data class PositionInfo(
    @ColumnInfo(name = "address_info")
    val address_info:String,
    @ColumnInfo(name = "lat")
    val latitude:Double,
    @ColumnInfo(name = "lng")
    val longitude:Double
)