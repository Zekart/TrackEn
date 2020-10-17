package com.zekart.tracken.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.zekart.tracken.model.db.GasStationDataBase
import com.zekart.tracken.model.entity.ConsumeToUser
import com.zekart.tracken.model.entity.GasStationToConsume
import com.zekart.tracken.model.pojo.ConsumeBundle
import com.zekart.tracken.model.pojo.StationBundle
import com.zekart.tracken.repository.FireBaseRepository
import com.zekart.tracken.repository.StationRepository
import com.zekart.tracken.repository.UserRepository
import com.zekart.tracken.utils.NetworkUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel(application: Application): AndroidViewModel(application) {

    private val mDbRepositoryStation:StationRepository
    private val mDbRepositoryUser:UserRepository

    private val mFirebaseRepository:FireBaseRepository
    private var connectionStatus:Boolean? = null
    private var mListStations:LiveData<Map<String, Any>?> = MutableLiveData<Map<String, Any>?>()
    private var mListConsume:LiveData<Map<String, Any>?> = MutableLiveData<Map<String, Any>?>()
    private var synchronizeIsFinished = MutableLiveData<Boolean>()

    private lateinit var temListStation:GasStationToConsume
    private lateinit var temListConsume:ConsumeToUser
    private val gson = Gson()

    init {
        val daoUser = GasStationDataBase.getDatabase(application).userDao()
        val daoStation = GasStationDataBase.getDatabase(application).stationDao()
        val firebase = FirebaseFirestore.getInstance()

        mDbRepositoryUser = UserRepository(daoUser)
        mDbRepositoryStation = StationRepository(daoStation)
        mFirebaseRepository = FireBaseRepository(firebase)

        checkConnection()
    }

    fun createUser(name: String)= viewModelScope.launch(Dispatchers.IO) {
        mDbRepositoryUser.createUser(name)
    }

    private fun getAllStationFromFirebase() {
        mListConsume = mFirebaseRepository.getDataConsume()
        mListStations = mFirebaseRepository.getDataStation()

        mFirebaseRepository.getStoredData()

        mListStations.observeForever {
            convertStationAndSaveToDB(it)
        }
        mListConsume.observeForever {
            convertConsumeAndSaveToDB(it)
        }
    }

    fun isCreatedUser():LiveData<Long>{
        return mDbRepositoryUser.getNewCreatedUser()
    }

    fun synchronizeIsFinished():LiveData<Boolean>{
        return synchronizeIsFinished
    }

    private fun  checkConnection(){
        connectionStatus = NetworkUtil.isOnline()
        if (connectionStatus!=null){
            if (!connectionStatus!!){
                synchronizeIsFinished.postValue(true)
            }else{
                getAllStationFromFirebase()
            }
        }else{
            synchronizeIsFinished.postValue(true)
        }
    }

    private fun convertConsumeAndSaveToDB(value: Map<String, Any>?){
        if (value!=null){
            try {
                val jsonFromFirebase = gson.toJson(value)
                val mConsume = gson.fromJson(jsonFromFirebase, ConsumeBundle::class.java)
                val sizeInDb = mDbRepositoryStation.getAllConsumeSize()

                if (sizeInDb!=null){
                    if (sizeInDb == 0){
                        mConsume.consume?.let { mDbRepositoryStation.insertAllConsume(it) }
                    }
                }

                println()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    private fun convertStationAndSaveToDB(value: Map<String, Any>?){
        if (value!=null){
            try {
                val p = gson.toJson(value)
                val mStation = gson.fromJson(p, StationBundle::class.java)
                val sizeInDb = mDbRepositoryStation.getAllStationSize()

                if (sizeInDb!=null){
                    if (sizeInDb == 0){
                        mStation.station_list?.let { mDbRepositoryStation.insertAllStation(it) }
                    }
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}