package com.zekart.tracken.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.zekart.tracken.model.entity.Station;

import java.util.ArrayList;

@Dao
public interface StationDao {

    //Insert gas station to db table
    @Insert
    void insertStation(Station station);

    //Update gas station
    @Update
    void updateStation(Station station);

    //Delete gas station
    @Delete
    void deleteStation(Station station);

    //Update all elements in db
    void updateAllElement(ArrayList<Station> stationArrayList);
}
