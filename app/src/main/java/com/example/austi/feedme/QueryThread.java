package com.example.austi.feedme;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by austi on 2017-12-01.
 */

public class QueryThread extends Thread {
    public static String result = "false";
    private String request;
    private ArrayList<String> bodies;
    public QueryThread(String request, ArrayList<String> bodies){
        this.bodies = bodies;
        this.request = request;
    }
    @Override
    public void run(){
        try {
            result = "false";
            java.net.URL url = new URL( request );
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);

            if(bodies.size() != 0 && bodies.size() %2 == 0){
                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                out.write((bodies.get(0) + "=" + bodies.get(1)).getBytes());
                for(int i = 2; i < bodies.size();i+=2){
                    out.write(("&"+bodies.get(i) + "=" + bodies.get(i+1)).getBytes());
                }
                out.flush();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
//            INSERT INTO `db_a2eaca_fmdb471`.`order` (`TotalCost`, `customer_id`, `delivery_loc`) VALUES ('13', '163', '56,123');
            br.close();
            String jsonString = sb.toString();
            urlConnection.disconnect();
            result = jsonString;
        }catch(Exception e){
            Log.e("error","registration: " + e.toString());
        }
    }
}
