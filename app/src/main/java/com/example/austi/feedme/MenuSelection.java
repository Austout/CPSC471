package com.example.austi.feedme;

import android.app.Activity;
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

public class MenuSelection extends AppCompatActivity {
    private List<Map<String, String>> crsList;
    private AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            Map<String,String> currentMap = crsList.get(position);
            String orderID = currentMap.get("foodID");
            startDeliveryClass(orderID);
        }
    };
    public static final String orderIDKey = "MenuSelection.OrderID";
    public void startDeliveryClass(String orderID){
        Intent confirmOrder = new Intent(this,ConfirmOrder.class);
        confirmOrder.putExtra(orderIDKey,orderID);
        startActivityForResult(confirmOrder,2);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_selection);

        ListView listView = (ListView) findViewById(R.id.MenuSelectionList);
        listView.setOnItemClickListener(mMessageClickedHandler);
        String request        = "http://136.159.47.124/php/getMenuItemHook.php/";
        ArrayList<String> bodies = new ArrayList<String>();
        Intent intent = getIntent();
        bodies.add("vendorid");
        bodies.add(intent.getStringExtra(VendorSelection.VendorIDkey));
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
                    if(columns[0].length() > 12){
                        columns[0] = columns[0].substring(0,12) + "...";
                    }
                    aug.put("name", columns[0]);
                    aug.put("foodID",columns[1]);
                    aug.put("image",columns[2]);
                    Log.e("here","name: " + columns[0]);
                    crsList.add(aug);
                }
            }
        }catch (Exception e){
            Log.e("error in thread" , e.toString());
            e.printStackTrace();
        }
        String[] keys = {"image","name"};
        int[] widgetIds = {R.id.imageView3,R.id.vendorSelectTView};
        ImageListView crsAdapter = new ImageListView(this,R.layout.vendor_selection_listview_layout,crsList, keys,widgetIds);
        listView.setAdapter(crsAdapter);
    }
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        setResult(2,null);
        finish();
    }
    @Override
    public void finishFromChild(Activity child){
        finish();
    }

}
