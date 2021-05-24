package com.dipesh.cct_demo.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dipesh.cct_demo.db.entity.LocationInfo;
import com.dipesh.cct_demo.db.repository.LocationInfoRepository;

import java.util.List;

public class LocationInfoTimelineViewModel extends AndroidViewModel {

    private LocationInfoRepository mLocationInfoRepository;

    private LiveData<List<LocationInfo>> mListLiveData;

    public LocationInfoTimelineViewModel(Application application) {
        super(application);
        mLocationInfoRepository = new LocationInfoRepository(application);
        mListLiveData = mLocationInfoRepository.getAllLocationInfo();
    }

    LiveData<List<LocationInfo>> getAllLocationList() {
        return mListLiveData;
    }

}
