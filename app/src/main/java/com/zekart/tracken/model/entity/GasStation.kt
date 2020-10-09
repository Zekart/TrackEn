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
    @ColumnInfo(name = "concern")
    val mStationConcernName:String,
    @Embedded
    val mPositionInfo: PositionInfo
)