package com.zekart.tracken.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.widget.ArrayAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tomtom.online.sdk.common.location.LatLng
import com.zekart.tracken.R
import com.zekart.tracken.model.db.GasStationDataBase
import com.zekart.tracken.model.entity.Consume
import com.zekart.tracken.model.entity.GasStation
import com.zekart.tracken.model.entity.PositionInfo
import com.zekart.tracken.repository.MapTomTomRepository
import com.zekart.tracken.repository.StationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ActivityGasStationViewModel(application: Application):AndroidViewModel(application) {
    private val mDbRepository:StationRepository
    private val mMapTomTomRepository: MapTomTomRepository

    private var mCoordinateNewMarker:LatLng = LatLng()
    private var mCurrentStationId:Int? = null
    private var mCurrentStation = MutableLiveData<GasStation>()
    private var listFuelType = MutableLiveData<List<String>>()

    var mConcernStationName = MutableLiveData<String>()
    var mFuelType = MutableLiveData<String>()
    var mConsumeFuelCount = MutableLiveData<Int>()
    var mCostConsume = MutableLiveData<Int>()
    var mStationAddress = MutableLiveData<String>()

    init {
        val dao = GasStationDataBase.getDatabase(application, viewModelScope).stationDao()
        mDbRepository = StationRepository(dao)
        mMapTomTomRepository = MapTomTomRepository(application)
    }

    fun insertStation(concernName:String) = viewModelScope.launch(Dispatchers.IO){
        createStationEntity(concernName).let {
            mDbRepository.insertStation(it)
        }
    }

    fun insertNewStationWithConsume(consumeName:String) = viewModelScope.launch(Dispatchers.IO){
        val tempStation = createStationEntity(consumeName)
        val tempConsume = createConsumeEntity()
//        mDbRepository.insertNewStationWithConsume(tempStation,tempConsume)
    }

    fun insertConsumeToStation() = viewModelScope.launch(Dispatchers.IO){
        //mDbRepository.insertConsumeStation(createConsumeEntity())
    }

    fun getStationFromBd() = viewModelScope.launch(Dispatchers.IO){
        mCurrentStationId?.let { mDbRepository.getStationById(it)}
    }

    fun deleteStation() = viewModelScope.launch(Dispatchers.IO){
         mDbRepository.deleteStation()
    }

    @SuppressLint("MissingPermission")
    fun getAddressByLatLng(latLng: LatLng) = viewModelScope.launch(Dispatchers.IO){
        mMapTomTomRepository.getAddressByLatLng(latLng)
    }

    fun setStationId(id:Int?){
        mCurrentStationId = id
    }

    fun getAddressFromRequest(): LiveData<String> {
        return mMapTomTomRepository.getAddress().also {
            mStationAddress.value = it.value
        }
    }

    private fun createStationEntity(concernName:String):GasStation{
        val lng = mCoordinateNewMarker.longitude
        val lat = mCoordinateNewMarker.latitude
        val pos = PositionInfo(mStationAddress.value.toString(), lat, lng)

        return GasStation(concernName,pos)
    }

    private fun createConsumeEntity():Consume{
        val consumeCount = mConsumeFuelCount.value
        val typeFuel = mFuelType.value
        val consumeCost = mCostConsume.value

        return Consume( 0,typeFuel,consumeCount,consumeCost)
    }

    fun setCurrentStation(current:GasStation){
        this.mCurrentStation.value = current
    }

    fun getCurrentStation():LiveData<GasStation>{
        return mDbRepository.getCurrentStation()
    }

    fun setCoordinateCurrent(position:LatLng){
        mCoordinateNewMarker = position
    }

    fun setCoordinateNewMarker(position:LatLng){
        mCoordinateNewMarker = position
    }

    fun createAdapterToFuelType(context: Context){
        listFuelType.value =  context.resources.getStringArray(R.array.fuel_type_array).toList()
    }

    fun getAdapterToFuelType():LiveData<List<String>>{
        return listFuelType
    }
}