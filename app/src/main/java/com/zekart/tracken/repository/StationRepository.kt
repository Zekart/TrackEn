package com.zekart.tracken.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zekart.tracken.model.dao.GasStationDao
import com.zekart.tracken.model.entity.Consume
import com.zekart.tracken.model.entity.ConsumeToGasStation
import com.zekart.tracken.model.entity.GasStation

class StationRepository(private val stationDao: GasStationDao){
    private var newIdInserted:Long? = -1
    init {
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
        newIdInserted = stationDao.insertGasStation(station)
        if (newIdInserted != null || newIdInserted!! > -1){
            insertNewConsume(consume)
        }
    }

    private fun insertNewConsume(consume: Consume){
        stationDao.insertConsume(consume)
    }

    fun getStationById(id:Int):LiveData<GasStation>{
        return stationDao.getGasStationById(id)
    }

    fun getAllStation():LiveData<List<GasStation>>{
        return stationDao.getAllGasStation()
    }

    fun getAllConsume():LiveData<List<ConsumeToGasStation>>{
       return stationDao.getAllConsume()
    }
}