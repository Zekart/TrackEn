package com.zekart.tracken.services

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore
import com.zekart.tracken.model.db.GasStationDataBase
import com.zekart.tracken.model.entity.GasStation
import com.zekart.tracken.utils.LocalDataUtil

class SynchronizeToFirebase(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {

    @SuppressLint("LogNotTimber")
    override fun doWork(): Result {
        try {
            val req = uploadToDB()

            if (req){
                Log.d("test_synchro", "scheduling sync")
                Result.success()
            }else{
                Log.d("test_synchro", "scheduling repeat")
                Result.retry()
            }
           //return Result.success()
        }catch (e: Exception){
            Log.d("test_synchro", "${e.localizedMessage}")
            Result.failure()
        }
        return Result.success()
    }

    private fun uploadToDB():Boolean {
        try {
            var doneWork = false

            val dao = GasStationDataBase.getDatabase(applicationContext).stationDao()
            val fireDb = FirebaseFirestore.getInstance()

            val consumeList = dao.getConsumeForServer(LocalDataUtil.getUserID(applicationContext))
            val stationList = dao.getGasStationForServer()

            val consumeMap: MutableMap<String, Any> = HashMap()
            consumeMap["consume"] = consumeList

            val stationMap: MutableMap<String, Any> = HashMap()
            stationMap["stations"] = stationList

            fireDb.collection("db")
                .document("consume")
                .set(consumeMap)

            fireDb.collection("db")
                .document("station_list")
                .set(stationList)

            return doneWork
        }catch (e: IllegalStateException){
            return false
        }catch (np: NullPointerException){
            return false
        }
    }
}
