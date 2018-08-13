package com.kencorp.music.musicfinder;

import android.annotation.TargetApi;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.os.Build;
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

/**
 * Created by ZE ONDOUA KONA KENNETH on 02/08/18.
 */

public class SoundEffect {



    private int[] equalizerValues=new int[3];

    private Equalizer equalizer;

    public boolean isActive() {
        return equalizer != null;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void activate(Visualizer.OnDataCaptureListener listener) {


        Equalizer equalizer = new Equalizer(0, MusicfinderPlugin.playerId);
        equalizer.setEnabled(true);
        setupEqualizerFxAndUI();

    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void deactivate()
    {
        equalizer.release();
        //equalizer = null;
    }

    void setupEqualizerFxAndUI()
    {
        short numberFrequencyBands = equalizer.getNumberOfBands();
        // get the level ranges to be used in settings the bands
        // get  lower limit of  the range in milliBels
        final short lowerEqualizerBandLevel = equalizer.getBandLevelRange()[0];
        // get  Upper limit of the range in milliBels
        final short upperEqualizerBandLevel = equalizer.getBandLevelRange()[1];
        // loop through all the equalizer bands  to display the lowest and the uppest levels
        // and the seek bars

        equalizerValues[0]=numberFrequencyBands;
        equalizerValues[1]=lowerEqualizerBandLevel;
        equalizerValues[2]=upperEqualizerBandLevel;

        //channel for send array

        for(short i=0;i<numberFrequencyBands;i++)
        {
            final short equalizerBandIndex =i;

            // frequency header for each seekBar

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
               // frequencyHeaderTextview.setText((equalizer.getCenterFreq(equalizerBandIndex)/1000)+"Mhz");
            }

        //   lowerEqualizerBandLevelTextView.setText((lowerEqualizerBandLevel/100)+"dB");
            // set up upper level textview for this seekbar
        //      upperEqualizerBandLevelTextView.setText((upperEqualizerBandLevel/100)+"dB");
            // ------- seekbar ------
            // set the layout parameters for the seekbar
        /*    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.weight=1;
            // create a new seekBar
            SeekBar seekBar = new SeekBar(this);
            // give the seekBar an ID
            seekBar.setId(i);
            seekBar.setLayoutParams(layoutParams);
            seekBar.setMax(upperEqualizerBandLevel-lowerEqualizerBandLevel);
*/
            // set the progress for the seek bar
  //          seekBar.setProgress(equalizer.getBandLevel(equalizerBandIndex));
            /*
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                        equalizer.setBandLevel(equalizerBandIndex,(short)(progress + lowerEqualizerBandLevel));
                    }
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });*/
            // add the lower and the upper band level textviews and the
            //seekBarRowLayout.addView(lowerEqualizerBandLevelTextView);
           // seekBarRowLayout.addView(seekBar);
           // seekBarRowLayout.addView(upperEqualizerBandLevelTextView);

           // linearLayout.addView(seekBarRowLayout);

            // show the spinner

         //   equalizeSound();
        }


    }

    void equalizeSound()
    {

        // set up the spinner

        ArrayList<String> equalizerPresetNames = new ArrayList<String>();

/*        ArrayAdapter<String> equalizerPresetSpinnerAdapter= new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,equalizerPresetNames);


        equalizerPresetSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner equalizerPresetSpinner = (Spinner) findViewById(R.id.spinner);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            for(short i=0;i<equalizer.getNumberOfPresets();i++)
            {
                equalizerPresetNames.add(equalizer.getPresetName(i));
            }
        }

        equalizerPresetSpinner.setAdapter(equalizerPresetSpinnerAdapter);

        // handle the spinner item selections

        equalizerPresetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    final short lowerEqualizerBandLevel ;
                    short numberFrequencyBands = 0;

                    equalizer.usePreset((short) position);
                    numberFrequencyBands = equalizer.getNumberOfBands();
                    int[] BandsNumber = int[numberFrequencyBands];

                    lowerEqualizerBandLevel = equalizer.getBandLevelRange()[0];

                    for (short i = 0; i < numberFrequencyBands; i++) {
                        short equalizerBandIndex = i;

                        SeekBar seekBar = (SeekBar) findViewById(equalizerBandIndex);

                        seekBar.setProgress(equalizer.getBandLevel(equalizerBandIndex) - lowerEqualizerBandLevel);

                        BandsNumber[i] = equalizer.getBandLevel(equalizerBandIndex) - lowerEqualizerBandLevel;

                    }

                    //channel invokation
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
    }

}

