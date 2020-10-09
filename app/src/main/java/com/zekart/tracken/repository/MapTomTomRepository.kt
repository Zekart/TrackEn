package com.zekart.tracken.repository

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.common.collect.ImmutableList
import com.tomtom.online.sdk.common.location.LatLng
import com.tomtom.online.sdk.map.CameraPosition
import com.tomtom.online.sdk.map.MarkerBuilder
import com.tomtom.online.sdk.map.SimpleMarkerBalloon
import com.tomtom.online.sdk.map.TomtomMap
import com.tomtom.online.sdk.search.OnlineSearchApi
import com.tomtom.online.sdk.search.SearchApi
import com.tomtom.online.sdk.search.api.SearchError
import com.tomtom.online.sdk.search.api.revgeo.RevGeoSearchResultListener
import com.tomtom.online.sdk.search.data.reversegeocoder.ReverseGeocoderFullAddress
import com.tomtom.online.sdk.search.data.reversegeocoder.ReverseGeocoderSearchQueryBuilder
import com.tomtom.online.sdk.search.data.reversegeocoder.ReverseGeocoderSearchResponse
import com.zekart.tracken.model.entity.MapSearchRequest
import java.lang.NullPointerException

class MapTomTomRepository(private val application: Application){

    private lateinit var mMap: TomtomMap
    private var searchApi: SearchApi = OnlineSearchApi.create(application,"3EF4mAc3omZtrDWhC5V1nrAalDYlIAqY")
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    private val mResponseSearch:MutableLiveData<MapSearchRequest> = MutableLiveData()
    private val mResponseError:MutableLiveData<SearchError> = MutableLiveData()


    fun setTomTomMap(map: TomtomMap){
        if (ActivityCompat.checkSelfPermission(application,Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(application,Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            return
        }

        this.mMap = map
        mMap.isMyLocationEnabled = true

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null){
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                val balloon = SimpleMarkerBalloon("ffff")
                mMap.addMarker(MarkerBuilder(currentLatLng).markerBalloon(balloon))
                mMap.centerOn(CameraPosition.builder(currentLatLng).zoom(15.0).build())

                getAddressByLatLng(currentLatLng)
            }
        }
    }

    private fun getAddressByLatLng(latLng: LatLng){
        val revGeoQuery = ReverseGeocoderSearchQueryBuilder(latLng.latitude, latLng.longitude).build()
        searchApi.reverseGeocoding(revGeoQuery)

        searchApi.reverseGeocoding(revGeoQuery, object: RevGeoSearchResultListener {
            override fun onSearchResult(response: ReverseGeocoderSearchResponse?) {

                val listRequest: ImmutableList<ReverseGeocoderFullAddress>? = response?.addresses
                var mSearchRequest: MapSearchRequest? = null

                try {
                    listRequest?.forEach {
                        val simpleAddress = it.address.freeformAddress
                        val position = it.position
                        mSearchRequest = MapSearchRequest(simpleAddress,position)
                    }

                }catch (np:NullPointerException){
                    np.printStackTrace()
                }catch (tr: ClassCastException){
                    tr.printStackTrace()
                }

                mSearchRequest.let {
                    mResponseSearch.value = it
                }
            }
            override fun onSearchError(error: SearchError?) {
                mResponseError.value = error
            }
        })
    }

    fun getAddressInfo() = mResponseSearch
}