package com.example.austi.feedme;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import static com.example.austi.feedme.DeliveryContainer.dContainerKey;

public class LandingPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        Button delevery = (Button) findViewById(R.id.makeDelivery);
        Button placeOrder = (Button) findViewById(R.id.placeOrder);
        Button help = (Button) findViewById(R.id.help);
        Button settings = (Button) findViewById(R.id.settings);
        Button currentOrders = (Button) findViewById(R.id.currentOrders);


        Button b8 = (Button) findViewById(R.id.button19);
        Button b9 = (Button) findViewById(R.id.button21);
        Button b10 = (Button) findViewById(R.id.button22);
        Button b11 = (Button) findViewById(R.id.button23);

        delevery.setBackgroundColor(Color.parseColor("#CC001E"));
        delevery.setTextColor(Color.WHITE);
        delevery.setTypeface(FontManager.getTypeface(this,FontManager.FONTAWESOME));

        placeOrder.setBackgroundColor(Color.parseColor("#CC001E"));
        placeOrder.setTextColor(Color.WHITE);
        placeOrder.setTypeface(FontManager.getTypeface(this,FontManager.FONTAWESOME));

        help.setBackgroundColor(Color.parseColor("#000000"));
        help.setTextColor(Color.WHITE);
        help.setTypeface(FontManager.getTypeface(this,FontManager.FONTAWESOME));

        settings.setBackgroundColor(Color.parseColor("#000000"));
        settings.setTextColor(Color.WHITE);
        settings.setTypeface(FontManager.getTypeface(this,FontManager.FONTAWESOME));

        currentOrders.setBackgroundColor(Color.parseColor("#000000"));
        currentOrders.setTextColor(Color.WHITE);
        currentOrders.setTypeface(FontManager.getTypeface(this,FontManager.FONTAWESOME));

        b8.setBackgroundColor(Color.parseColor("#FFFFFF"));
        b8.setTextColor(Color.BLACK);
        b8.setTypeface(FontManager.getTypeface(this,FontManager.FONTAWESOME));

        b9.setBackgroundColor(Color.parseColor("#FFFFFF"));
        b9.setTextColor(Color.BLACK);
        b9.setTypeface(FontManager.getTypeface(this,FontManager.FONTAWESOME));

        b10.setBackgroundColor(Color.parseColor("#FFFFFF"));
        b10.setTextColor(Color.BLACK);
        b10.setTypeface(FontManager.getTypeface(this,FontManager.FONTAWESOME));

        b11.setBackgroundColor(Color.parseColor("#FFFFFF"));
        b11.setTextColor(Color.BLACK);
        b11.setTypeface(FontManager.getTypeface(this,FontManager.FONTAWESOME));

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                getLastLoc();
                handler.postDelayed(this, delay);
            }
        }, delay);

        doesDeliveryExist();

    }
    public void doesDeliveryExist(){
        String request        = "http://136.159.47.124/php/doesDeliveryExistHook.php/";
        ArrayList<String> bodies = new ArrayList<String>();
        bodies.add("delivererid");
        bodies.add(MainActivity.userID + "");
        Thread mThread = new QueryThread(request,bodies);
        mThread.start();
        try {
            mThread.join();
            Log.e("theseResults",QueryThread.result);
            if(!QueryThread.result.contains("false")){
                String orderID = QueryThread.result;
                Intent startDeliveryLock = new Intent(this,DeliveryLockPage.class);
                startDeliveryLock.putExtra(dContainerKey,orderID);
                startActivityForResult(startDeliveryLock,2);
            }
        }catch (Exception e){
            Log.e("error in thread" , e.toString());
            e.printStackTrace();
        }
    }
    @Override
    public void onResume(){
        doesDeliveryExist();
        super.onResume();
    }
    private FusedLocationProviderClient mFusedLocationClient;
    final int delay = 1000; //milliseconds
    private static Handler handler = new Handler();
    public static LatLng currlocation = null;
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
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            currlocation = new LatLng(location.getLatitude(),location.getLongitude());
                        }
                    }
                });
    }

    public void userSettingsOnclick(View view){
        Intent userSettingsIntent = new Intent(this,UserSettings.class);
        startActivity(userSettingsIntent);
    }
    public void currentDeliveries(View v){
        Intent currDeliveries = new Intent(this, CurrentDeliverys.class);
        startActivity(currDeliveries);
    }
    public void currentOrdersOnClick(View v){
        Intent currOrders = new Intent(this, CurrentOrders.class);
        startActivity(currOrders);
    }
    public  void vendorSelectionOnClick(View v){
        Intent vendorSelectionIntent = new Intent(this, VendorSelection.class);
        startActivity(vendorSelectionIntent);
    }
    public void helpOnClick(View v){
        Intent helpIntent = new Intent(this, FAQ.class);
        startActivity(helpIntent);
    }
}
