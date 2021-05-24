package com.dipesh.cct_demo.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.dipesh.cct_demo.db.entity.LocationInfo;
import com.dipesh.cct_demo.db.repository.LocationInfoRepository;
import com.dipesh.cct_demo.ui.MainActivity;
import com.dipesh.cct_demo.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.Calendar;

import static com.dipesh.cct_demo.base.App.CHANNEL_ID;

public class LocationTimelineService extends Service {
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    Location locationCurrent = new Location("Current");
    Location locationLast = new Location("Last");

    LocationInfoRepository locationInfoRepository;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);



        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);



                locationCurrent.setLatitude(locationResult.getLastLocation().getLatitude());
                locationCurrent.setLongitude(locationResult.getLastLocation().getLongitude());

                String input = "mylog:- "+calculateLocationDistance()+"  Lat is: " + locationResult.getLastLocation().getLatitude() + ", " + "Lng is: " + locationResult.getLastLocation().getLongitude();


//                calculateLocationDistance();

                Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setContentTitle("Location Service")
                        .setContentText(input)
                        .setSmallIcon(R.drawable.ic_android)
                        .setContentIntent(pendingIntent)
                        .setNotificationSilent()
                        .build();

                startForeground(1, notification);

            }
        };

        requestLocation();


//        String input = intent.getStringExtra("inputExtra");


        return START_STICKY;
    }

    private String calculateLocationDistance() {

        float distance = locationCurrent.distanceTo(locationLast);
        locationLast = locationCurrent;
        if(distance >= 5){
            LocationInfo mLocationInfo = new LocationInfo();
            mLocationInfo.setTimestamp(String.valueOf(Calendar.getInstance().getTimeInMillis()));
            mLocationInfo.setLocationNote("");
            mLocationInfo.setLatitude(locationCurrent.getLatitude());
            mLocationInfo.setLongitude(locationCurrent.getLongitude());
            // save data
            insertLocationInfo(mLocationInfo);
            Log.d("mylog", "Lat is: " + locationCurrent.getLatitude() + ", "
                    + "Lng is: " + locationCurrent.getLongitude());
        }
        return String.valueOf(distance);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("MissingPermission")
    private void requestLocation() {


        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    public void insertLocationInfo(LocationInfo locationInfo) {
        locationInfoRepository = new LocationInfoRepository(getApplication());
        locationInfoRepository.insert(locationInfo);
    }
}
