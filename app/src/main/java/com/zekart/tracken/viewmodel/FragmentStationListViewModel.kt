package com.zekart.tracken.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.zekart.tracken.model.db.GasStationDataBase
import com.zekart.tracken.model.entity.ConcernGasStation
import com.zekart.tracken.repository.StationListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentStationListViewModel(application: Application): AndroidViewModel(application) {
    private val repository:StationListRepository
    val mStation: LiveData<List<ConcernGasStation>>

    init {
        val dao = GasStationDataBase.getDatabase(application,viewModelScope).stationDao()
        repository = StationListRepository(dao)
        mStation = repository.getAllStation()
    }

    fun getAllStation()= viewModelScope.launch(Dispatchers.IO) {
        repository.getAllStation()
    }

//    fun insert(station: GasStation) = viewModelScope.launch(Dispatchers.IO){
//        repository.insert(station)
//    }
//
//    fun delete(idStation:Int) = viewModelScope.launch(Dispatchers.IO){
//        repository.delete(idStation)
//    }
}