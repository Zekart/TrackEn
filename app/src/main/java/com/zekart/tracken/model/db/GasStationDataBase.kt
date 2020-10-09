package com.zekart.tracken.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.zekart.tracken.model.dao.GasStationDao
import com.zekart.tracken.model.entity.Consume
import com.zekart.tracken.model.entity.GasStation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [GasStation::class, Consume::class],version = 1)
abstract class GasStationDataBase : RoomDatabase() {

    abstract fun stationDao():GasStationDao

    companion object{
        @Volatile
        private var DB_INSTANCE:GasStationDataBase? = null

        fun getDatabase(context: Context,scope: CoroutineScope):GasStationDataBase{
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
                    .addCallback(DbCallback(scope))
                    .build()
                DB_INSTANCE = instance
                return instance
            }
        }
    }

    private class DbCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            DB_INSTANCE?.let { database ->
                scope.launch {
                    println(database.queryExecutor)
                }
            }
        }
    }
}