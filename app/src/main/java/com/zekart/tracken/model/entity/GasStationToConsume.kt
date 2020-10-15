package com.zekart.tracken.model.entity

import androidx.room.Embedded
import androidx.room.Relation

data class GasStationToConsume (
    @Embedded
    val station: GasStation,
    @Relation(
        parentColumn = "station_id",
        entityColumn = "station_id",
        entity = Consume::class
    )
    val mConsumeFromGasStationLists: List<Consume>
)