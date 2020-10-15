package com.zekart.tracken.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Main class for consumes
 **/
@Entity(tableName = "fuel_consume")
data class Consume(
    @ColumnInfo(name = "user_id")
    var mUserId:Long?,
    @ColumnInfo(name = "station_id")
    var mStationId:Long?,
    @ColumnInfo(name = "fuel_type")
    val mFuelType:String?,
    @ColumnInfo(name = "consume_count")
    val mCountConsume:Int?,
    @ColumnInfo(name = "consume_price")
    val mPriceConsume:Int?
){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "consume_id")
    var id: Long? = null
}