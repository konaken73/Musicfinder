package com.kencorp.music.musicfinder;

import android.annotation.TargetApi;
import android.media.audiofx.Visualizer;
import android.os.Build;
import android.util.Log;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

import static android.content.ContentValues.TAG;


public  class AudioVisualizerPlugin implements MethodChannel.MethodCallHandler {

    private AudioVisualizer visualizer = new AudioVisualizer();



    private static final Pattern VISUALIZER_METHOD_NAME_MATCH = Pattern.compile("");

    private static MethodChannel visualizerChannel;


    public static void setNameMethodChannel(MethodChannel channel2)
    {

        visualizerChannel = channel2;
        visualizerChannel.setMethodCallHandler(new AudioVisualizerPlugin());
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onMethodCall(MethodCall call, MethodChannel.Result result) {




        try {

            switch (call.method) {
                case "activate_visualizer":





                    if (visualizer.isActive()) {
                        return;
                    }
                visualizer.activate(new Visualizer.OnDataCaptureListener() {

                        @Override
                        public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {

                            Map<String, Object> args = new HashMap<>();
                            args.put("waveform", waveform);


                            visualizerChannel.invokeMethod("onWaveformVisualization", args);
                        }


                        @Override
                        public void onFftDataCapture(Visualizer visualizer, byte[] sharedFft, int samplingRate) {

                            byte[] fft = Arrays.copyOf(sharedFft, sharedFft.length);

                            Map<String, Object> args = new HashMap<>();
                            args.put("fft", fft);
                            visualizerChannel.invokeMethod("onFftVisualization", args);


                        }
                    });
                    break;
                case "deactivate_visualizer":
                    Log.d(TAG, "Deactivating visualizer");
                    visualizer.deactivate();
                    break;
            }

            result.success(null);
        } catch (IllegalArgumentException e) {
            result.notImplemented();
        }
    }


    private AudioVisualizerPlayerCall parseMethodName( String methodName) {
        Matcher matcher = VISUALIZER_METHOD_NAME_MATCH.matcher(methodName);

        if (matcher.matches()) {
            String command = matcher.group(1);
            return new AudioVisualizerPlayerCall(command);
        } else {
            Log.d(TAG, "Match not found");
            throw new IllegalArgumentException("Invalid audio visualizer message: " + methodName);
        }
    }

    private static class AudioVisualizerPlayerCall {
        public final String command;

        private AudioVisualizerPlayerCall(String command) {
            this.command = command;
        }

        @Override
        public String toString() {
            return String.format("AudioVisualizerPlayerCall - Command: %s", command);
        }
    }
}

