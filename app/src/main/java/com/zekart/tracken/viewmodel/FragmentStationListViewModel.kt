package com.zekart.tracken.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zekart.tracken.model.db.GasStationDataBase
import com.zekart.tracken.model.entity.GasStation
import com.zekart.tracken.repository.StationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentStationListViewModel(application: Application): AndroidViewModel(application) {
    private val repository:StationRepository
    private var mStationList: LiveData<List<GasStation>> = MutableLiveData()

    init {
        val dao = GasStationDataBase.getDatabase(application).stationDao()
        repository = StationRepository(dao)
        getAllStation()
    }

    private fun getAllStation()= viewModelScope.launch(Dispatchers.IO) {
         mStationList = repository.getAllStation()
    }

    fun getStationList():LiveData<List<GasStation>>{
        return mStationList
    }

}