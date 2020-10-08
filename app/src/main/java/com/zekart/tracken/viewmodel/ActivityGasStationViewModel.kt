package com.zekart.tracken.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zekart.tracken.model.db.GasStationDataBase
import com.zekart.tracken.model.entity.GasStation
import com.zekart.tracken.model.entity.PositionInfo
import com.zekart.tracken.repository.StationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityGasStationViewModel(application: Application):AndroidViewModel(application) {
    private val repository:StationRepository

    val concernName = MutableLiveData<String>()
    val stationPosition = MutableLiveData<PositionInfo>()
    //val consu

    init {
        val dao = GasStationDataBase.getDatabase(application,viewModelScope).stationDao()
        repository = StationRepository(dao)
        //mStation = repository.mAllGasStation
    }

    fun insert(station: GasStation) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(station)
    }

    fun update(station: GasStation) = viewModelScope.launch(Dispatchers.IO){
        //repository.insert(station)
    }

    fun delete(idStation:Int) = viewModelScope.launch(Dispatchers.IO){
        repository.delete(idStation)
    }
}