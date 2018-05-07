package com.example.austi.feedme;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

import java.util.ArrayList;

public class DeliveryLockPage extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private LatLng orderPosition = null;
    private String orderID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_lock_page);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mMapFragmentLockPage);
        mapFragment.getMapAsync(this);
        Log.e("here","created");

        Intent currIntent = getIntent();
        orderID = currIntent.getStringExtra(DeliveryContainer.dContainerKey);
        Button doneOrderButton = ((Button) findViewById(R.id.doneOrder));
        doneOrderButton.setBackgroundColor(Color.parseColor("#a2ff5b"));
        doneOrderButton.setTextColor(Color.WHITE);

        handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                getLastLoc();
                handler.postDelayed(this, delay);
            }
        }, delay);
        hasBeenMoved = false;
    }
    final int delay = 1000; //milliseconds
    private static Handler handler = new Handler();
    public void getLastLoc(){
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
        // Got last known location. In some rare situations this can be null.
        if (LandingPage.currlocation != null) {
            LatLng currPos = LandingPage.currlocation;
            if(lastLoc == null || !(lastLoc.longitude == currPos.longitude && lastLoc.latitude == currPos.latitude)){
//                mMap.clear();
                lastLoc = currPos;
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currPos));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                hasBeenMoved = true;
                Log.e("loc","triggered");
                currentUserLocationMarker = mMap.addMarker(new MarkerOptions().position(currPos).title("You"));
            }
            if(currentUserLocationMarker != null){
                currentUserLocationMarker.setPosition(currPos);
            }
        }
    }
    public static LatLng lastLoc = null;
    @Override
    public void onResume(){
        hasBeenMoved = false;
        Log.e("resumed","here");
        super.onResume();
    }
    public Marker currentUserLocationMarker = null;
    public static boolean hasBeenMoved = true;
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    private Marker orderMarker = null;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        hasBeenMoved = false;
        Log.e("here","isFalse");
        if(lastLoc != null){
            mMap.clear();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(lastLoc));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
            Log.e("loc","triggered");
            currentUserLocationMarker = mMap.addMarker(new MarkerOptions().position(lastLoc).title("You"));
        }

        String request        = "http://136.159.47.124/php/getLatLngFromOrderIDHook.php/";
        ArrayList<String> bodies = new ArrayList<String>();
        bodies.add("orderid");
        bodies.add(orderID);
        Thread mThread = new QueryThread(request,bodies);
        mThread.start();
        try {
            mThread.join();
            Log.e("theseResults",QueryThread.result);
            String resultLatLng[] = QueryThread.result.split("<")[0].split(",");
            LatLng userLocation = new LatLng(Double.parseDouble(resultLatLng[0]), Double.parseDouble(resultLatLng[1]));
            orderMarker = mMap.addMarker(new MarkerOptions().position(userLocation).title("Order"));
        }catch (Exception e){
            Log.e("error in thread" , e.toString());
            e.printStackTrace();
        }
    }
    public void doneOrderOnClick(View v){

        String request        = "http://136.159.47.124/php/doneOrderHook.php/";
        ArrayList<String> bodies = new ArrayList<String>();
        bodies.add("orderid");
        bodies.add(orderID);
        Thread mThread = new QueryThread(request,bodies);
        mThread.start();
        try {
            mThread.join();
            Log.e("theseResults",QueryThread.result);
        }catch (Exception e){
            Log.e("error in thread" , e.toString());
            e.printStackTrace();
        }

        setResult(2,null);
        finish();
    }
}
