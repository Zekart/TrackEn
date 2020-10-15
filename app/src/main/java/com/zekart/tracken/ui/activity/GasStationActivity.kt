package com.zekart.tracken.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tomtom.online.sdk.common.location.LatLng
import com.tomtom.online.sdk.map.MapFragment
import com.tomtom.online.sdk.map.OnMapReadyCallback
import com.tomtom.online.sdk.map.TomtomMap
import com.zekart.tracken.R
import com.zekart.tracken.databinding.ActivityGasStationBinding
import com.zekart.tracken.databinding.BottomSheetLayoutBinding
import com.zekart.tracken.databinding.MainMapLayoutBinding
import com.zekart.tracken.ui.dialogs.CustomAlertDialog
import com.zekart.tracken.ui.listeners.OnAlertDialogClick
import com.zekart.tracken.utils.*
import com.zekart.tracken.viewmodel.ActivityGasStationViewModel
import com.zekart.tracken.viewmodel.StationActivityFactory


class GasStationActivity : AppCompatActivity(), OnMapReadyCallback, OnAlertDialogClick {
    private lateinit var binding: ActivityGasStationBinding
    private lateinit var customAlertDialog:AlertDialog.Builder

    private var mapFragment:MapFragment? = null
    private var mMap:TomtomMap? = null
    private var mOptionMenu:Menu? = null
    private var mViewModel:ActivityGasStationViewModel? = null
    private var fusedLocationClient: FusedLocationProviderClient?= null
    private var mMapViewBinding: MainMapLayoutBinding? = null
    private var mEditViewBinding: BottomSheetLayoutBinding? = null
    private var bottomSheetBehavior:BottomSheetBehavior<View>? = null
    private var selectedViewMode:AppEnums.ActivityMode = AppEnums.ActivityMode.CREATE_NEW
    private var mCurrentStationID:Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGasStationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkIDCurrentStation()

        mEditViewBinding = binding.includeEditorLayout
        mMapViewBinding = binding.includeMapView

        mViewModel = ViewModelProvider(
            this,
            StationActivityFactory(
                application,
                DataAppUtil.getUserID(this),
                mCurrentStationID
            )
        ).get(ActivityGasStationViewModel::class.java)

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
                showCustomDialog(getString(R.string.alert_delete_title),getString(R.string.not_registered_user__dialog_message))
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showCustomDialog(title:String,message:String) {
        customAlertDialog = CustomAlertDialog.createAlertCustomDialogBuilder(this,
            this,title,message)
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
        initMapView()

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
            if (selectedViewMode == AppEnums.ActivityMode.CREATE_NEW){
                mMapViewBinding?.txGasStationAddress?.text = it.toString()
                mViewModel?.setIsAddressLoading(it)
            }
        })

        mViewModel?.getCurrentGasStation()?.observe(this, {
            if (it != null) {
                val stationPosition = it.mPositionInfo
                val latLng = LatLng(stationPosition.mLatitude, stationPosition.mLongitude)

                mEditViewBinding?.edtConcernName?.setText(it.mConcernName)
                mMapViewBinding?.txGasStationAddress?.text = stationPosition.mAddressInfo

                bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED

                drawMarkerOnMap(latLng)
                selectedViewMode = AppEnums.ActivityMode.CHANGE_CURRENT
                menuViewController(true)
            } else {
                selectedViewMode = AppEnums.ActivityMode.CREATE_NEW
                menuViewController(false)
            }
        })


        mViewModel?.getErrorFromMap()?.observe(this, {
            mMapViewBinding?.txGasStationAddress?.text = it
            ViewUtil.showToastApplication(this,getString(R.string.unknown_error))
        })

        mViewModel?.onDeleteResponse()?.observe(this,{
            if (it!=null){
                ViewUtil.showToastApplication(this, getString(R.string.db_success_deleted))
                finish()
            }
        })
//
        mViewModel?.onInsertResponse()?.observe(this,{
            if (it!=null){
                if (it>0){
                    ViewUtil.showToastApplication(this,getString(R.string.db_success_inserted))
                    mViewModel?.getStationFromBd(it)
                }else{
                    ViewUtil.showToastApplication(this,getString(R.string.unknown_error))
                }
            }
        })
//
        mViewModel?.onUpdateResponse()?.observe(this,{
            if (it!=null){
                ViewUtil.showToastApplication(this,getString(R.string.db_success_updated))
            }
        })

        mViewModel?.onConsumeResponse()?.observe(this,{
            if (it!=null){
                ViewUtil.showToastApplication(this,getString(R.string.db_success_consume))
            }
        })
    }

    private fun initMapView(){
        mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as MapFragment
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mapFragment?.getAsyncMap(this)
    }

    override fun onMapReady(tomtomMap: TomtomMap) {
        mMap = tomtomMap
        setUpMap()
        initViewElementsWithObservers()
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
            )
            return
        }

        mMap?.isMyLocationEnabled = true

        try {
            fusedLocationClient?.lastLocation?.addOnSuccessListener(this) { location ->
                if (location != null){
                    if (selectedViewMode != AppEnums.ActivityMode.CHANGE_CURRENT){
                        val currentLatLng = LatLng(location.latitude, location.longitude)
                        mMap?.centerOn(CustomMapHelper.getMapCenterZoomOption(currentLatLng))
                        mViewModel?.setCoordinateNewMarker(currentLatLng)
                        mViewModel?.getAddressByLatLng(currentLatLng)
                    }
                }

            }

            mMap?.addOnMapLongClickListener {
                if (selectedViewMode != AppEnums.ActivityMode.CHANGE_CURRENT){
                    mViewModel?.getAddressByLatLng(it)
                    mViewModel?.setCoordinateNewMarker(it)
                    mMap?.removeMarkers()
                    mMap?.addMarker(CustomMapHelper.constructMarker(this, it))

                    bottomSheetBehavior?.state = (BottomSheetBehavior.STATE_EXPANDED)
                }
            }
        }catch (il: IllegalStateException){
            il.printStackTrace()
        }
    }

    private fun drawMarkerOnMap(latLng: LatLng){
        if (mMap!=null){
            mMap.let {
                it?.addMarker(
                    CustomMapHelper.constructMarker(
                        this,
                        latLng
                    )
                )
                it
            }
            mMap?.centerOn(CustomMapHelper.getMapCenterZoomOption(latLng))
        }
    }


    private fun menuViewController(showEditedOption:Boolean){

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
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED)
                    ) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                        mapFragment?.getAsyncMap(this)
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