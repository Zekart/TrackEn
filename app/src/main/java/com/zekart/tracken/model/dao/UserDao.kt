package com.zekart.tracken.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zekart.tracken.model.entity.GasStation
import com.zekart.tracken.model.entity.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User):Long?

    //TODO - Window with edit user isn`t implementing
    @Update
    fun updateUser(user: User):Int?

    @Query("SELECT * FROM users WHERE user_id = :id")
    fun getUserById(id:Long?):User
}