package com.dipesh.cct_demo.db.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.dipesh.cct_demo.db.LocationInformationDatabase;
import com.dipesh.cct_demo.db.dao.LocationInfoDao;
import com.dipesh.cct_demo.db.entity.LocationInfo;

import java.util.List;

public class LocationInfoRepository {

        private LocationInfoDao mLocationInfDao;
        private LiveData<List<LocationInfo>> mAllLocationInfo;

    public LocationInfoRepository(Application application) {
        LocationInformationDatabase db = LocationInformationDatabase.getDatabase(application);
        mLocationInfDao = db.locationInfoDao();
        mAllLocationInfo = mLocationInfDao.getAllLocationInfo();
        }

        // Room executes all queries on a separate thread.
        // Observed LiveData will notify the observer when the data has changed.
        public LiveData<List<LocationInfo>> getAllLocationInfo() {
            return mAllLocationInfo;
        }


        public void insert(LocationInfo locationInfo) {
            LocationInformationDatabase.databaseWriteExecutor.execute(() -> {
                mLocationInfDao.insert(locationInfo);
            });
        }

    public void update(LocationInfo locationInfo) {
        LocationInformationDatabase.databaseWriteExecutor.execute(() -> {
            mLocationInfDao.updateLocationInfo(locationInfo);
        });
    }
}
