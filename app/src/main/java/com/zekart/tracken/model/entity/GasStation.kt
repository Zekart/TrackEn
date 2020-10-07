package com.zekart.tracken.model.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gas_stations")
data class GasStation (
    @PrimaryKey
    @ColumnInfo(name = "station_id")
    val mId:Int,
    @ColumnInfo(name = "name")
    val mName:String,
    @Embedded
    val mAddress: Address
)