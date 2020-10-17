package com.zekart.tracken.model.pojo

import com.google.android.gms.maps.model.LatLng

data class CustomLocation(
    val mAddress:String,
    val latLng: LatLng
)