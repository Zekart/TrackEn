package com.zekart.tracken.ui.activity

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.zekart.tracken.R
import com.zekart.tracken.databinding.ActivityGasStationBinding
import com.zekart.tracken.databinding.BottomSheetLayoutBinding
import com.zekart.tracken.ui.dialogs.CustomAlertDialog
import com.zekart.tracken.ui.listeners.OnAlertDialogClick
import com.zekart.tracken.utils.*
import com.zekart.tracken.viewmodel.ActivityGasStationViewModel
import com.zekart.tracken.viewmodel.StationActivityFactory


class GasStationActivity : AppCompatActivity(),OnAlertDialogClick, OnMapReadyCallback,
    GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {
    private lateinit var binding: ActivityGasStationBinding
    private lateinit var customAlertDialog:AlertDialog.Builder
    private var mOptionMenu:Menu? = null
    private var mViewModel:ActivityGasStationViewModel? = null
    private var mEditViewBinding: BottomSheetLayoutBinding? = null
    private var bottomSheetBehavior:BottomSheetBehavior<View>? = null
    private var selectedViewMode:AppEnums.ActivityMode = AppEnums.ActivityMode.CREATE_NEW
    private var mMap:GoogleMap? = null
    private var mCurrentStationID:Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGasStationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkIDCurrentStation()

        mEditViewBinding = binding.includeEditorLayout

        mViewModel = ViewModelProvider(
            this,
            StationActivityFactory(
                application,
                DataAppUtil.getUserID(this),
                mCurrentStationID
            )
        ).get(ActivityGasStationViewModel::class.java)

        if (SelfPermissions.checkAndRequestPermissions(this)){
            initMapView()
        }

        initViewElements()
    }

    private fun checkIDCurrentStation(){
        val intent = intent
        if (intent.extras != null){
            intent.extras.let {
                if (it != null) {
                    val key  = it.getLong(Constans.START_STATION_ACTIVITY, -1)
                    mCurrentStationID = key
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menuInflater.inflate(R.menu.menu_edit_activity, menu)
        if (menu != null) {
            mOptionMenu = menu
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_item_save -> {
                saveSelectedToDb()
            }
            R.id.menu_item_change_gas_station -> {
                mEditViewBinding?.txtInputLayoutConcernName?.requestFocusFromTouch()
            }
            R.id.menu_item_delete_gas_station -> {
                showCustomDialog(
                    getString(R.string.alert_delete_title),
                    getString(R.string.alert_delete_message)
                )
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        this.finish()
        super.onBackPressed()
    }

    private fun showCustomDialog(title: String, message: String) {
        customAlertDialog = CustomAlertDialog.createAlertCustomDialogBuilder(
            this,
            this, title, message
        )
        customAlertDialog.show()
    }

    override fun onOkButtonClick() {
        mViewModel?.deleteStation()
    }

    private fun saveSelectedToDb(){
        val nameConcern = mEditViewBinding?.edtConcernName?.text.toString()

        if (nameConcern.isNotEmpty()){
            val consumeChecked = mEditViewBinding?.chekIfNeedConsume?.isChecked!!
            mViewModel?.setNameConcernFromEdit(nameConcern)

            if (consumeChecked){
                mViewModel?.setNewConsume(
                    mEditViewBinding?.filledExposedDropdown?.text.toString(),
                    mEditViewBinding?.edtConsumeCount?.text.toString(),
                    mEditViewBinding?.edtConsumeCost?.text.toString()
                )
            }

            when(selectedViewMode){
                AppEnums.ActivityMode.CREATE_NEW -> {
                    mViewModel?.insertStation(consumeChecked)
                }
                AppEnums.ActivityMode.CHANGE_CURRENT -> {
                    if (consumeChecked) {
                        mViewModel?.insertConsumeToStation()
                    } else {
                        mViewModel?.updateStation()
                    }
                }
            }
        }else{
            ViewUtil.showToastApplication(this, getString(R.string.unknown_error))
        }
    }

    private fun initViewElements(){
        bottomSheetBehavior = BottomSheetBehavior.from(mEditViewBinding!!.bottomSheet)

        mEditViewBinding?.chekIfNeedConsume?.setOnCheckedChangeListener { _, cheked ->
            when (cheked) {
                true -> {
                    mEditViewBinding?.linerConsumeFuel?.visibility = View.VISIBLE
                }
                false -> {
                    mEditViewBinding?.linerConsumeFuel?.visibility = View.GONE
                }
            }
        }
    }

    private fun initViewElementsWithObservers(){
        mViewModel?.getAdapterToFuelType()?.observe(this, {
            val adapter = ArrayAdapter(
                this,
                R.layout.support_simple_spinner_dropdown_item, it
            )
            mEditViewBinding?.filledExposedDropdown?.setAdapter(adapter)
        })

        mViewModel?.getAddressFromRequest()?.observe(this, {
            if (selectedViewMode == AppEnums.ActivityMode.CREATE_NEW) {
                //mMapViewBinding?.txGasStationAddress?.text = it.toString()
                //mViewModel?.setIsAddressLoading(it)
                println()
            }
        })

        mViewModel?.getCurrentGasStation()?.observe(this, {
            if (it != null) {
//                val stationPosition = it.mPositionInfo
//                val latLng = LatLng(stationPosition.mLatitude, stationPosition.mLongitude)
//
//                mEditViewBinding?.edtConcernName?.setText(it.mConcernName)
//                mMapViewBinding?.txGasStationAddress?.text = stationPosition.mAddressInfo
//
//                bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
//
//                drawMarkerOnMap(latLng)
//                selectedViewMode = AppEnums.ActivityMode.CHANGE_CURRENT
//                menuViewController(true)
            } else {
                selectedViewMode = AppEnums.ActivityMode.CREATE_NEW
                menuViewController(false)
            }
        })


        mViewModel?.getErrorFromMap()?.observe(this, {
            //mMapViewBinding?.txGasStationAddress?.text = it getString(R.string.unknown_error)
            ViewUtil.showToastApplication(this,it)
        })

        mViewModel?.onDeleteResponse()?.observe(this, {
            if (it != null) {
                ViewUtil.showToastApplication(this, getString(R.string.db_success_deleted))
                finish()
            } else {
                ViewUtil.showToastApplication(this, getString(R.string.db_error_deleted))
            }
        })
//
        mViewModel?.onInsertResponse()?.observe(this, {
            if (it != null) {
                if (it > 0) {
                    ViewUtil.showToastApplication(this, getString(R.string.db_success_inserted))
                    mViewModel?.getStationFromBd(it)
                } else {
                    ViewUtil.showToastApplication(this, getString(R.string.unknown_error))
                }
            } else {
                ViewUtil.showToastApplication(this, getString(R.string.db_error_inserted))
            }
        })
//
        mViewModel?.onUpdateResponse()?.observe(this, {
            if (it != null) {
                ViewUtil.showToastApplication(this, getString(R.string.db_success_updated))
            } else {
                ViewUtil.showToastApplication(this, getString(R.string.db_error_updated))
            }
        })

        mViewModel?.onConsumeResponse()?.observe(this, {
            if (it != null) {
                ViewUtil.showToastApplication(this, getString(R.string.db_success_consume))
            } else {
                ViewUtil.showToastApplication(this, getString(R.string.db_error_consume))
            }
        })
    }

    private fun initMapView(){
        try {
            val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }catch (e: NullPointerException){
            ViewUtil.showToastApplication(this, getString(R.string.unknown_error))
            e.printStackTrace()
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap?.isMyLocationEnabled = true

        setUpMap()
        initViewElementsWithObservers()
    }

    override fun onMapLongClick(location: LatLng?) {
        if (selectedViewMode != AppEnums.ActivityMode.CHANGE_CURRENT){
            if (location!=null){
                mMap?.clear()
                mMap?.addMarker(
                    MarkerOptions()
                        .position(location)
                        .title("Marker")
                )

//                mViewModel?.getAddressByLatLng(it)
//                mViewModel?.setCoordinateNewMarker(it)
                bottomSheetBehavior?.state = (BottomSheetBehavior.STATE_EXPANDED)
            }else{
                ViewUtil.showToastApplication(this, getString(R.string.error_catch_data))
            }
        }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        TODO("Not yet implemented")
    }

    private fun setUpMap() {
        try {
//            fusedLocationClient?.lastLocation?.addOnSuccessListener(this) { location ->
//                if (location != null){
//                    if (selectedViewMode != AppEnums.ActivityMode.CHANGE_CURRENT){
//                        val currentLatLng = LatLng(location.latitude, location.longitude)
//                        mMap?.centerOn(CustomMapHelper.getMapCenterZoomOption(currentLatLng))
//                        mViewModel?.setCoordinateNewMarker(currentLatLng)
//                        mViewModel?.getAddressByLatLng(currentLatLng)
//                    }
//                }
//
//            }
//
//            mMap?.addOnMapLongClickListener {
//                if (selectedViewMode != AppEnums.ActivityMode.CHANGE_CURRENT){
//                    mViewModel?.getAddressByLatLng(it)
//                    mViewModel?.setCoordinateNewMarker(it)
//                    mMap?.removeMarkers()
//                    mMap?.addMarker(CustomMapHelper.constructMarker(this, it))
//
//                    bottomSheetBehavior?.state = (BottomSheetBehavior.STATE_EXPANDED)
//                }
//            }
        }catch (il: IllegalStateException){
            il.printStackTrace()
        }
    }

    private fun menuViewController(showEditedOption: Boolean){

        mOptionMenu?.setGroupVisible(R.id.menu_edit_group, showEditedOption)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(
                            this@GasStationActivity,
                            ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED)
                    ) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                        initMapView()
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
                return
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }




}