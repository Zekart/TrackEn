package com.zekart.tracken.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zekart.tracken.model.entity.ConcernGasStation
import com.zekart.tracken.model.entity.GasStation

@Dao
interface GasStationDao {
        //Get all station from db
        @Transaction
        @Query("SELECT * FROM concern")
        fun getAllGasStation():LiveData<List<ConcernGasStation>>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insertGasStation(station: GasStation)

        @Query("SELECT * FROM gas_stations WHERE station_id = :id")
        fun getStation(id:Int)

        @Query("DELETE FROM gas_stations WHERE station_id = :id")
        fun deleteGasStation(id:Int);
}