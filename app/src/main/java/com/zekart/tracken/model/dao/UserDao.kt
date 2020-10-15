package com.zekart.tracken.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zekart.tracken.model.entity.User

/**
 * User dao. Implement only insert to data base
 **/

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User):Long?

    //TODO - Window with edit user isn`t implementing

    @Query("SELECT * FROM users WHERE user_id = :id")
    fun getUserById(id:Long?):LiveData<User>
}