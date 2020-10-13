package com.zekart.tracken.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @ColumnInfo(name = "user_name")
    var mName:String?
){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    var id: Long? = null
}