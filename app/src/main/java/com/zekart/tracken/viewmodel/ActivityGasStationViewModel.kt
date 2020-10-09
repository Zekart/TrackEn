package com.zekart.tracken.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tomtom.online.sdk.map.TomtomMap
import com.tomtom.online.sdk.search.data.reversegeocoder.ReverseGeocoderSearchResponse
import com.zekart.tracken.model.db.GasStationDataBase
import com.zekart.tracken.model.entity.GasStation
import com.zekart.tracken.model.entity.MapSearchRequest
import com.zekart.tracken.model.entity.PositionInfo
import com.zekart.tracken.repository.MapTomTomRepository
import com.zekart.tracken.repository.StationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityGasStationViewModel(application: Application):AndroidViewModel(application) {
    private val mDbRepository:StationRepository
    private val mMapTomTomRepository:MapTomTomRepository
    //private var mTomTomMap:TomtomMap

    val mConcernName = MutableLiveData<String>()
    val stationPosition = MutableLiveData<PositionInfo>()

    var mAddress:MutableLiveData<MapSearchRequest> = MutableLiveData()

    //val consu

    init {
        val dao = GasStationDataBase.getDatabase(application,viewModelScope).stationDao()
        mDbRepository = StationRepository(dao)
        mMapTomTomRepository = MapTomTomRepository(application)
        //mStation = repository.mAllGasStation
    }

    fun insertStation(station: GasStation) = viewModelScope.launch(Dispatchers.IO){
        mDbRepository.insert(station)
    }

    fun updateStation(station: GasStation) = viewModelScope.launch(Dispatchers.IO){
        //repository.insert(station)
    }

    fun deleteStation(idStation:Int) = viewModelScope.launch(Dispatchers.IO){
        mDbRepository.delete(idStation)
    }

    fun initMap(map:TomtomMap) = viewModelScope.launch(Dispatchers.IO){
//        mTomTomMap.
//        mTomTomMap = map
        mMapTomTomRepository.setTomTomMap(map)
        getAddress()
    }

    fun getAddress() = mMapTomTomRepository.getAddressInfo()
}