package com.example.austi.feedme;

import android.content.ClipData;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.austi.feedme.R;
import com.example.austi.feedme.URLImageLoaderThread;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by austi on 2017-12-11.
 */

public class ImageListView extends ArrayAdapter<Map<String,String>> {
    int resource = R.layout.listview_layout;
    private String[] keys;
    private int[] values;
    public ImageListView(Context context, int resource, List<Map<String,String>> items, String[] keys, int[] values) {
        super(context, resource, items);
        this.resource = resource;
        this.keys = keys;
        this.values = values;
        Log.e("imageListView", "init");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e("imageListView", "here");
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(resource, null);
        }
        Log.e("imageListView", "pre item");
        Map<String,String> p = getItem(position);
        Log.e("imageListView", "got item");
        if (p != null) {
            ImageView imageView = (ImageView) v.findViewById(values[0]);
            Thread urlLoader = new URLImageLoaderThread(p.get(keys[0]));
            urlLoader.start();
            Log.e("imageListView", "starting multiThread");
            try {
                urlLoader.join();
                imageView.setImageBitmap(URLImageLoaderThread.bmp);
            }catch (Exception e){

            }
            for(int i = 1; i < values.length;i++){
                TextView tv = (TextView) v.findViewById(values[i]);
                if(tv != null){
                    tv.setText(p.get(keys[i]));
                }
            }
        }

        return v;
    }

}
