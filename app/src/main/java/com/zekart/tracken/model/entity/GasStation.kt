package com.zekart.tracken.model.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gas_stations")
data class GasStation  (
    @ColumnInfo(name = "station_owner")
    var mOwner:String,
    @Embedded
    val mPositionInfo: PositionInfo?
){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "station_id")
    var id: Int? = null
}