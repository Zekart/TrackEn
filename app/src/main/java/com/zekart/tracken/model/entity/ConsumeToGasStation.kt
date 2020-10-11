package com.zekart.tracken.model.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ConsumeToGasStation (
    @Embedded
    val mGasStation:GasStation,
    @Relation(
        parentColumn = "station_id",
        entityColumn = "owner_station",
        entity = Consume::class
    )
    val mConsumeFromGasStationLists: List<Consume>
)