package com.zekart.tracken.model.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ConsumeToUser(
    @Embedded
    val mUser:User,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "user_id",
        entity = Consume::class
    )
    val mConsumeFromGasStationLists: List<Consume>
)