package com.zekart.tracken.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zekart.tracken.model.db.GasStationDataBase
import com.zekart.tracken.model.entity.Consume
import com.zekart.tracken.model.entity.ConsumeToGasStation
import com.zekart.tracken.repository.StationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentStatisticViewModel(application: Application): AndroidViewModel(application) {
    private val repository: StationRepository
    private var mConsumeList: LiveData<List<ConsumeToGasStation>> = MutableLiveData()

    init {
        val dao = GasStationDataBase.getDatabase(application,viewModelScope).stationDao()
        repository = StationRepository(dao)
    }

    fun allConsume()= viewModelScope.launch(Dispatchers.IO) {
        mConsumeList = repository.getAllConsume()
    }

    fun getAllConsumeList():LiveData<List<ConsumeToGasStation>>{
        return mConsumeList
    }
}