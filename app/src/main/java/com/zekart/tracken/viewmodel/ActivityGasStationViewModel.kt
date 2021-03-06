package com.zekart.tracken.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.zekart.tracken.R
import com.zekart.tracken.model.db.GasStationDataBase
import com.zekart.tracken.model.entity.Consume
import com.zekart.tracken.model.entity.GasStation
import com.zekart.tracken.model.entity.PositionInfo
import com.zekart.tracken.model.pojo.CustomLocation
import com.zekart.tracken.repository.MapRepository
import com.zekart.tracken.repository.StationRepository
import com.zekart.tracken.utils.DataAppUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ActivityGasStationViewModel(application: Application, private val idUser:Long?, idStation: Long?):AndroidViewModel(application) {

    private val mDbRepository:StationRepository
    private val mMapRepository: MapRepository
    private var mCurrentStation = MutableLiveData<GasStation>()
    private var listFuelType = MutableLiveData<List<String>>()
    private var mLocationAddress:LiveData<CustomLocation> = MutableLiveData()
    private var mNewConcernStationName:String = ""
    private var mFuelType:String = ""
    private var mConsumeFuelCount:String = ""
    private var mCostConsume:String = ""

    private var deleteStatusId = MutableLiveData<Int>()
    private var createStatusId = MutableLiveData<Long>()
    private var updateStatusId = MutableLiveData<Int>()
    private var insertConsume = MutableLiveData<Long>()

    init {
        val dao = GasStationDataBase.getDatabase(application).stationDao()
        mDbRepository = StationRepository(dao)
        mMapRepository = MapRepository(application)
        listFuelType.value =  application.resources.getStringArray(R.array.fuel_type_array).toList()

        mLocationAddress = mMapRepository.getSearchLocation()

        if (idStation != null) {
            getStationFromBd(idStation)
        }
    }

    fun getStationFromBd(id:Long) = viewModelScope.launch(Dispatchers.IO){
        mCurrentStation.postValue(mDbRepository.getStationById(id))
    }

    fun insertStation(withConsume:Boolean) = viewModelScope.launch(Dispatchers.IO){
        val tempStation = createStationEntity()
        if (tempStation!=null){
            if (withConsume){
                val tempConsume = createConsumeEntity()
                mDbRepository.createGasStationWithConsume(tempStation,tempConsume)
            }else{
                createStatusId.postValue(mDbRepository.createGasStation(tempStation))
            }
        }else{
            insertConsume.postValue(null)
        }
    }

    fun deleteStation() = viewModelScope.launch(Dispatchers.IO){
        val station = mCurrentStation.value
        if (station != null) {
            deleteStatusId.postValue(mDbRepository.deleteStation(station))
        }
    }

    fun updateStation() = viewModelScope.launch(Dispatchers.IO){
        if (mCurrentStation.value?.concern_name!= mNewConcernStationName){
            val tempStation = mCurrentStation.value
            if (tempStation!=null){
                tempStation.concern_name = mNewConcernStationName
                updateStatusId.postValue(mDbRepository.updateStation(tempStation))
            }
        }
    }

    fun insertConsumeToStation() = viewModelScope.launch(Dispatchers.IO){
        val consume = createConsumeEntity()
        insertConsume.postValue(mDbRepository.insertConsumeStation(consume))
    }

    fun checkAddressByLatLng(latLng: LatLng) = viewModelScope.launch(Dispatchers.IO){
        mMapRepository.getLocation(latLng)
    }

    fun getAddressPosition():LiveData<CustomLocation> {
        return mLocationAddress
    }

    fun getAdapterToFuelType():LiveData<List<String>>{
        return listFuelType
    }

    fun getDeviceLocation() = viewModelScope.launch(Dispatchers.IO){
        mMapRepository.getDeviceLocation()
    }

    fun getErrorFromMap():LiveData<String>{
        return mMapRepository.getMapError()
    }

    fun setNameConcernFromEdit(concern: String){
        mNewConcernStationName = concern
    }

    fun setNewConsume(typeFuel: String, countFuel:String, costFuel:String){
        mConsumeFuelCount = countFuel
        mCostConsume = costFuel
        mFuelType = typeFuel
    }

    private fun createStationEntity(): GasStation? {
        val concern = mNewConcernStationName
        val lng = mLocationAddress.value?.latLng?.longitude
        val lat = mLocationAddress.value?.latLng?.latitude
        val address = mLocationAddress.value?.mAddress
        if (lng!=null && lat!=null && address!=null){
            val pos = PositionInfo(address, lat, lng)
            return GasStation(concern,pos)
        }
        return null
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