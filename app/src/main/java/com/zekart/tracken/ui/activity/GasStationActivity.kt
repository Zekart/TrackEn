package com.zekart.tracken.ui.activity

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
import com.zekart.tracken.databinding.ActivityGasStationBinding
import com.zekart.tracken.databinding.ActivityMainBinding
import com.zekart.tracken.enum.Fuel
import com.zekart.tracken.viewmodel.ActivityGasStationViewModel
import kotlinx.android.synthetic.main.activity_gas_station.view.*


class GasStationActivity : AppCompatActivity(), OnMapReadyCallback {
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var map: TomtomMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var binding: ActivityGasStationBinding

    private var viewModel:ActivityGasStationViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGasStationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this).get(ActivityGasStationViewModel::class.java)

        initEditedViews(view)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menuInflater.inflate(R.menu.menu_edit_activity,menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun initEditedViews(view:View){
//        viewModel?.stationPosition.observe(this, Observer {
//
//        })
    }

    private fun getAddressByLatLng(latLng: LatLng){
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

    private fun initMapView(){
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as MapFragment
        mapFragment.getAsyncMap(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onMapReady(tomtomMap: TomtomMap) {
        this.map = tomtomMap
        setTomTomMap()
    }

    private fun setTomTomMap(){
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

                getAddressByLatLng(currentLatLng)
            }
        }
    }
}