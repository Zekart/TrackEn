package com.zekart.tracken.repository

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import com.zekart.tracken.model.dao.GasStationDao
import com.zekart.tracken.model.db.GasStationDataBase
import com.zekart.tracken.model.entity.ConcernGasStation
import com.zekart.tracken.model.entity.GasStation

class StationRepository(private val stationDao: GasStationDao){
    var mAllGasStation: LiveData<List<ConcernGasStation>>
    //var mStation:GasStation:LiveData<List<GasStation>>

    init {
        mAllGasStation = stationDao.getAllGasStation()
        //mStation = stationDao.getStation()
    }

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

    suspend fun getAllStation() = mAllGasStation
}