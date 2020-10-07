package com.zekart.tracken.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zekart.tracken.enum.Fuel

@Entity(tableName = "fuel_consume")
data class Consume(
    @PrimaryKey
    @ColumnInfo(name = "consume_id")
    val mId:Int,
    @ColumnInfo(name = "fuel_type")
    val mFuelType:String,
    @ColumnInfo(name = "consume_price")
    val mPriceConsume:Int
)