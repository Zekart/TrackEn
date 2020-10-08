package com.zekart.tracken.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.zekart.tracken.model.db.GasStationDataBase
import com.zekart.tracken.model.entity.ConcernGasStation
import com.zekart.tracken.model.entity.GasStation
import com.zekart.tracken.repository.StationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityGasStationViewModel(application: Application):AndroidViewModel(application) {
//    private val mRepository:StationRepository
//    private val mAllStation:LiveData<ConcernGasStation>
//
//    init {
//        val stationDao = GasStationDataBase.getDatabase(application).stationDao()
//
//        mRepository = StationRepository(stationDao)
//        mAllStation = mRepository.mAllGasStation
//    }
//
//    fun insert(station: GasStation) = viewModelScope.launch(Dispatchers.IO){
//        mRepository.insert(station)
//    }
}