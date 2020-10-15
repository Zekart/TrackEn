package com.zekart.tracken.model.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zekart.tracken.model.entity.GasStationToConsume
import java.lang.reflect.Type


class GasStationToConsumeConverter {
    private val type: Type = object : TypeToken<List<GasStationToConsume?>?>() {}.type
    @TypeConverter
    fun toGasStationFromConsumeList(value: String): List<GasStationToConsume> {
        val gson = Gson()
        val type = object : TypeToken<List<GasStationToConsume>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun toGasStationToConsumeList(nestedData: List<GasStationToConsume>): String {
        val gson = Gson()
        return gson.toJson(nestedData, type);
    }
}