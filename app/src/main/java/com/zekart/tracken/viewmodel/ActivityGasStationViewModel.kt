package com.zekart.tracken.viewmodel

import android.app.Application
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
import java.lang.ClassCastException


class ActivityGasStationViewModel(application: Application, id: Int?):AndroidViewModel(application) {
    private val mDbRepository:StationRepository
    private val mMapTomTomRepository: MapTomTomRepository

    private var mCoordinateNewMarker:LatLng = LatLng()
    private var mCurrentStation:LiveData<GasStation>?
    private var listFuelType = MutableLiveData<List<String>>()

    private val mCurrentStationID = MutableLiveData<Int>()
    private var mConcernStationName:String = ""
    private var mFuelType:String = ""
    private var mConsumeFuelCount:String = ""
    private var mCostConsume:String = ""
    private var mStationAddress:String = ""

    private var mErrorInViewModel = MutableLiveData<String>()

    init {
        val dao = GasStationDataBase.getDatabase(application, viewModelScope).stationDao()
        mDbRepository = StationRepository(dao)
        mMapTomTomRepository = MapTomTomRepository(application)
        mCurrentStationID.value = id
        mCurrentStation = id?.let { mDbRepository.getStationById(it) }
        listFuelType.value =  application.resources.getStringArray(R.array.fuel_type_array).toList()
    }

    fun insertStation() = viewModelScope.launch(Dispatchers.IO){
        mDbRepository.insertStation(createStationEntity())
    }

    fun deleteStation() = viewModelScope.launch(Dispatchers.IO){
        mCurrentStation?.value?.let { mDbRepository.deleteStation(it) }
    }

    fun updateStation() = viewModelScope.launch(Dispatchers.IO){
        mCurrentStation?.value?.let {
            it.mOwner = mConcernStationName
            mDbRepository.updateStation(it)
        }
    }

    fun insertConsumeToStation() = viewModelScope.launch(Dispatchers.IO){
        val tempConsume = createConsumeEntity()
        if (mCurrentStation!=null){
            mDbRepository.insertConsumeStation(tempConsume)
        }else{
            val tempStation = createStationEntity()
            mDbRepository.insertNewStationWithConsume(tempStation,tempConsume)
        }
    }

    fun getStationFromBd() = viewModelScope.launch(Dispatchers.IO){
        mCurrentStationID.value?.let { mDbRepository.getStationById(it)}
    }

    fun getAddressFromRequest(): LiveData<String> {
        return mMapTomTomRepository.getAddress()
    }

    fun getCurrentStation():LiveData<GasStation>?{
        return mCurrentStation
    }

    fun getAdapterToFuelType():LiveData<List<String>>{
        return listFuelType
    }

    fun getAddressByLatLng(latLng: LatLng) = viewModelScope.launch(Dispatchers.IO){
        mMapTomTomRepository.getAddressByLatLng(latLng)
    }

    fun setNameConcernFromEdit(concern: String){
        mConcernStationName = concern
    }

    fun setNewConsume(typeFuel: String, countFuel:String, costFuel:String){
        mConsumeFuelCount = countFuel
        mCostConsume = costFuel
        mFuelType = typeFuel
    }

    fun setIsAddressLoading(address:String){
        mStationAddress = address
    }

    fun setCoordinateNewMarker(position:LatLng){
        mCoordinateNewMarker = position
    }

    private fun createStationEntity():GasStation{
        val lng = mCoordinateNewMarker.longitude
        val lat = mCoordinateNewMarker.latitude
        val pos = PositionInfo(mStationAddress, lat, lng)

        return GasStation(mConcernStationName,pos)
    }

    private fun createConsumeEntity():Consume{
        val consumeCount = mConsumeFuelCount
        val typeFuel = mFuelType
        val consumeCost = mCostConsume
        val stationID = mCurrentStationID.value

        return Consume(stationID,typeFuel,
            Parsing.fromStringToInt(consumeCount),
            Parsing.fromStringToInt(consumeCost))
    }


    fun checkErrorInModel():LiveData<String>{
        return mErrorInViewModel
    }


}