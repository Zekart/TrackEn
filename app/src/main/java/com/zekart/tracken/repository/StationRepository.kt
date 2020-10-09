package com.zekart.tracken.repository

import com.zekart.tracken.model.dao.GasStationDao
import com.zekart.tracken.model.entity.GasStation

class StationRepository(private val stationDao: GasStationDao){
//    //private var mAllGasStation: LiveData<List<ConcernGasStation>>
//    //private var mStation:GasStation:LiveData<List<GasStation>>
//
//    init {
//        mAllGasStation = stationDao.getAllGasStation()
//        mStation = stationDao.getStation()
//    }

    suspend fun insert(station: GasStation){
        stationDao.insertGasStation(station)
    }

    //TODO update method
//    suspend fun update(station: GasStation){
//        stationDao.deleteGasStation(idStation)
//    }

    suspend fun delete(idStation:Int){
        stationDao.deleteGasStation(idStation)
    }

    suspend fun getAllStation() = stationDao.getAllGasStation()
}