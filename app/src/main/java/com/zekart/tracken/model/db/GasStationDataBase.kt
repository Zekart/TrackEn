package com.zekart.tracken.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zekart.tracken.model.dao.GasStationDao
import com.zekart.tracken.model.dao.UserDao
import com.zekart.tracken.model.entity.Consume
import com.zekart.tracken.model.entity.GasStation
import com.zekart.tracken.model.entity.User


/**
 * Singleton for data base
 **/

@Database(entities = [User::class,GasStation::class, Consume::class],version = 1)
abstract class GasStationDataBase : RoomDatabase() {

    abstract fun stationDao():GasStationDao
    abstract fun userDao():UserDao

    companion object{
        @Volatile
        private var DB_INSTANCE:GasStationDataBase? = null

        fun getDatabase(context: Context):GasStationDataBase{
            val tempInstance = DB_INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GasStationDataBase::class.java,
                    "gas_station_database"
                )
                    .build()
                DB_INSTANCE = instance
                return instance
            }
        }
    }
}