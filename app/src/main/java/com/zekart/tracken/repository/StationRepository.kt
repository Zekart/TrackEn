package com.zekart.tracken.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zekart.tracken.model.dao.FirebaseDaoImpl
import com.zekart.tracken.model.dao.GasStationDao
import com.zekart.tracken.model.entity.Consume
import com.zekart.tracken.model.entity.ConsumeToGasStation
import com.zekart.tracken.model.entity.GasStation

class StationRepository(private val stationDao: GasStationDao){
    private var mRequestCommandDone = MutableLiveData<Int>()
    private var mExternalDb: FirebaseDaoImpl

    init {
        mExternalDb = FirebaseDaoImpl()
        stationDao.getAllGasStation()
        stationDao.getAllConsume()
    }

    fun insertStation(station: GasStation){
       stationDao.insertGasStation(station)
    }

    fun deleteStation(station: GasStation){
        stationDao.deleteGasStation(station)
    }

    fun updateStation(station: GasStation){
        stationDao.updateGasStation(station)
    }

    fun insertConsumeStation(consume: Consume){
        stationDao.insertConsume(consume)
    }

    fun insertNewStationWithConsume(station: GasStation, consume: Consume){
        val tempId = stationDao.insertGasStation(station)
        if (tempId!= null && tempId > -1){
            consume.mStationId = tempId
            insertConsumeStation(consume)
        }
    }

    fun getStationById(id:Long):LiveData<GasStation>{
        return stationDao.getGasStationById(id)
    }

    fun getAllStation():LiveData<List<GasStation>>{
        return stationDao.getAllGasStation()
    }

    fun getAllConsume():LiveData<List<ConsumeToGasStation>>{
       return stationDao.getAllConsume()
    }

    fun insertToFire(station: GasStation){
        mExternalDb.saveToFireBase(station)
    }

    fun getRequestCommand():LiveData<Int>{
        return mRequestCommandDone
    }

    fun checkStationFromExternalBd(station: GasStation){
        mExternalDb.saveToFireBase(station)
    }


}