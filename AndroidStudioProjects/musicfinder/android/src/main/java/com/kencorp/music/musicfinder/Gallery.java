package com.kencorp.music.musicfinder;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Gallery {

    private static final String TAG = "music_finder";

    private ContentResolver mContentResolver;
    private List<String> images = new ArrayList<>();

    private Random mRandom = new Random();


    private List<String> pattern= new ArrayList<String>();


    Activity activity;
    Context context;

    Storage sharedpreferencesplugin;
    // Storage sharedPreferencesPlugin= new Storage();
    public Gallery(ContentResolver cr,Context context) {
        mContentResolver = cr;
        // new SharedPreferencesPlugin(activity.getApplicationContext()) ;
       // sharedpreferencesplugin=new Storage(context);
    }

    public void prepare() {
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        Cursor cur = mContentResolver.query(uri, null, null, null, null);

        if (cur == null) {
            return;
        }
        if (!cur.moveToFirst()) {
            return;
        }

       int data= cur.getColumnIndex(MediaStore.Images.Media.DATA);

        int id = cur.getColumnIndex(MediaStore.Images.Media._ID);


        do {

            String image = cur.getString(data);
           // int imageId = cur.getInt(id);
            images.add(image);

        }while (cur.moveToNext());
    }

    public ContentResolver getContentResolver() {
        return mContentResolver;
    }

    public String getImageList()
    {
        return images.toString();
    }
}
