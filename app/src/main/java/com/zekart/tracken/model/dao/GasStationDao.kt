package com.zekart.tracken.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zekart.tracken.model.entity.Consume
import com.zekart.tracken.model.entity.ConsumeToGasStation
import com.zekart.tracken.model.entity.GasStation

@Dao
interface GasStationDao {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insertGasStation(station: GasStation):Long

        @Update
        fun updateGasStation(station: GasStation)

        @Delete
        fun deleteGasStation(gasStation: GasStation)

        @Query("SELECT * FROM gas_stations WHERE station_id = :id")
        fun getGasStationById(id:Int):LiveData<GasStation>

        @Insert
        fun insertConsume(consume: Consume)
        //Get all station from db
        @Transaction
        @Query("SELECT * FROM gas_stations")
        fun getAllGasStation():LiveData<List<GasStation>>

        @Transaction
        @Query("SELECT * FROM gas_stations")
        fun getAllConsume(): LiveData<List<ConsumeToGasStation>>
}