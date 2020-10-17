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
    var user_id:Long?,
    @ColumnInfo(name = "station_id")
    var station_id:Long?,
    @ColumnInfo(name = "fuel_type")
    val fuel_type:String?,
    @ColumnInfo(name = "consume_count")
    val count_consume:Int?,
    @ColumnInfo(name = "consume_price")
    val price_consume:Int?
){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "consume_id")
    var id: Long? = null
}


