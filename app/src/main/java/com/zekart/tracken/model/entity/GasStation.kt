package com.zekart.tracken.model.entity

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

//@Entity(tableName = "gas_stations")
@Entity(tableName = "gas_stations")
data class GasStation  (
    @ColumnInfo(name = "concern_name")
    var mConcernName:String?,
    @Embedded
    val mPositionInfo:PositionInfo
){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "station_id")
    var id: Long? = null
}