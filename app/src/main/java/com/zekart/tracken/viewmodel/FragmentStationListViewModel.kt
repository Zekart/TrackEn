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

class FragmentStationListViewModel(application: Application): AndroidViewModel(application) {
    private val repository:StationRepository
    //val mStation:LiveData<List<GasStation>>

    init {
        val dao = GasStationDataBase.getDatabase(application,viewModelScope).stationDao()
        repository = StationRepository(dao)
        //mStation = repository.mAllGasStation
    }

    fun insert(station: GasStation) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(station)
    }

    fun delete(idStation:Int) = viewModelScope.launch(Dispatchers.IO){
        repository.delete(idStation)
    }
}