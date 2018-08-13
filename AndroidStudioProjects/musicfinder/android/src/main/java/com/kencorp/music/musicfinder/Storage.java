package com.kencorp.music.musicfinder;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

/**
 * Created by root on 05/06/18.
 */
//import org.apache.*;

/**
 * Created by root on 02/04/18.
 */

public class Storage {

    // nom de la preference partagé
    private String STORAGE="ZmusicSharedPreferences";

    private SharedPreferences preferences;

    private Context context;

    public Storage(Context context) {
        //this.context = context.getApplicationContext();
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
    }


    // store albumArt path
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void storeArt(String albumId, String path)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(albumId,path);
        editor.apply();
    }

    //get albumArt path

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public String getArt(String albumId)
    {
        return preferences.getString(albumId,null);

        // editor.apply();
    }


    // store albumArt path
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void storeSong(String StringSong)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("AllSong",StringSong);
        editor.apply();
    }



    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public String getSong()
    {
        return preferences.getString("AllSong",null);

        // editor.apply();
    }

    // store albumArt path
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void storeAlbum(String StringSong)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("AllAlbum",StringSong);
        editor.apply();
    }



    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public String getAlbum()
    {
        return preferences.getString("AllAlbum",null);

        // editor.apply();
    }
}

