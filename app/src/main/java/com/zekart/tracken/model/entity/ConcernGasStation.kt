package com.zekart.tracken.model.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ConcernGasStation(
    @Embedded val mConcern:Concern,
    @Relation(
        parentColumn = "id",
        entityColumn = "station_id"
    )
    val mGasStationLists: List<GasStation>
)