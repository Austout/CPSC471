package com.example.austi.feedme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrentOrders extends AppCompatActivity {
    private List<Map<String, String>> crsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_orders);

        ListView listView = (ListView) findViewById(R.id.currOrdersList);
        String request        = "http://136.159.47.124/php/getMyOrdersHook.php/";
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
                    aug.put("name", columns[0]);
                    aug.put("time", columns[1].substring(11,columns[1].length()-3));
                    aug.put("image",columns[2]);
                    crsList.add(aug);
                }
            }
        }catch (Exception e){
            Log.e("error in thread" , "result: " + QueryThread.result);
            for(StackTraceElement element :e.getStackTrace()){
                Log.e("error in CurrentOrders:", element.toString());
            }
            e.printStackTrace();
        }
        String[] keys = {"image","name","time"};
        int[] widgetIds = {R.id.listViewImage,R.id.textView2, R.id.textView3};
        ImageListView crsAdapter = new ImageListView(this,R.layout.listview_layout,crsList, keys,widgetIds);
        listView.setAdapter(crsAdapter);

    }

}
