package com.kencorp.music.musicfinder;

import android.annotation.TargetApi;
import android.app.Activity;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.os.Build;


public class AudioVisualizer {

    public static final AudioVisualizer instance = new AudioVisualizer();

    private static final String TAG = "AudioVisualizer";

    private Visualizer visualizer;

    public boolean isActive() {
        return visualizer != null;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void activate(Visualizer.OnDataCaptureListener listener) {


        visualizer = new Visualizer(MusicfinderPlugin.playerId);

        visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        visualizer.setDataCaptureListener(
                listener,
                Visualizer.getMaxCaptureRate() / 2,
                false,
                true
        );
        visualizer.setEnabled(true);


        Equalizer equalizer = new Equalizer(0, MusicfinderPlugin.playerId);
        equalizer.setEnabled(true);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void deactivate()
    {
        visualizer.release();
        visualizer = null;
    }

}
