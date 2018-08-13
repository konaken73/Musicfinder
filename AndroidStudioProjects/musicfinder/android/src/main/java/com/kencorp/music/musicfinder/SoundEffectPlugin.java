package com.kencorp.music.musicfinder;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

/**
 * Created by root on 02/08/18.
 */

public class SoundEffectPlugin implements MethodChannel.MethodCallHandler{

    private SoundEffect soundEffect = new SoundEffect();

    @Override
    public void onMethodCall(MethodCall methodCall, MethodChannel.Result result) {

    }
}







