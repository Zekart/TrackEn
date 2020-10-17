package com.zekart.tracken.model.entity

import androidx.room.Embedded
import androidx.room.Relation
import java.io.Serializable

/**
 * One to many. One gas station many consumes
 **/
data class GasStationToConsume (
    @Embedded
    val station: GasStation,
    @Relation(
        parentColumn = "station_id",
        entityColumn = "station_id",
        entity = Consume::class
    )
    val consumeFromGasStationLists: List<Consume>
):Serializable