package com.zekart.tracken.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zekart.tracken.model.entity.*

/**
 * Dao interface fot room database.
 * @see Room
 **/
@Dao
interface GasStationDao {

        /**
         * Create new gas station to data base.
         **/
        @Transaction
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun createGasStation(station: GasStation):Long?

        /**
         * Update current gas station to data base.
         **/
        @Transaction
        @Update
        fun updateGasStation(station: GasStation):Int?

        /**
         * Delete current gas station to data base.
         **/
        @Transaction
        @Delete
        fun deleteGasStation(gasStation: GasStation):Int?

        /**
         * Get gas station by ID.
         **/
        @Transaction
        @Query("SELECT * FROM gas_stations WHERE station_id = :id")
        fun getGasStationById(id: Long):GasStation

        /**
         * Create consume to user.
         **/
        @Transaction
        @Insert
        fun createConsume(consume: Consume):Long?

        /**
         * Take all gas station from data base
         **/
        @Transaction
        @Query("SELECT * FROM gas_stations")
        fun getAllGasStation():LiveData<List<GasStation>>

        /**
         * Take all consume which allow to current user
         **/
        @Transaction
        @Query("SELECT * FROM users")
        fun getAllConsume(): LiveData<List<ConsumeToUser>>

        /**
         * Update all gas station
         * Not usable
         **/
        @Transaction
        @Insert
        fun updateAllGasStationInDb(updatedList: List<GasStation>)

        /**
         * Get list of consume which allow to id gas station
         **/
        @Transaction
        @Query("select * from gas_stations inner join fuel_consume on fuel_consume.station_id = gas_stations.station_id where fuel_consume.user_id = :id")
        fun selectedConsumeByUser(id: Long):LiveData<List<GasStationToConsume>>

        /**
         * Get gas station by address
         **/
        @Transaction
        @Query("SELECT * FROM gas_stations WHERE station_id = :id")
        fun getStationAddressById(id: Long):GasStation

        /**
         * Get lists consume by user. Using from WorkManager
         **/
        @Transaction
        @Query("SELECT * FROM fuel_consume where user_id = :id")
        fun getConsumeForServer(id: Long): List<Consume>

        /**
         * Get lists gas station by user. Using from WorkManager
         **/
        @Transaction
        @Query("SELECT * FROM gas_stations")
        fun getGasStationForServer(): List<GasStation>

}