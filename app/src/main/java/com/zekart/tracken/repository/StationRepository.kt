package com.zekart.tracken.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Transaction
import com.zekart.tracken.model.dao.GasStationDao
import com.zekart.tracken.model.entity.Consume
import com.zekart.tracken.model.entity.GasStation
import com.zekart.tracken.model.entity.GasStationToConsume
import com.zekart.tracken.model.pojo.StatisticResponse

class StationRepository(private val stationDao: GasStationDao){
    private var mutableStatistic = MutableLiveData<List<StatisticResponse>>()

    fun deleteStation(station: GasStation): Int? {
        return stationDao.deleteGasStation(station)
    }

    fun updateStation(station: GasStation): Int? {
        return stationDao.updateGasStation(station)
    }

    fun insertConsumeStation(consume: Consume): Long? {
        return stationDao.createConsume(consume)
    }

    fun getStationById(id: Long):GasStation{
        return stationDao.getGasStationById(id)
    }

    fun getAllStation():LiveData<List<GasStation>>{
        return stationDao.getAllGasStation()
    }

    fun getAllStationSize():Int?{
        return stationDao.getAllGasStationSize()
    }

    fun getAllConsumeSize():Int?{
        return stationDao.getAllGasConsumeSize()
    }

    fun insertAllStation(station:List<GasStation>){
        return stationDao.insertAllGasStation(station)
    }

    fun insertAllConsume(consume:List<Consume>){
        return stationDao.insertAllConsume(consume)
    }

    fun getAllConsumeTest(id:Long):LiveData<List<GasStationToConsume>>{
        return stationDao.selectedConsumeByUser(id)
    }

    fun createGasStation(station: GasStation): Long? {
        return stationDao.createGasStation(station)
    }


    @Transaction
    fun createGasStationWithConsume(station: GasStation, consume: Consume) {
        // Set concernId for new creating gas station
        val stationId = stationDao.createGasStation(station)
        // Set mStationId for new creating consume station
        consume.station_id = stationId
        stationDao.createConsume(consume)
    }

}