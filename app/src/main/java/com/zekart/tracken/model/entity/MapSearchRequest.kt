package com.zekart.tracken.model.entity

import com.tomtom.online.sdk.common.location.LatLng

data class MapSearchRequest(
    var simpleAddress:String,
    val position:LatLng
)
