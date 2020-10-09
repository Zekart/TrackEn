package com.zekart.tracken.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zekart.tracken.model.entity.GasStation

@Dao
interface GasStationDao {
        //Get all station from db
        @Transaction
        @Query("SELECT * FROM gas_stations")
        fun getAllGasStation():LiveData<List<GasStation>>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insertGasStation(station: GasStation)

        @Query("SELECT * FROM gas_stations WHERE station_id = :id")
        fun getStation(id:Int):LiveData<GasStation>

//        @Query("SELECT * FROM gas_stations WHERE station_id = :station")
//        fun updateStationn(station: GasStation)

        @Query("DELETE FROM gas_stations WHERE station_id = :id")
        fun deleteGasStation(id:Int);
}