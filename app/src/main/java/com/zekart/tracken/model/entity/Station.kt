package com.zekart.tracken.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Station (
    @PrimaryKey
    val id:Int,
    @ColumnInfo(name = "")
    val name:String,
    val positionInfo:Position
)