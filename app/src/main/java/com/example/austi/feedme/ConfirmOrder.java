package com.example.austi.feedme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import android.net.Uri;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConfirmOrder extends AppCompatActivity {
    String itemPrice = "";
    String foodID = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        Button acceptOrder = (Button) findViewById(R.id.AcceptOrder);
        Button declineOrder = (Button) findViewById(R.id.DenyOrder);

        TextView foodName = (TextView) findViewById(R.id.FoodName);
        TextView foodPrice = (TextView) findViewById(R.id.FoodPrice);
        ImageView foodImage = (ImageView) findViewById(R.id.imageView2);

        String request        = "http://136.159.47.124/php/getFoodItemByID.php/";
        ArrayList<String> bodies = new ArrayList<String>();
        Intent intent = getIntent();
        bodies.add("foodid");
        foodID = intent.getStringExtra(MenuSelection.orderIDKey);
        bodies.add(foodID);
        Thread mThread = new QueryThread(request,bodies);
        mThread.start();
        try {
            mThread.join();
            Log.e("theseResults",QueryThread.result);
            String fields[] = QueryThread.result.split("<")[0].split(",");
            Thread urlLoader = new URLImageLoaderThread(fields[2]);
            urlLoader.start();
            foodName.setText(fields[0]);
            foodPrice.setText("$" + fields[1]);
            itemPrice = fields[1];
            urlLoader.join();
            foodImage.setImageBitmap(URLImageLoaderThread.bmp);

        }catch (Exception e){
            Log.e("error in thread" , e.toString());
            e.printStackTrace();
        }

        acceptOrder.setBackgroundColor(Color.parseColor("#a2ff5b"));
        acceptOrder.setTextColor(Color.WHITE);
        acceptOrder.setTypeface(FontManager.getTypeface(this,FontManager.FONTAWESOME));

        declineOrder.setBackgroundColor(Color.parseColor("#CC001E"));
        declineOrder.setTextColor(Color.WHITE);
        declineOrder.setTypeface(FontManager.getTypeface(this,FontManager.FONTAWESOME));
    }

    public void AcceptOrderOnclick(View v){

        ArrayList<String> bodies = new ArrayList<String>();
        String request        = "http://136.159.47.124/php/createOrderHook.php/";
        bodies.add("price");
        bodies.add(itemPrice);
        bodies.add("customerid");
        bodies.add(MainActivity.userID + "");
        bodies.add("location");
        bodies.add(LandingPage.currlocation.latitude + "," + LandingPage.currlocation.longitude);
        bodies.add("foodid");
        Log.e("confirmOrder","foodID: " +foodID);
        bodies.add(foodID);
        bodies.add("food_id");
        bodies.add(foodID);
        Thread mThread = new QueryThread(request,bodies);
        mThread.start();
        try {
            mThread.join();
            Log.e("theseResults",QueryThread.result);
            if(!QueryThread.result.contains("false")){ //TODO
                Intent startLandingPage = new Intent(this,LandingPage.class);
                startActivity(startLandingPage);
            }else{
//                Toast.makeText(getApplicationContext(),"Invalid Email/Password",Toast.LENGTH_LONG);
            }
        }catch (Exception e){
            Log.e("error in thread" , e.toString());
            e.printStackTrace();
        }



        setResult(2,null);
        finish();
    }
    public void DenyOrderOnclick(View v){
        setResult(2,null);
        finish();
    }
}
