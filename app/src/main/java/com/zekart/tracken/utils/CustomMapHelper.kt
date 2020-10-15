package com.zekart.tracken.utils

import android.content.Context
import com.tomtom.online.sdk.common.location.LatLng
import com.tomtom.online.sdk.map.CameraPosition
import com.tomtom.online.sdk.map.Icon
import com.tomtom.online.sdk.map.MarkerAnchor
import com.tomtom.online.sdk.map.MarkerBuilder
import com.zekart.tracken.R

/**
 * Helper class for map
 *
 * **/

class CustomMapHelper {
    companion object{

        //Draw custom marker
        fun constructMarker(context: Context,latLng: LatLng): MarkerBuilder {
            return MarkerBuilder(latLng)
                .icon(Icon.Factory.fromResources(context,R.drawable.marker_gas_station))
                .iconAnchor(MarkerAnchor.Center)
                .decal(true)
        }

        //Centring on map
        fun getMapCenterZoomOption(position: LatLng): CameraPosition {
            return CameraPosition.builder(position).animationDuration(100).zoom(15.0).build()
        }
    }
}