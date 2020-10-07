package com.zekart.tracken.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "concern")
data class Concern(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val mId:Int,
    @ColumnInfo(name = "name")
    val mName:String
)