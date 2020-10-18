package com.zekart.tracken.model.pojo

import com.google.gson.annotations.SerializedName
import com.zekart.tracken.model.entity.GasStation
import java.io.Serializable

data class StationBundle (
    @SerializedName("station")
    var station_list:List<GasStation>? = null
):Serializable