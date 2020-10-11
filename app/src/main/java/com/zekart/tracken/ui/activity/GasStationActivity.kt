package com.zekart.tracken.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tomtom.online.sdk.common.location.LatLng
import com.tomtom.online.sdk.map.CameraPosition
import com.tomtom.online.sdk.map.MapFragment
import com.tomtom.online.sdk.map.OnMapReadyCallback
import com.tomtom.online.sdk.map.TomtomMap
import com.zekart.tracken.R
import com.zekart.tracken.databinding.ActivityGasStationBinding
import com.zekart.tracken.databinding.BottomSheetLayoutBinding
import com.zekart.tracken.databinding.MainMapLayoutBinding
import com.zekart.tracken.utils.Constans
import com.zekart.tracken.utils.CustomMapHelper
import com.zekart.tracken.utils.Parsing
import com.zekart.tracken.utils.ViewModeEnum
import com.zekart.tracken.viewmodel.ActivityGasStationViewModel


class GasStationActivity : AppCompatActivity(), OnMapReadyCallback{
    private lateinit var binding:ActivityGasStationBinding
    private lateinit var mapFragment:MapFragment
    private lateinit var mMap:TomtomMap
    private var mViewModel:ActivityGasStationViewModel? = null
    private var fusedLocationClient: FusedLocationProviderClient?= null
    private var mUserWantCreateNewStation:Boolean = false
    private var includeMapView:MainMapLayoutBinding? = null
    private var mIncludeEditView:BottomSheetLayoutBinding? = null
    private var bottomSheetBehavior:BottomSheetBehavior<View>? = null
    private var selectedViewMode:ViewModeEnum = ViewModeEnum.CREATE_NEW
    private var mCurrentStationID:Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGasStationBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this

        val view = binding.root
        setContentView(view)

        checkSelectedViewMode()

        mIncludeEditView = binding.includeEditorLayout
        includeMapView = binding.includeMapView

        mViewModel = ViewModelProvider(this).get(ActivityGasStationViewModel::class.java)

        initViewElements()
    }

    private fun checkSelectedViewMode(){
        val intent = intent
        if (intent.extras != null){
            intent.extras.let {
                if (it != null) {
                    val key  = it.getInt(Constans.START_STATION_ACTIVITY, -1)
                    selectedViewMode = if ( key < 0){
                        ViewModeEnum.CREATE_NEW
                        //onCreateNewStationMode()
                    }else{
                        ViewModeEnum.CHANGE_CURRENT
                    }
                    mCurrentStationID = key
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menuInflater.inflate(R.menu.menu_edit_activity, menu)
        if (!mUserWantCreateNewStation){
            menu?.setGroupVisible(R.id.menu_edit_group, false);
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_item_add_consume -> {
                mIncludeEditView?.linerConsumeFuel.let {
                    when (it?.visibility) {
                        View.VISIBLE -> {
                            it.visibility = View.GONE
                        }
                        View.GONE -> {
                            it.visibility = View.VISIBLE
                        }
                    }
                }
            }

            R.id.menu_item_change_gas_station -> {

            }
            R.id.menu_item_delete_gas_station -> {
                mViewModel?.deleteStation()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initViewElements(){
        bottomSheetBehavior = BottomSheetBehavior.from(mIncludeEditView!!.bottomSheet)

        mIncludeEditView?.buttonCreateUpdateStation?.setOnClickListener {
            setStationToDb()
        }

        mViewModel?.getAdapterToFuelType()?.observe(this,{
            val adapter = ArrayAdapter<String>(
                this,
                R.layout.support_simple_spinner_dropdown_item, it)
            mIncludeEditView?.filledExposedDropdown?.setAdapter(adapter)
        })

        mViewModel?.getAddressFromRequest()?.observe(this, {
            mIncludeEditView?.txGasStationAddress?.text = it.toString()
        })

        initMapView()
    }

    private fun initMapView(){
        mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as MapFragment
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mapFragment.getAsyncMap(this)
    }


    override fun onMapReady(tomtomMap: TomtomMap) {
        mMap = tomtomMap
        setUpMap()
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }
        mMap.isMyLocationEnabled = true

        fusedLocationClient?.lastLocation?.addOnSuccessListener(this) {
                location ->
            if (location != null){
//                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
//                val balloon = SimpleMarkerBalloon("You are Here")
//                map.addMarker(MarkerBuilder(currentLatLng).markerBalloon(balloon))
                mMap.centerOn(CustomMapHelper.getMapCenterZoomOption(currentLatLng))
                mViewModel?.setCoordinateCurrent(currentLatLng)
                mViewModel?.getAddressByLatLng(currentLatLng)
            }

        }

        mMap.addOnMapLongClickListener {
            mViewModel?.getAddressByLatLng(it)
            mViewModel?.setCoordinateNewMarker(it)
            mMap.removeMarkers()
            mMap.addMarker(CustomMapHelper.constructMarker(this, it))

            bottomSheetBehavior?.state = (BottomSheetBehavior.STATE_EXPANDED)
        }

        mViewModel?.setStationId(mCurrentStationID)
        mViewModel?.getStationFromBd()

        mViewModel?.getCurrentStation()?.observe(this, {
            if (it != null){
                val stationPosition = it.mPositionInfo
                if (stationPosition != null) {
                    val latLng = LatLng(stationPosition.mLatitude, stationPosition.mLongitude)

                    mIncludeEditView?.edtConcernName?.setText(it.mOwner)
                    mIncludeEditView?.txGasStationAddress?.text = stationPosition.mAddressInfo

                    bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED

                    drawMarkerOnMap(latLng)
                }
            }
        })
    }

    private fun setStationToDb(){
        mIncludeEditView?.linerConsumeFuel.let { lnt ->
            val newNameStation = mIncludeEditView?.edtConcernName?.text.toString()
            when(lnt?.visibility){
                View.VISIBLE ->{

                    mViewModel?.mConcernStationName?.let { type ->
                        type.value = mIncludeEditView?.edtConcernName?.text.toString()
                    }

                    mViewModel?.mConsumeFuelCount?.let { consume ->
                        consume.value = Parsing.fromEditableToInt(mIncludeEditView?.edtConsumeCount?.text)
                    }
                    mViewModel?.mCostConsume?.let { cost ->
                        cost.value = Parsing.fromEditableToInt(mIncludeEditView?.edtConsumeCost?.text)
                    }
                    mViewModel?.mFuelType?.let { type ->
                        type.value = mIncludeEditView?.filledExposedDropdown?.text.toString()
                    }

                    if(mCurrentStationID == -1){
                        mViewModel?.insertNewStationWithConsume(newNameStation)
                    }else{
                        mViewModel?.insertConsumeToStation()
                    }
                }
                View.GONE ->{
                    mViewModel?.insertStation(newNameStation)
                }
                else -> return
            }
        }
    }

    private fun drawMarkerOnMap(latLng:LatLng){
        mMap.let {
            it.addMarker(
                CustomMapHelper.constructMarker(
                    this,
                    latLng
                )
            )
            it.centerOn(CustomMapHelper.getMapCenterZoomOption(latLng))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {

        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(
                            this@GasStationActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED)
                    ) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}