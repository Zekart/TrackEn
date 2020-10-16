package com.zekart.tracken.repository

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.zekart.tracken.R
import com.zekart.tracken.utils.Constans
import java.lang.NullPointerException

class MapRepository(private val context: Context){

    private val mZoomMap:Float
    private lateinit var mFusedLocationProviderClient:FusedLocationProviderClient
    private var mLastKnownLocation = MutableLiveData<Location>()
    private var mGeocoder:Geocoder
    private var mAddressList = MutableLiveData<List<Address>>()
    private var mMapError = MutableLiveData<String>()
    init {
        mZoomMap = context.resources.getInteger(R.integer.map_zoom).toFloat()
        mGeocoder = Geocoder(context)
        initElements()
    }

    private fun initElements(){
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    }

    fun getDeviceLocation(){
        try {
            val locationResult = mFusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener {
                if (it.isSuccessful) {
                    // Set the map's camera position to the current location of the device.
                    mLastKnownLocation.value = it.result
//                    mMap.moveCamera(
//                        CameraUpdateFactory.newLatLngZoom(
//                            LatLng(mLastKnownLocation.latitude,mLastKnownLocation.longitude),mZoomMap))
                }else {
                    mLastKnownLocation.value = null
                    Log.d(Constans.TAG_GOOGLE_MAP, "Current location is null. Using defaults.")
                    Log.d(Constans.TAG_GOOGLE_MAP, "Exception: %s $it.exception")
                    //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, mZoomMap))
                }
            }
        }
        catch (e: SecurityException)  {
            mMapError.value = e.localizedMessage
            Log.e(Constans.TAG_GOOGLE_MAP, "Exception: %s $e.localizedMessage")
        }
        catch (np: NullPointerException)  {
            mMapError.value = np.localizedMessage
            Log.e(Constans.TAG_GOOGLE_MAP, "Exception: %s $np.localizedMessage")
        }
    }

    fun getSearchLocation(latitude:Double,longitude:Double){
        try {
            mAddressList.value =  mGeocoder.getFromLocation(latitude, longitude, 1)

        } catch (e: NullPointerException)  {
            mMapError.value = e.localizedMessage
            Log.e(Constans.TAG_GOOGLE_MAP, "Exception: %s $e.localizedMessage")
        }
    }


//    private var searchApi: SearchApi = OnlineSearchApi.create(application,"3EF4mAc3omZtrDWhC5V1nrAalDYlIAqY")
//
//    private var mResponseSearch = MutableLiveData<String>()
//    private var mResponseError = MutableLiveData<String>()
//
//    fun getAddressByLatLng(latLng: LatLng) {
//        val revGeoQuery = ReverseGeocoderSearchQueryBuilder(latLng.latitude, latLng.longitude).build()
//        searchApi.reverseGeocoding(revGeoQuery)
//
//        searchApi.reverseGeocoding(revGeoQuery, object: RevGeoSearchResultListener {
//            override fun onSearchResult(response: ReverseGeocoderSearchResponse?) {
//                val listRequest: ImmutableList<ReverseGeocoderFullAddress>? = response?.addresses
//                try {
//                    listRequest?.forEach {
//                        mResponseSearch.value = it.address.freeformAddress
//                    }
//                }catch (np:NullPointerException){
//                    np.printStackTrace()
//                }catch (tr: ClassCastException){
//                    tr.printStackTrace()
//                }
//            }
//            override fun onSearchError(error: SearchError?) {
//                mResponseError.postValue(error?.localizedMessage)
//            }
//        })
//    }

    fun getCurrentLocation(): LiveData<Location> {
        return mLastKnownLocation
    }

    fun getSearchLocation(): LiveData<List<Address>> {
        return mAddressList
    }

    fun getMapError():LiveData<String>{
        return mMapError
    }

}