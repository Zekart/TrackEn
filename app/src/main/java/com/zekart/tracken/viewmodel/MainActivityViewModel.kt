package com.zekart.tracken.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.zekart.tracken.model.db.GasStationDataBase
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
    private var synchronizeIsFinishedConsume = MutableLiveData<Boolean>()
    private var synchronizeIsFinishedStation = MutableLiveData<Boolean>()
    private var synchronizeErrorConsume = MutableLiveData<Boolean>()
    private var synchronizeErrorStation = MutableLiveData<Boolean>()
    private var userId:Long? = null

    private val gson = Gson()

    init {
        val daoUser = GasStationDataBase.getDatabase(application).userDao()
        val daoStation = GasStationDataBase.getDatabase(application).stationDao()
        val firebase = FirebaseFirestore.getInstance()

        mDbRepositoryUser = UserRepository(daoUser)
        mDbRepositoryStation = StationRepository(daoStation)
        mFirebaseRepository = FireBaseRepository(firebase)
    }

    fun createUser(name: String)= viewModelScope.launch(Dispatchers.IO) {
        mDbRepositoryUser.createUser(name)
    }

    private fun getAllStationFromFirebase() {
        mListConsume = mFirebaseRepository.getDataConsume()
        mListStations = mFirebaseRepository.getDataStation()

        mListStations.observeForever {
            convertStationAndSaveToDB(it)
        }
        mListConsume.observeForever {
            convertConsumeAndSaveToDB(it)
        }
    }

    fun setUserId(id:Long){
        this.userId = id
    }

    fun isCreatedUser():LiveData<Long>{
        return mDbRepositoryUser.getNewCreatedUser()
    }

    fun synchronizeIsErrorConsume():LiveData<Boolean>{
        return synchronizeErrorConsume
    }

    fun synchronizeIsErrorStation():LiveData<Boolean>{
        return synchronizeErrorStation
    }

    fun  checkConnection(){
        if (synchronizeIsFinishedStation.value == true && synchronizeIsFinishedStation.value == true){
            return
        }

        connectionStatus = NetworkUtil.isOnline()
        if (connectionStatus!=null){
            if (connectionStatus!!){
                if (userId!=null){
                    mFirebaseRepository.getStoredData(userId!!)
                    getAllStationFromFirebase()
                }
            }
        }
    }

    private fun convertConsumeAndSaveToDB(value: Map<String, Any>?)= viewModelScope.launch(Dispatchers.IO) {
        if (value!=null){
            try {
                val jsonFromFirebase = gson.toJson(value)
                val mConsume = gson.fromJson(jsonFromFirebase, ConsumeBundle::class.java)
                val sizeInDb = mDbRepositoryStation.getAllConsumeSize()

                if (sizeInDb!=null){
                    if (sizeInDb == 0){
                        val done = mConsume.consume?.let { mDbRepositoryStation.insertAllConsume(it) }
                        if (done!=null && done.isNotEmpty()){
                            synchronizeIsFinishedConsume.postValue(true)
                        }else{
                            synchronizeErrorConsume.postValue(true)
                        }
                    }else{
                        synchronizeIsFinishedConsume.postValue(true)
                    }
                }
            }catch (e: Exception){
                synchronizeErrorConsume.postValue(true)
                e.printStackTrace()
            }
        }
    }

    private fun convertStationAndSaveToDB(value: Map<String, Any>?)= viewModelScope.launch(Dispatchers.IO) {
        if (value!=null){
            try {
                val p = gson.toJson(value)
                val mStation = gson.fromJson(p, StationBundle::class.java)
                val sizeInDb = mDbRepositoryStation.getAllStationSize()

                if (sizeInDb!=null){
                    if (sizeInDb == 0){
                        val done = mStation.station_list?.let { mDbRepositoryStation.insertAllStation(it) }
                        if (done!=null && done.isNotEmpty()){
                            synchronizeIsFinishedStation.postValue(true)
                        }else{
                            synchronizeErrorStation.postValue(true)
                        }
                    }else{
                        synchronizeIsFinishedStation.postValue(true)
                    }
                }
            }catch (e: Exception){
                synchronizeErrorStation.postValue(true)
                e.printStackTrace()
            }
        }
    }
}