package com.zekart.tracken.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.zekart.tracken.model.db.GasStationDataBase
import com.zekart.tracken.model.entity.GasStation
import com.zekart.tracken.repository.StationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentStationListViewModel(application: Application): AndroidViewModel(application) {
    private val repository:StationRepository
    private var mStationList: LiveData<List<GasStation>>? = null

    init {
        val dao = GasStationDataBase.getDatabase(application,viewModelScope).stationDao()
        repository = StationRepository(dao)
        getAllStation()
    }

    private fun getAllStation()= viewModelScope.launch(Dispatchers.IO) {
        mStationList = repository.getAllStation()
    }

//    fun insert(station: GasStation) = viewModelScope.launch(Dispatchers.IO){
//        repository.insert(station)
//    }
//
//    fun delete(idStation:Int) = viewModelScope.launch(Dispatchers.IO){
//        repository.delete(idStation)
//    }

    fun getStationList() = mStationList

}