package com.zekart.tracken.repository

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.common.collect.ImmutableList
import com.tomtom.online.sdk.common.location.LatLng
import com.tomtom.online.sdk.map.*
import com.tomtom.online.sdk.search.OnlineSearchApi
import com.tomtom.online.sdk.search.SearchApi
import com.tomtom.online.sdk.search.api.SearchError
import com.tomtom.online.sdk.search.api.revgeo.RevGeoSearchResultListener
import com.tomtom.online.sdk.search.data.reversegeocoder.ReverseGeocoderFullAddress
import com.tomtom.online.sdk.search.data.reversegeocoder.ReverseGeocoderSearchQueryBuilder
import com.tomtom.online.sdk.search.data.reversegeocoder.ReverseGeocoderSearchResponse
import com.zekart.tracken.model.entity.MapSearchRequest
import java.lang.NullPointerException

class MapTomTomRepository(application: Application){
    private var searchApi: SearchApi = OnlineSearchApi.create(application,"3EF4mAc3omZtrDWhC5V1nrAalDYlIAqY")
    private lateinit var lastLocation: Location

    private var mResponseSearch:MutableLiveData<String> = MutableLiveData()
    private val mResponseError:MutableLiveData<SearchError> = MutableLiveData()

    fun getAddressByLatLng(latLng: LatLng) {
        val revGeoQuery = ReverseGeocoderSearchQueryBuilder(latLng.latitude, latLng.longitude).build()
        searchApi.reverseGeocoding(revGeoQuery)

        searchApi.reverseGeocoding(revGeoQuery, object: RevGeoSearchResultListener {
            override fun onSearchResult(response: ReverseGeocoderSearchResponse?) {
                val listRequest: ImmutableList<ReverseGeocoderFullAddress>? = response?.addresses
                try {
                    listRequest?.forEach {
                        mResponseSearch.value = it.address.freeformAddress
                    }
                }catch (np:NullPointerException){
                    np.printStackTrace()
                }catch (tr: ClassCastException){
                    tr.printStackTrace()
                }
            }
            override fun onSearchError(error: SearchError?) {
                mResponseError.value = error
            }
        })
    }

    fun getAddress():LiveData<String>{
        return mResponseSearch
    }
}