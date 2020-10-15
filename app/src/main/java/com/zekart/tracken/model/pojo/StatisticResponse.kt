package com.zekart.tracken.model.pojo

import com.zekart.tracken.model.entity.Consume
import com.zekart.tracken.model.entity.User

data class StatisticResponse(
    val mConcernName:String?,
    val mStationAddress:String?,
    val mCountUserConsume:Int?
)