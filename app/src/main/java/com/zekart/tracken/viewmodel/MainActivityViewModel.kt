package com.zekart.tracken.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zekart.tracken.model.db.GasStationDataBase
import com.zekart.tracken.model.entity.User
import com.zekart.tracken.repository.UserRepository
import com.zekart.tracken.utils.LocalDataUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel(application: Application): AndroidViewModel(application) {
    private val mDbRepository:UserRepository
    private var mUserId = MutableLiveData<Long>()

    init {
        val dao = GasStationDataBase.getDatabase(application, viewModelScope).userDao()
        mUserId.value = LocalDataUtil.getUserID(application)
        mDbRepository = UserRepository(dao)
    }

    fun getUser()= viewModelScope.launch(Dispatchers.IO) {
        mUserId.value?.let { mDbRepository.getUser(it) }
    }

    fun createUser(name:String)= viewModelScope.launch(Dispatchers.IO) {
        mDbRepository.createUser(name)
    }

    fun checkUser():LiveData<User>{
        return mDbRepository.getCurrentUser()
    }


    fun isCreatedUser():LiveData<Long>{
        return mDbRepository.getNewCreatedUser()
    }


}