package com.example.austi.feedme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrentDeliverys extends AppCompatActivity {
    // Create a message handling object as an anonymous class.
    private AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            Map<String,String> currentMap = crsList.get(position);
            String orderID = currentMap.get("orderID");
            startDeliveryClass(orderID);
        }
    };
    public static final String deliveryRouteKey = "deliveryRoute.OrderID";
    public void startDeliveryClass(String orderID){
        Intent deliveryRoute = new Intent(this,DeliveryContainer.class);
        deliveryRoute.putExtra(deliveryRouteKey,orderID);
        startActivityForResult(deliveryRoute,2);
    }
    private List<Map<String, String>> crsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_deliverys);

        ListView listView = (ListView) findViewById(R.id.currDeleveryList);
        listView.setOnItemClickListener(mMessageClickedHandler);
        String request        = "http://136.159.47.124/php/getAvalibleDeliverysHook.php/";
        ArrayList<String> bodies = new ArrayList<String>();
        bodies.add("customerid");
        bodies.add(MainActivity.userID + "");
        Log.e("my Orders","userID: " + MainActivity.userID);
        Thread mThread = new QueryThread(request,bodies);
        mThread.start();
        try {
            mThread.join();
            Log.e("theseResults",QueryThread.result);
            if(!QueryThread.result.equals("false")){
                crsList = new ArrayList<Map<String,String>>();
                String foods[] = QueryThread.result.split("<");
                for(int i = 0; i < foods.length; i++){
                    String columns[] = foods[i].split(",");
                    Map<String, String> aug = new HashMap<String, String>();
                    if(columns[0].length() > 16){
                        columns[0] = columns[0].substring(0,12) + "...";
                    }
                    aug.put("foodname", columns[0]);
                    aug.put("studentname", columns[1]);
                    aug.put("orderID", columns[2]);
                    aug.put("image", columns[3]);
                    Log.e("here","name: " + columns[0]);
                    crsList.add(aug);
                }
            }
        }catch (Exception e){
            Log.e("error in thread" , e.toString());
            e.printStackTrace();
        }
        String[] keys = {"image","foodname","studentname","orderID"};
        int[] widgetIds = {R.id.listViewImage,R.id.textView2, R.id.textView3};
        ImageListView crsAdapter = new ImageListView(this,R.layout.listview_layout, crsList,keys,widgetIds);
        listView.setAdapter(crsAdapter);

    }
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if(resultCode == 2){
            finish();
        }
    }
}
