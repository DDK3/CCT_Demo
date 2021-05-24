package com.dipesh.cct_demo.adapter;

import com.dipesh.cct_demo.db.entity.LocationInfo;

public interface LocationInfoListAdapterListener {
    void onLocationInfoClick(LocationInfo locationInfo);
    void onLocationUpdateInfoClick(LocationInfo locationInfo);
}
