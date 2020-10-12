package com.zekart.tracken.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
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
import com.zekart.tracken.utils.Parsing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ActivityGasStationViewModel(application: Application, id: Int?):AndroidViewModel(application) {
    private val mDbRepository:StationRepository
    private val mMapTomTomRepository: MapTomTomRepository
    private val mCurrentStationID:Int?

    private var mCoordinateNewMarker:LatLng = LatLng()
    private var mCurrentStation = MutableLiveData<GasStation>()
    private var listFuelType = MutableLiveData<List<String>>()

    private var mConcernStationName:String = ""
    private var mFuelType:String = ""
    private var mConsumeFuelCount:String = ""
    private var mCostConsume:String = ""
    private var mStationAddress:String = ""

    init {
        val dao = GasStationDataBase.getDatabase(application, viewModelScope).stationDao()
        mDbRepository = StationRepository(dao)
        mMapTomTomRepository = MapTomTomRepository(application)
        mCurrentStationID = id
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
        mCurrentStationID?.let { mDbRepository.getStationById(it)}
    }

    fun deleteStation() = viewModelScope.launch(Dispatchers.IO){
         mDbRepository.deleteStation()
    }

    fun getAddressByLatLng(latLng: LatLng) = viewModelScope.launch(Dispatchers.IO){
        mMapTomTomRepository.getAddressByLatLng(latLng)
    }


    fun getAddressFromRequest(): LiveData<String> {
        return mMapTomTomRepository.getAddress()
    }

    private fun createStationEntity(concernName:String):GasStation{
        val lng = mCoordinateNewMarker.longitude
        val lat = mCoordinateNewMarker.latitude
        val pos = PositionInfo(mStationAddress.toString(), lat, lng)

        return GasStation(concernName,pos)
    }

    private fun createConsumeEntity():Consume{
        val consumeCount = mConsumeFuelCount
        val typeFuel = mFuelType
        val consumeCost = mCostConsume

        return Consume( 0,typeFuel,
            Parsing.fromStringToInt(consumeCount),
            Parsing.fromStringToInt(consumeCost))
    }

    fun setCurrentStation(current:GasStation){
        this.mCurrentStation.value = current
    }

    fun getCurrentStation():LiveData<GasStation>?{
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


    fun setIsAddressLoading(address:String){
        mStationAddress = address
    }
}