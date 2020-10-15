package com.zekart.tracken.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Transaction
import com.zekart.tracken.model.dao.FirebaseDaoImpl
import com.zekart.tracken.model.dao.GasStationDao
import com.zekart.tracken.model.entity.*
import com.zekart.tracken.model.pojo.StatisticResponse

class StationRepository(private val stationDao: GasStationDao){
    private var mRequestCommandDone = MutableLiveData<Int>()
    private var mExternalDb: FirebaseDaoImpl
    private var mutableStatistic = MutableLiveData<List<StatisticResponse>>()
    private var m:LiveData<List<Consume>>? =null

    init {
        mExternalDb = FirebaseDaoImpl()
        //stationDao.getAllGasStation()
    }

//    fun insertStation(station: GasStation){
//       stationDao.createGasStation(station)
//    }

    fun deleteStation(station: GasStation): Int? {
        return stationDao.deleteGasStation(station)
    }

    fun updateStation(station: GasStation){
        stationDao.updateGasStation(station)
    }

    fun insertConsumeStation(consume: Consume){
        stationDao.createConsume(consume)
    }

    fun getStationById(id: Long):LiveData<GasStation>{
        return stationDao.getGasStationById(id)
    }

    fun getAllStation():LiveData<List<GasStation>>{
        return stationDao.getAllGasStation()
    }

    fun getAllConsumeTest(id:Long):LiveData<List<GasStationToConsume>>{
        return stationDao.selectedConsumeByUser(id)
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


    fun getStatistic():LiveData<List<StatisticResponse>>{
        return mutableStatistic
    }

    fun createGasStation(station: GasStation) {
        stationDao.createGasStation(station)
    }

    @Transaction
    fun createGasStationWithConsume(station: GasStation, consume: Consume) {
        // Set concernId for new creating gas station
        val stationId = stationDao.createGasStation(station)
        // Set mStationId for new creating consume station
        consume.mStationId = stationId
        stationDao.createConsume(consume)
    }


}