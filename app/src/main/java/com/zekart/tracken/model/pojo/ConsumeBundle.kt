package com.zekart.tracken.model.pojo

import com.google.gson.annotations.SerializedName
import com.zekart.tracken.model.entity.Consume

class ConsumeBundle{
    @SerializedName("consume")
    var consume:List<Consume>? = null
}