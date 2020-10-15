package com.zekart.tracken.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.zekart.tracken.model.db.GasStationDataBase
import com.zekart.tracken.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel(application: Application): AndroidViewModel(application) {
    private val mDbRepository:UserRepository

    init {
        val dao = GasStationDataBase.getDatabase(application).userDao()
        mDbRepository = UserRepository(dao)
    }

    fun createUser(name:String)= viewModelScope.launch(Dispatchers.IO) {
        mDbRepository.createUser(name)
    }

    fun isCreatedUser():LiveData<Long>{
        return mDbRepository.getNewCreatedUser()
    }
}