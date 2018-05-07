package com.example.austi.feedme;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

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

public class Delivery extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        handler.postDelayed(new Runnable(){
            public void run(){
                getLastLoc();
                handler.postDelayed(this, delay);
            }
        }, delay);
        hasBeenMoved = false;
    }
    private FusedLocationProviderClient mFusedLocationClient;
    final int delay = 1000; //milliseconds
    final Handler handler = new Handler();
    public void getLastLoc(){
        Log.e("this","running");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("this", "missing permmissions");
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Log.e("this","running2");
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Log.e("here1","lng:" + location.getLongitude());
                            LatLng currPos = new LatLng(location.getLatitude(),location.getLongitude());
                            if(!hasBeenMoved){
                                mMap.clear();
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(currPos));
                                mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                                hasBeenMoved = true;
                                currentUserLocationMarker = mMap.addMarker(new MarkerOptions().position(currPos).title("You"));
                            }
                            currentUserLocationMarker.setPosition(currPos);
//                            mMap.addMarker(new MarkerOptions().position(new LatLng(51.1251,-114.0169)));
                        }
                    }
                });
    }
    public Marker currentUserLocationMarker;
    public static boolean hasBeenMoved = false;
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        hasBeenMoved = false;
    }
}
