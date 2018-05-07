package com.example.austi.feedme;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.net.URL;

/**
 * Created by austi on 2017-12-06.
 */

public class URLImageLoaderThread extends Thread{
    private String myURL;
    public URLImageLoaderThread(String myURL){
        this.myURL = myURL;
    }
    public static Bitmap bmp = null;
    @Override
    public void run(){
        try {
            URL url = new URL(myURL);
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        }catch (Exception e){
            Log.e("URLImageLoaderThread", "error loading image: " + e.toString());
        }
    }
}
