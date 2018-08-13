package com.kencorp.music.musicfinder;

/**
 * Created by root on 17/05/18.
 */

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;


public class MusicHelper {
    public static final int STORAGE_PERMISSION_CODE = 10;

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean hasExternalStorageAccess(Activity activity) {
        if(activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        activity.requestPermissions(new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                STORAGE_PERMISSION_CODE);

        return false;
    }

    public static boolean isAccessGranted(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == MusicHelper.STORAGE_PERMISSION_CODE) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                return true;
        }
        return false;
    }
}
