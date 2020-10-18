package com.zekart.tracken.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zekart.tracken.model.dao.UserDao
import com.zekart.tracken.model.entity.User

/**
 *
 * Repository to work wih station entity in Data Base
 *@param userDao
 * Dao for communicate with user table
 * @see UserDao
 *
 * Now user_id is 1
 */

class UserRepository(private val userDao: UserDao) {
    private lateinit var mCurrentUser:LiveData<User>
    private var newCreatedIDUser:MutableLiveData<Long> = MutableLiveData()

    fun createUser(userName:String){
        newCreatedIDUser.postValue(userDao.insertUser(User(userName)))
    }

    fun getNewCreatedUser():LiveData<Long>{
        return newCreatedIDUser
    }
}