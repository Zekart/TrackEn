package com.zekart.tracken.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zekart.tracken.model.db.ConsumeToUserConverter
import com.zekart.tracken.model.entity.*

@Dao
interface GasStationDao {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun createGasStation(station: GasStation):Long?

        @Update
        fun updateGasStation(station: GasStation):Int?

        @Delete
        fun deleteGasStation(gasStation: GasStation):Int?

        @Transaction
        @Query("SELECT * FROM gas_stations WHERE station_id = :id")
        fun getGasStationById(id: Long):LiveData<GasStation>

        @Insert
        fun createConsume(consume: Consume):Long?
        //Get all station from db
        @Transaction
        @Query("SELECT * FROM gas_stations")
        fun getAllGasStation():LiveData<List<GasStation>>

        @Transaction
        @Query("SELECT * FROM users")
        fun getAllConsume(): LiveData<List<ConsumeToUser>>

        @Transaction
        @Insert
        fun updateAllGasStationInDb(updatedList: List<GasStation>)

        @Transaction
        @Query("select * from gas_stations inner join fuel_consume on fuel_consume.station_id = gas_stations.station_id where fuel_consume.user_id = :id")
        //@Query("SELECT * FROM gas_stations WHERE user_id = :id")
        fun selectedConsumeByUser(id: Long):LiveData<List<GasStationToConsume>>

        @Transaction
        @Query("SELECT * FROM gas_stations WHERE station_id = :id")
        fun getStationAddressById(id: Long):GasStation

        @Transaction
        @Query("SELECT * FROM fuel_consume where user_id = :id")
        fun getConsumeForServer(id: Long): List<Consume>

        @Transaction
        @Query("SELECT * FROM gas_stations")
        fun getGasStationForServer(): List<GasStation>

}