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
import com.google.android.gms.maps.CameraUpdateFactory
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
    private lateinit var mEditView: BottomSheetLayoutBinding
    private var mOptionMenu:Menu? = null
    private var mViewModel:ActivityGasStationViewModel? = null
    private var bottomSheetBehavior:BottomSheetBehavior<View>? = null
    private var selectedViewMode:AppEnums.ActivityMode = AppEnums.ActivityMode.CREATE_NEW
    private var mMap:GoogleMap? = null
    private var mCurrentStationID:Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGasStationBinding.inflate(layoutInflater)
        mEditView = binding.includeEditorLayout
        setContentView(binding.root)

        checkIDCurrentStation()

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

    // Check station id from intent
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

        //Set mode to current activity. Which allow change view elements
        selectedViewMode = when(mCurrentStationID){
            null -> AppEnums.ActivityMode.CREATE_NEW
            -1L, 0L -> AppEnums.ActivityMode.CREATE_NEW
            else ->{
                AppEnums.ActivityMode.CHANGE_CURRENT
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menuInflater.inflate(R.menu.menu_edit_activity, menu)
        if (menu != null) {
            mOptionMenu = menu
            if (selectedViewMode == AppEnums.ActivityMode.CHANGE_CURRENT){
                menuViewController(true)
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_item_save -> {
                saveStationToDb()
            }
            R.id.menu_item_change_gas_station -> {
                mEditView.txtInputLayoutConcernName.requestFocusFromTouch()
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

    private fun menuViewController(showEditedOption: Boolean){
        mOptionMenu?.setGroupVisible(R.id.menu_edit_group, showEditedOption)
    }

    private fun initViewElements(){
        bottomSheetBehavior = BottomSheetBehavior.from(mEditView.bottomSheet)

        //Set Listener to select view. LinerConsumeFuel - view with edit text fields
        mEditView.chekIfNeedConsume.setOnCheckedChangeListener { _, cheked ->
            when (cheked) {
                true -> {
                    mEditView.linerConsumeFuel.visibility = View.VISIBLE
                }
                false -> {
                    mEditView.linerConsumeFuel.visibility = View.GONE
                }
            }
        }
    }

    private fun initViewElementsWithObservers(){
        //Init Array adapter from Array from values/string
        mViewModel?.getAdapterToFuelType()?.observe(this, {
            val adapter = ArrayAdapter(
                this,
                R.layout.support_simple_spinner_dropdown_item, it
            )
            mEditView.filledExposedDropdown.setAdapter(adapter)
        })

        //Set value to address on top textView
        mViewModel?.getAddressPosition()?.observe(this, {
            if (selectedViewMode == AppEnums.ActivityMode.CREATE_NEW) {
                if (it == null){
                    binding.txAddressPosition.text = getString(R.string.error_location)
                }else{
                    binding.txAddressPosition.text = it.mAddress
                }

                mMap?.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(it.latLng.latitude, it.latLng.longitude),
                        resources.getInteger(R.integer.map_zoom).toFloat()
                    )
                )
            }
        })

        //Return current station entity. If station not null - set View to Edit mode
        mViewModel?.getCurrentGasStation()?.observe(this, {
            if (it != null) {
                val stationPosition = it.position_info
                val latLng = LatLng(stationPosition.latitude, stationPosition.longitude)

                mEditView.edtConcernName.setText(it.concern_name)
                binding.txAddressPosition.text = stationPosition.address_info
                bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED

                menuViewController(true)
                selectedViewMode = AppEnums.ActivityMode.CHANGE_CURRENT
                drawMarkerOnMap(latLng,it.concern_name.toString())
            }
        })

        //Return map errors
        mViewModel?.getErrorFromMap()?.observe(this, {
            ViewUtil.showToastApplication(this, it)
        })

        //Show message success/error from response from db to delete station
        mViewModel?.onDeleteResponse()?.observe(this, {
            if (it != null) {
                ViewUtil.showToastApplication(this, getString(R.string.db_success_deleted))
                finish()
            } else {
                ViewUtil.showToastApplication(this, getString(R.string.db_error_deleted))
            }
        })

        //Show message success/error from response from db to insert station
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

        //Show message success/error from response from db to update station
        mViewModel?.onUpdateResponse()?.observe(this, {
            if (it != null) {
                ViewUtil.showToastApplication(this, getString(R.string.db_success_updated))
            } else {
                ViewUtil.showToastApplication(this, getString(R.string.db_error_updated))
            }
        })

        //Show message success/error from response from db to add consume to station
        mViewModel?.onConsumeResponse()?.observe(this, {
            if (it != null) {
                ViewUtil.showToastApplication(this, getString(R.string.db_success_consume))
            } else {
                ViewUtil.showToastApplication(this, getString(R.string.db_error_consume))
            }
        })
    }

    //Show user dialog if station set to delete
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

    private fun saveStationToDb(){
        val nameConcern = mEditView.edtConcernName.text.toString()

        if (nameConcern.isNotEmpty()){
            val consumeChecked = mEditView.chekIfNeedConsume.isChecked
            mViewModel?.setNameConcernFromEdit(nameConcern)

            //If select consume is checked
            if (consumeChecked){
                val fuelType = mEditView.filledExposedDropdown.text.toString()
                val consumeCount = mEditView.edtConsumeCount.text.toString()
                val consumePrice= mEditView.edtConsumeCost.text.toString()

                if (fuelType.isNotEmpty() && consumeCount.isNotEmpty() && consumePrice.isNotEmpty()){
                    mViewModel?.setNewConsume(fuelType, consumeCount, consumePrice)
                }else{
                    ViewUtil.showToastApplication(this, getString(R.string.error_empty_field))
                }
            }

            //Check what need to do after press button save
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
            ViewUtil.showToastApplication(this, getString(R.string.error_empty_field))
        }
    }

    /**
     * Maps block
     **/
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

        mMap?.setOnMapLongClickListener(this)

        if (selectedViewMode == AppEnums.ActivityMode.CREATE_NEW){
            ViewUtil.showToastApplication(this,"Create new option")
            mViewModel?.getDeviceLocation()
        }
        initViewElementsWithObservers()
    }

    override fun onMapLongClick(location: LatLng?) {
        if (selectedViewMode != AppEnums.ActivityMode.CHANGE_CURRENT){
            if (location!=null){
                mMap?.clear()
                drawMarkerOnMap(location,"New station")
                mViewModel?.checkAddressByLatLng(location)
                bottomSheetBehavior?.state = (BottomSheetBehavior.STATE_EXPANDED)
            }else{
                ViewUtil.showToastApplication(this, getString(R.string.error_catch_data))
            }
        }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        TODO("Not yet implemented")
    }

    //Draws marker on map
    private fun drawMarkerOnMap(latLng: LatLng, title: String){
        mMap?.clear()
        mMap?.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(title)
        )
        var zoomlevel = mMap?.cameraPosition?.zoom

        if (zoomlevel!=null){

            if (zoomlevel < 10){
                zoomlevel = resources.getInteger(R.integer.map_zoom).toFloat()
            }

            //Zooming map
            mMap?.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(latLng.latitude, latLng.longitude),
                    zoomlevel
                )
            )
        }
    }

    override fun onBackPressed() {
        this.finish()
        super.onBackPressed()
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