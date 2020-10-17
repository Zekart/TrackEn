package com.zekart.tracken.services

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore
import com.zekart.tracken.model.db.GasStationDataBase
import com.zekart.tracken.utils.DataAppUtil

class SynchronizeToFirebase(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {

    @SuppressLint("LogNotTimber")
    override fun doWork(): Result {
        try {
            val req = uploadToFirebaseDB()
            if (req){
                Result.success()
            }else{
                Result.retry()
            }
        }catch (e: Exception){
            Result.failure()
        }
        return Result.success()
    }

    private fun uploadToFirebaseDB():Boolean {
        try {
            var doneWork = false

            val dao = GasStationDataBase.getDatabase(applicationContext).stationDao()
            val fireDb = FirebaseFirestore.getInstance()

            val consumeList = dao.getConsumeForServer(DataAppUtil.getUserID(applicationContext))
            val stationList = dao.getGasStationForServer()

            val consumeMap: MutableMap<String, Any> = HashMap()
            consumeMap["consume"] = consumeList

            val stationMap: MutableMap<String, Any> = HashMap()
            stationMap["stations"] = stationList

            fireDb.collection("db")
                .document("consume")
                .set(consumeMap).addOnCompleteListener {
                    doneWork = true
                }

            fireDb.collection("db")
                .document("station_list")
                .update(stationMap).addOnCompleteListener {
                    doneWork = true
                }

            return doneWork
        }catch (e: IllegalStateException){
            Log.d("test", "getLatency: EXCEPTION");
            return false
        }catch (np: NullPointerException){
            Log.d("test", "getLatency: EXCEPTION");
            return false
        }
    }
}
