package com.zekart.tracken.model.dao

import androidx.room.*
import com.zekart.tracken.model.entity.ConcernGasStation

@Dao
interface GasStationDao {
        //Get all station from db
        @Transaction
        @Query("SELECT * FROM concern")
        fun getAllGasStation():List<ConcernGasStation>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insertGasStation()

        @Query("DELETE FROM gas_stations WHERE station_id = :id")
        fun deleteGasStation(id:Int);
}