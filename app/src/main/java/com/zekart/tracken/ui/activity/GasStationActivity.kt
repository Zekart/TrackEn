package com.zekart.tracken.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tomtom.online.sdk.map.MapFragment
import com.tomtom.online.sdk.map.OnMapReadyCallback
import com.tomtom.online.sdk.map.TomtomMap
import com.zekart.tracken.R
import com.zekart.tracken.databinding.ActivityGasStationBinding
import com.zekart.tracken.viewmodel.ActivityGasStationViewModel
import kotlinx.android.synthetic.main.activity_gas_station.view.*


class GasStationActivity : AppCompatActivity(), OnMapReadyCallback {
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var binding: ActivityGasStationBinding

    private var viewModel:ActivityGasStationViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGasStationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this).get(ActivityGasStationViewModel::class.java)

        initMapView()
        initEditedViews(view)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menuInflater.inflate(R.menu.menu_edit_activity,menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun initEditedViews(view:View){
        viewModel?.getAddress()?.observe(this, {
            view.tx_gas_station_address.text = it.simpleAddress
        })
    }



    private fun initMapView(){
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as MapFragment
        mapFragment.getAsyncMap(this)
    }

    override fun onMapReady(tomtomMap: TomtomMap) {
        viewModel?.initMap(tomtomMap)
    }
}