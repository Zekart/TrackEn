package com.zekart.tracken.model.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Main entity for gas station
 **/

@Entity(tableName = "gas_stations")
data class GasStation  (
    @ColumnInfo(name = "concern_name")
    var concern_name:String?,
    @Embedded
    val position_info:PositionInfo
){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "station_id")
    var id: Long? = null
}