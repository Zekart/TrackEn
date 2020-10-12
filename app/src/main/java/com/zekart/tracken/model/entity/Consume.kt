package com.zekart.tracken.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fuel_consume")
data class Consume(
    @ColumnInfo(name = "owner_station")
    val mStationId:Int?,
    @ColumnInfo(name = "fuel_type")
    val mFuelType:String?,
    @ColumnInfo(name = "consume_count")
    val mCountConsume:Int?,
    @ColumnInfo(name = "consume_price")
    val mPriceConsume:Int?
){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "consume_id")
    var id: Int? = null
}