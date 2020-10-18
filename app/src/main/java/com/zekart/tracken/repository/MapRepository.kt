package com.zekart.tracken.repository

import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.zekart.tracken.R
import com.zekart.tracken.model.pojo.CustomLocation
import com.zekart.tracken.utils.Constans
import java.io.IOException

class MapRepository(private val context: Context){

    /**
     * Repository to get from Google API location info
     */

    private val mZoomMap:Float
    private lateinit var mFusedLocationProviderClient:FusedLocationProviderClient
    private var mGeocoder:Geocoder
    private var mAddress = MutableLiveData<CustomLocation>()
    private var mMapError = MutableLiveData<String>()

    init {
        mZoomMap = context.resources.getInteger(R.integer.map_zoom).toFloat()
        mGeocoder = Geocoder(context)
        initElements()
    }

    private fun initElements(){
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    }

    //Init Location client to position devices
    fun getDeviceLocation(){
        try {
            val locationResult = mFusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener {
                if (it.isSuccessful) {
                    val position = LatLng(it.result.latitude, it.result.longitude)
                    getLocation(position)
                }else {
                    mMapError.value = context.getString(R.string.error_location)
                    Log.d(Constans.TAG_GOOGLE_MAP, "Current location is null. Using defaults.")
                    Log.d(Constans.TAG_GOOGLE_MAP, "Exception: %s $it.exception")
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

    //Get address from LatLng. Return address when connection is true. Else -> set " - "
     fun getLocation(position:LatLng){
        try {
            val m = mGeocoder.getFromLocation(position.latitude, position.longitude, 1)
            if (m != null) {
                var address =""
                for (ind in m)
                    address = "" +
                            checkAddressNull(ind.locality) +
                            checkAddressNull(ind.thoroughfare) +
                            checkAddressNull(ind.subThoroughfare)


                if (address.isEmpty()){
                    address = context.getString(R.string.error_location)
                }

                mAddress.postValue(CustomLocation(
                        address,
                        position
                    ))
            }
        } catch (e: NullPointerException)  {
            mAddress.postValue(CustomLocation("-",position))
            mMapError.postValue(context.getString(R.string.error_address_location))
            Log.e(Constans.TAG_GOOGLE_MAP, "Exception: %s $e.localizedMessage")
        }
        catch (e: IOException)  {
            mAddress.postValue(CustomLocation("-",position))
            mMapError.postValue(context.getString(R.string.error_address_location))
            Log.e(Constans.TAG_GOOGLE_MAP, "Exception: %s $e.localizedMessage")
        }
    }

    //Get address can return null value. To prevent show user null -> to empty string
    private fun checkAddressNull(value:String?):String{
        return if (value.isNullOrEmpty()){
            ""
        }else{
            value.plus(".")
        }
    }

    fun getSearchLocation(): LiveData<CustomLocation> {
        return mAddress
    }

    fun getMapError():LiveData<String>{
        return mMapError
    }

}