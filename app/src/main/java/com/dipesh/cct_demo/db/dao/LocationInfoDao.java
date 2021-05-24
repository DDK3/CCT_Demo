package com.dipesh.cct_demo.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.dipesh.cct_demo.db.entity.LocationInfo;

import java.util.List;

@Dao
public interface LocationInfoDao {

    @Insert
    public void insertLocationInfo(LocationInfo locationInfo);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(LocationInfo... locationInfo);

    @Query("SELECT * FROM location_info ORDER BY timestamp ASC")
    LiveData<List<LocationInfo>> getAllLocationInfo();

    @Update
    public void updateLocationInfo(LocationInfo locationInfo);

}
