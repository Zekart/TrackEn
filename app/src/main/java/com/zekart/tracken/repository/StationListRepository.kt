package com.zekart.tracken.repository

import androidx.lifecycle.LiveData
import com.zekart.tracken.model.dao.GasStationDao
import com.zekart.tracken.model.entity.ConcernGasStation

class StationListRepository(private val stationDao: GasStationDao){
    private var mAllGasStation: LiveData<List<ConcernGasStation>> = stationDao.getAllGasStation()
    fun getAllStation() = mAllGasStation
}