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
import com.zekart.tracken.utils.DataAppUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ActivityGasStationViewModel(application: Application, private val idUser:Long?, idStation: Long?):AndroidViewModel(application) {

    private val mDbRepository:StationRepository
    private val mMapTomTomRepository: MapTomTomRepository
    private var mCoordinateNewMarker:LatLng = LatLng()
    private var mCurrentStation = MutableLiveData<GasStation>()
    private var listFuelType = MutableLiveData<List<String>>()

    private var mNewConcernStationName:String = ""
    private var mFuelType:String = ""
    private var mConsumeFuelCount:String = ""
    private var mCostConsume:String = ""
    private var mStationAddress:String = ""

    private var deleteStatusId = MutableLiveData<Int>()
    private var createStatusId = MutableLiveData<Long>()
    private var updateStatusId = MutableLiveData<Int>()
    private var insertConsume = MutableLiveData<Long>()

    init {
        val dao = GasStationDataBase.getDatabase(application).stationDao()
        mDbRepository = StationRepository(dao)
        mMapTomTomRepository = MapTomTomRepository(application)
        listFuelType.value =  application.resources.getStringArray(R.array.fuel_type_array).toList()

        if (idStation != null) {
            getStationFromBd(idStation)
        }

    }

    fun getStationFromBd(id:Long) = viewModelScope.launch(Dispatchers.IO){
        mCurrentStation.postValue(mDbRepository.getStationById(id))
    }

    fun insertStation(withConsume:Boolean) = viewModelScope.launch(Dispatchers.IO){
        val tempStation = createStationEntity()
        if (withConsume){
            val tempConsume = createConsumeEntity()
            mDbRepository.createGasStationWithConsume(tempStation,tempConsume)
        }else{
            createStatusId.postValue(mDbRepository.createGasStation(tempStation))
        }
    }

    fun deleteStation() = viewModelScope.launch(Dispatchers.IO){
        val station = mCurrentStation.value
        if (station != null) {
            deleteStatusId.postValue(mDbRepository.deleteStation(station))
        }
    }

    fun updateStation() = viewModelScope.launch(Dispatchers.IO){
        if (mCurrentStation.value?.mConcernName!= mNewConcernStationName){
            val tempStation = mCurrentStation.value
            if (tempStation!=null){
                tempStation.mConcernName = mNewConcernStationName
                updateStatusId.postValue(mDbRepository.updateStation(tempStation))
            }
        }
    }

    fun insertConsumeToStation() = viewModelScope.launch(Dispatchers.IO){
        val consume = createConsumeEntity()
        insertConsume.postValue(mDbRepository.insertConsumeStation(consume))
    }

    fun getAddressFromRequest(): LiveData<String> {
        return mMapTomTomRepository.getAddress()
    }

    fun getAdapterToFuelType():LiveData<List<String>>{
        return listFuelType
    }

    fun getAddressByLatLng(latLng: LatLng) = viewModelScope.launch(Dispatchers.IO){
        mMapTomTomRepository.getAddressByLatLng(latLng)
    }

    fun getErrorFromMap():LiveData<String?>{
        return mMapTomTomRepository.getError()
    }

    fun setNameConcernFromEdit(concern: String){
        mNewConcernStationName = concern
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

    private fun createStationEntity(): GasStation {
        val concern = mNewConcernStationName
        val lng = mCoordinateNewMarker.longitude
        val lat = mCoordinateNewMarker.latitude
        val pos = PositionInfo(mStationAddress, lat, lng)
        return GasStation(concern,pos)
    }

    private fun createConsumeEntity(): Consume {
        val consumeCount = mConsumeFuelCount
        val typeFuel = mFuelType
        val consumeCost = mCostConsume
        val stationID = mCurrentStation.value?.id

        return Consume(idUser,stationID,typeFuel,
            DataAppUtil.fromStringToInt(consumeCount),
            DataAppUtil.fromStringToInt(consumeCost))
    }

    fun getCurrentGasStation(): LiveData<GasStation>? {
        return mCurrentStation
    }

    fun onDeleteResponse():LiveData<Int?>{
        return deleteStatusId
    }

    fun onUpdateResponse():LiveData<Int?>{
        return updateStatusId
    }

    fun onInsertResponse():LiveData<Long?>{
        return createStatusId
    }

    fun onConsumeResponse():LiveData<Long?>{
        return insertConsume
    }
}