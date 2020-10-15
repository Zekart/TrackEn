package com.zekart.tracken.model.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zekart.tracken.model.entity.ConsumeToUser
import com.zekart.tracken.model.entity.GasStationToConsume
import java.lang.reflect.Type

class ConsumeToUserConverter {
    private val type: Type = object : TypeToken<List<ConsumeToUser?>?>() {}.type
    @TypeConverter
    fun fromConsumeToUser(value: String): List<ConsumeToUser> {
        val gson = Gson()
        val type = object : TypeToken<List<ConsumeToUser>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun toConsumeToUser(nestedData: List<ConsumeToUser>): String {
        val gson = Gson()
        return gson.toJson(nestedData, type);
    }
}