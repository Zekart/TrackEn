package com.zekart.tracken.model.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * One to many. One user many consumes
 **/
data class ConsumeToUser (
    @Embedded
    val mUser:User,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "user_id",
        entity = Consume::class
    )
    val mConsumeFromGasStationLists: List<Consume>
)