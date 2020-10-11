package com.zekart.tracken.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zekart.tracken.model.entity.Consume
import com.zekart.tracken.model.entity.ConsumeToGasStation
import com.zekart.tracken.model.entity.GasStation

@Dao
interface GasStationDao {
        //Get all station from db
        @Transaction
        @Query("SELECT * FROM gas_stations")
        fun getAllGasStation():LiveData<List<GasStation>>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insertGasStation(station: GasStation):Long

        @Query("SELECT * FROM gas_stations WHERE station_id = :id")
        fun getStation(id:Int):LiveData<GasStation>

        @Update
        fun updateStation(station: GasStation)

        @Delete
        fun deleteGasStation(gasStation: GasStation)

        @Insert
        fun insertConsume(consume: Consume)

        @Transaction
        @Query("SELECT * FROM gas_stations")
        fun getAllConsume(): LiveData<List<ConsumeToGasStation>>
}