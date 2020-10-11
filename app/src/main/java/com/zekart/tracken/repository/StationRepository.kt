package com.zekart.tracken.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zekart.tracken.model.dao.GasStationDao
import com.zekart.tracken.model.entity.Consume
import com.zekart.tracken.model.entity.ConsumeToGasStation
import com.zekart.tracken.model.entity.GasStation

class StationRepository(private val stationDao: GasStationDao){
    private var mAllGasStation: LiveData<List<GasStation>>
    private lateinit var mGasStation: LiveData<GasStation>
    private var newIdInserted:Long = -1


    init {
        mAllGasStation = stationDao.getAllGasStation()
    }

    fun insertStation(station: GasStation){
        stationDao.insertGasStation(station)
    }

    fun insertConsumeStation(consume: Consume){
        stationDao.insertConsume(consume)
    }

    fun insertNewStationWithConsume(station: GasStation, consume: Consume){
        newIdInserted = stationDao.insertGasStation(station)
        if (newIdInserted > -1){
            insertNewConsume(consume)
        }
    }

    private fun insertNewConsume(consume: Consume){
        consume.mStationId = newIdInserted.toInt()
        stationDao.insertConsume(consume)
    }

    fun deleteStation(){
        mGasStation.value?.let { stationDao.deleteGasStation(it) }
    }

    fun updateCurrentStation(station: GasStation){
        stationDao.updateStation(station)
    }

    fun getStationById(id:Int){
        this.mGasStation = stationDao.getStation(id)
    }

    fun getCurrentStation():LiveData<GasStation>{
        return this.mGasStation
    }

    fun getAllStation():LiveData<List<GasStation>>{
        return stationDao.getAllGasStation()
    }

    fun getAllConsume():LiveData<List<ConsumeToGasStation>>{
       return stationDao.getAllConsume()
    }
}