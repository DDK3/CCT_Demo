package com.dipesh.cct_demo.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.dipesh.cct_demo.R;
import com.dipesh.cct_demo.adapter.LocationInfoListAdapter;
import com.dipesh.cct_demo.adapter.LocationInfoListAdapterListener;
import com.dipesh.cct_demo.db.entity.LocationInfo;
import com.dipesh.cct_demo.db.repository.LocationInfoRepository;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Collections;
import java.util.List;

public class LocationTimelineListActivity extends AppCompatActivity implements LocationInfoListAdapterListener {

    private LocationInfoTimelineViewModel mLocationInfoTimelineViewModel;
    private SupportMapFragment mapFragment;
    private LocationInfoRepository locationInfoRepository;
    private Dialog noteAddDialog = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_timeline_list);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_frag_sec);

        mLocationInfoTimelineViewModel = new ViewModelProvider(this).get(LocationInfoTimelineViewModel.class);
        observeNotificationData();
    }

    private void observeNotificationData(){
        mLocationInfoTimelineViewModel.getAllLocationList().observe(this, new Observer<List<LocationInfo>>() {
            @Override
            public void onChanged(List<LocationInfo> locationInfos) {
                if(locationInfos != null && locationInfos.size() > 0){
                    Collections.reverse(locationInfos);
                    setUpNotificationRecycler(locationInfos);

                }
            }
        });
    }

    private void setUpNotificationRecycler(List<LocationInfo> locationInfoList){

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LocationInfoListAdapter adapter = new LocationInfoListAdapter(LocationTimelineListActivity.this,locationInfoList,this);
        recyclerView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();

    }

    @Override
    public void onLocationInfoClick(LocationInfo locationInfo) {
        LocationInfo mlocation;
        mlocation = locationInfo;
        if(mlocation != null){
            LocationInfo finalMlocation = mlocation;
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {

                    LatLng latLng = new LatLng(finalMlocation.getLatitude(), finalMlocation.getLongitude());
                    MarkerOptions options = new MarkerOptions().title(locationInfo.getLocationNote()).position(latLng);
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                    googleMap.addMarker(options);
                }
            });
        }
    }

    @Override
    public void onLocationUpdateInfoClick(LocationInfo locationInfo) {

        updateNote(locationInfo);
    }

    public void updateLocationInfo(LocationInfo locationInfo) {
        locationInfoRepository = new LocationInfoRepository(getApplication());
        locationInfoRepository.update(locationInfo);

        observeNotificationData();
    }

    private void updateNote(LocationInfo locationInfo){


       if(noteAddDialog == null || !noteAddDialog.isShowing()) {
        noteAddDialog = new Dialog(LocationTimelineListActivity.this);
        noteAddDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        noteAddDialog.setCancelable(true);
        noteAddDialog.setContentView(R.layout.update_note_dialog);
        Button dialogButton = noteAddDialog.findViewById(R.id.add_note);
        EditText dialogEditText = noteAddDialog.findViewById(R.id.text_note);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appliance = dialogEditText.getText().toString();
                if(!TextUtils.isEmpty(appliance)) {
                    LocationInfo mLocationInfo = new LocationInfo();
                    mLocationInfo.setTimestamp(locationInfo.getTimestamp());
                    mLocationInfo.setLocationNote("");
                    mLocationInfo.setLatitude(locationInfo.getLatitude());
                    mLocationInfo.setLongitude(locationInfo.getLongitude());
                    // save data
                    updateLocationInfo(mLocationInfo);
                    noteAddDialog.dismiss();
                }else{
                    dialogEditText.setError("Please input at least one letter.");
                }
            }
        });
        noteAddDialog.show();
    }
    }
}