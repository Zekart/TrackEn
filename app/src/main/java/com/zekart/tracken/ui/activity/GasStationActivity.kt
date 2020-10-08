package com.zekart.tracken.ui.activity

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.tomtom.online.sdk.common.location.LatLng
import com.tomtom.online.sdk.map.*
import com.tomtom.online.sdk.search.OnlineSearchApi
import com.tomtom.online.sdk.search.api.SearchError
import com.tomtom.online.sdk.search.api.revgeo.RevGeoSearchResultListener
import com.tomtom.online.sdk.search.data.reversegeocoder.ReverseGeocoderSearchQueryBuilder
import com.tomtom.online.sdk.search.data.reversegeocoder.ReverseGeocoderSearchResponse
import com.zekart.tracken.R


class GasStationActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var map: TomtomMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gas_station)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as MapFragment
        mapFragment.getAsyncMap(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menuInflater.inflate(R.menu.menu_edit_activity,menu)
        return super.onCreateOptionsMenu(menu)
    }


    private fun initMap(){
//        val keysMap = mapOf(
//            ApiKeyType.MAPS_API_KEY to "maps-key",
//        )
//        MapProperties.Builder()
//            .keys(keysMap)
//            .build()

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.INTERNET
            ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.INTERNET), LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        map.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null){
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                val balloon = SimpleMarkerBalloon("ffff")
                map.addMarker(MarkerBuilder(currentLatLng).markerBalloon(balloon))
                map.centerOn(CameraPosition.builder(currentLatLng).zoom(12.0).build())

                val  m = map.markers
                initBusy(currentLatLng)
            }

        }

    }

    fun initBusy(latLng: LatLng){
        val searchApi = OnlineSearchApi.create(this,"3EF4mAc3omZtrDWhC5V1nrAalDYlIAqY")

        val revGeoQuery = ReverseGeocoderSearchQueryBuilder(latLng.latitude, latLng.longitude).build()
        searchApi.reverseGeocoding(revGeoQuery)

        searchApi.reverseGeocoding(revGeoQuery, object:RevGeoSearchResultListener{
            override fun onSearchResult(response: ReverseGeocoderSearchResponse?) {
                println(response)
            }

            override fun onSearchError(error: SearchError?) {
                println(error)
            }

        })
    }

    override fun onMapReady(tomtomMap: TomtomMap) {
        this.map = tomtomMap
        initMap()
    }
}