package com.zekart.tracken.model.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * One to many. One user many consumes
 **/
data class ConsumeToUser (
    @Embedded
    val user:User,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "user_id",
        entity = Consume::class
    )
    val consumeFromGasStationLists: List<Consume>
)