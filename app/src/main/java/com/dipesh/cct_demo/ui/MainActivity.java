 package com.dipesh.cct_demo.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dipesh.cct_demo.R;
import com.dipesh.cct_demo.service.LocationTimelineService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

 public class MainActivity extends AppCompatActivity {

     public static final int LOCATION_PERMISSION_CODE = 1;

     private SupportMapFragment mapFragment;
     private FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_frag);

        client = LocationServices.getFusedLocationProviderClient(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CODE);
            } else {
                startService();
            }
        } else {
            startService();
        }


    }



     public void startService() {

         Intent serviceIntent = new Intent(this, LocationTimelineService.class);
         startService(serviceIntent);

         final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

         if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
             buildAlertMessageNoGps();
         }
         
         geCurrentLocation();

     }

     private void geCurrentLocation() {

         @SuppressLint("MissingPermission")
         Task<Location> task = client.getLastLocation();

         task.addOnSuccessListener(new OnSuccessListener<Location>() {
             @Override
             public void onSuccess(Location location) {

                 if(location != null){
                     mapFragment.getMapAsync(new OnMapReadyCallback() {
                         @Override
                         public void onMapReady(GoogleMap googleMap) {

                             LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                             MarkerOptions options = new MarkerOptions().position(latLng);
                             googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                             googleMap.addMarker(options);
                         }
                     });
                 }

             }
         });
     }


     @Override
     public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
         super.onRequestPermissionsResult(requestCode, permissions, grantResults);
         if (requestCode == LOCATION_PERMISSION_CODE) {
             if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                 startService();
             } else {
                 Toast.makeText(this, "Give me permissions", Toast.LENGTH_LONG).show();
             }
         }
     }

     private void buildAlertMessageNoGps() {
         final AlertDialog.Builder builder = new AlertDialog.Builder(this);
         builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                 .setCancelable(false)
                 .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                     public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                         startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                     }
                 })
                 .setNegativeButton("No", new DialogInterface.OnClickListener() {
                     public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                         dialog.cancel();
                     }
                 });
         final AlertDialog alert = builder.create();
         alert.show();
     }


     public void seeLocationTimelineHistory(View view) {
         Intent intent = new Intent(this, LocationTimelineListActivity.class);
         startActivity(intent);
     }
 }