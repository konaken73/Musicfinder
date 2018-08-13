package com.kencorp.music.musicfinder;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Process;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * MusicFinderPlugin
 */
public class MusicFinderPlugin implements MethodCallHandler, PluginRegistry.RequestPermissionsResultListener {
   private final MethodChannel channel;
   public static int playerId = -1;
   private static final int REQUEST_CODE_STORAGE_PERMISSION = 3777;

   private boolean MEDIAPLAYER=false;

   private Activity activity;
   private Map<String, Object> arguments;
   private boolean executeAfterPermissionGranted;
   private static MusicFinderPlugin instance;
   private Result pendingResult;
   private String artist;
   private int albumid;

    //MusicPlayer
   private static AudioManager am;
   final Handler handler = new Handler();
   private MediaPlayer mediaPlayer;

   private String playlistName;
   Storage sharedpreferencesplugin;

   List<String> list;
    List<String> listAlbum;
    private Uri trackUri;
    String url;
    int newSong;
    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "music_finder");
        instance = new MusicFinderPlugin(registrar.activity(), channel);
        registrar.addRequestPermissionsResultListener(instance);
        channel.setMethodCallHandler(instance);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public MusicFinderPlugin(Activity activity, MethodChannel channel) {
        this.activity = activity;
        this.channel = channel;
        this.channel.setMethodCallHandler(this);
        if (MusicFinderPlugin.am == null) {
            MusicFinderPlugin.am = (AudioManager) activity.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        }

        mediaPlayer = new MediaPlayer();
        MusicFinderPlugin.playerId=mediaPlayer.getAudioSessionId();

        // MusicFinderPlugin.playerId= mediaPlayer.getAudioSessionId();
        Log.d("MusicFinder","Music Plugin is there");
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {

        Log.d("METHOD",call.method);

        if (call.method.equals("getPlatformVersion")) {
            result.success("Android " + Build.VERSION.RELEASE);
        } else if (call.method.equals("getSongs")) {

            Log.d("GetSongs","getSongs");
            pendingResult = result;
            if (!(call.arguments instanceof Map)) {
                throw new IllegalArgumentException("Plugin not passing a map as parameter: " + call.arguments);
            }
            arguments = (Map<String, Object>) call.arguments;
            boolean handlePermission = (boolean) arguments.get("handlePermissions");
            this.executeAfterPermissionGranted = (boolean) arguments.get("executeAfterPermissionGranted");
            checkPermission(handlePermission);
            // result.success(getData());
            pendingResult.success(getData());
        } else if (call.method.equals("play")) {
            //url = ((HashMap) call.arguments()).get("url").toString();
            newSong = (int) ((HashMap) call.arguments()).get("url");
            Log.d("Index PLAY",String.valueOf(newSong));
            Boolean resPlay = play(newSong);

          //  Boolean resPlay = play(url);
            result.success(1);
        }
        else if (call.method.equals("next")) {
            // url = ((HashMap) call.arguments()).get("url").toString();
            newSong = (int) ((HashMap) call.arguments()).get("url");
            Log.d("URL NEXT",String.valueOf(newSong));
            next(newSong);
            //next(url);
            result.success(1);
        }
        else if (call.method.equals("previous")) {

            // url = ((HashMap) call.arguments()).get("url").toString();

            newSong = (int) ((HashMap) call.arguments()).get("url");

            Log.d("URL PREVIOUS",String.valueOf(newSong));

            previous(newSong);
            // previous(url);
            result.success(1);
        }
        else if (call.method.equals("pause")) {
            pause();
            result.success(1);
        } else if (call.method.equals("stop")) {
            stop();
            result.success(1);
        } else if (call.method.equals("seek")) {
            double position = call.arguments();
            seek(position);
            result.success(1);
        } else if (call.method.equals("mute")) {
            Boolean muted = call.arguments();
            mute(muted);
            result.success(1);
        }
        else if (call.method.equals("getAlbums")) {
            pendingResult = result;
            if (!(call.arguments instanceof Map)) {
                throw new IllegalArgumentException("Plugin not passing a map as parameter: " + call.arguments);
            }
            Log.d("GetAlbums","getAlbums");
            arguments = (Map<String, Object>) call.arguments;
            boolean handlePermission = (boolean) arguments.get("handlePermissions");
            this.executeAfterPermissionGranted = (boolean) arguments.get("executeAfterPermissionGranted");
            checkPermission(handlePermission);
            // result.success(getData());
            pendingResult.success(getAlbumData());
        }
        else if (call.method.equals("getArtists")) {
            pendingResult = result;
            if (!(call.arguments instanceof Map)) {
                throw new IllegalArgumentException("Plugin not passing a map as parameter: " + call.arguments);
            }

            Log.d("GetArtists","getArtists");

            arguments = (Map<String, Object>) call.arguments;
            boolean handlePermission = (boolean) arguments.get("handlePermissions");
            this.executeAfterPermissionGranted = (boolean) arguments.get("executeAfterPermissionGranted");
            checkPermission(handlePermission);
            // result.success(getData());
            pendingResult.success(getArtistData());
        }
        else if (call.method.equals("getArtistSong")) {
            pendingResult = result;
            Log.d("GetArtistsSong","getArtistSong");

            artist = (String) ((HashMap) call.arguments()).get("artist");
            // result.success(getData());
            Log.d("GetArtistSongs",artist);
            pendingResult.success(getArtistSongData(artist));
        }
        else if (call.method.equals("getAlbumSong")) {
            pendingResult = result;

            albumid = (int) ((HashMap) call.arguments()).get("albumid");

            Log.d("GetAlbumSong","getAlbumSong -"+String.valueOf(albumid));
            pendingResult.success(getAlbumSongData(albumid));
        }
        else if (call.method.equals("shareDat1a")) {
            pendingResult = result;

            Log.d("GetStringSong","getStringSong -");
            pendingResult.success(getStringSong());
        }
        else if (call.method.equals("ImageData1")) {

            Log.d("GetImageList","getImageList -");

            getImages();
            pendingResult.success(1);
        }
        else if (call.method.equals("shareAlbum1")) {
            pendingResult = result;

            Log.d("GetsHAREALBUM","getStringSong -");
            getAlbums();
            pendingResult.success(1);
        }
        else if (call.method.equals("shareArtist1")) {
            pendingResult = result;


            Log.d("GetStringSongARTIST","getStringSong -");
            getArtists();
            pendingResult.success(1);
        }
        else if (call.method.equals("createplaylist")) {
            pendingResult = result;

             playlistName = (String) ((HashMap) call.arguments()).get("playlist_name");

            Log.d("CreatePlayList","CreatePlayList -");

            createPlaylist(playlistName);

            pendingResult.success(1);
        }
        else if (call.method.equals("playlist")) {
            pendingResult = result;

            Log.d("CreatePlayList","CreatePlayList -");

            getPlaylist();
            pendingResult.success(1);
        }
        else if (call.method.equals("addTrackToplaylist")) {
            pendingResult = result;

            ArrayList<String> members = (ArrayList<String>) ((HashMap) call.arguments()).get("members");

            addMembers(members);
            Log.d("addTrackToplaylist","addTrackToplaylist -");
            getPlaylist();
            pendingResult.success(1);
        }
        else if (call.method.equals("getTrackPlaylist")) {
            pendingResult = result;
            int playlist_id = (int) ((HashMap) call.arguments()).get("playlist_id");
            Log.d("getTrackPlaylist","getTrackPlaylist -");

            getTrackPlaylist(playlist_id);
            pendingResult.success(1);
        }
        else if (call.method.equals("Genres")) {
            pendingResult = result;

            //getGenres();
            Log.d("Genres","Genres -");

            pendingResult.success(getGenres());
        }
        else if (call.method.equals("getSongByGenres")) {
            pendingResult = result;
            int genre = (int) ((HashMap) call.arguments()).get("genre");
            Log.d("getSongByGenres","getSongByGenres -");

            //getSongByGenres(genre);
            pendingResult.success(getSongByGenres(genre));
        }
        else
            {
            result.notImplemented();
        }
    }

    private void checkPermission(boolean handlePermission) {
        if (checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // TODO: user should be explained why the app needs the permission
                if (handlePermission) {
                    requestPermissions();
                } else {
                    setNoPermissionsError();
                }
            } else {
                if (handlePermission) {
                    requestPermissions();
                } else {
                    setNoPermissionsError();
                }
            }

        } else {
           // pendingResult.success(getData());
            //pendingResult = null;
            //arguments = null;
        }
    }


    public void getImages()
    {

        Gallery gallery = new Gallery(activity.getContentResolver(),activity.getApplicationContext());

        gallery.prepare();
        String list = gallery.getImageList();

        Log.d("GETIMAGES",list);
        channel.invokeMethod("ImageData", list);

    }

    public void getAlbums()
    {

        sharedpreferencesplugin=new Storage(activity.getApplicationContext());
        String albums=sharedpreferencesplugin.getAlbum();
        channel.invokeMethod("shareDataAlbum", albums);

    }

    public void getArtists()
    {

        sharedpreferencesplugin=new Storage(activity.getApplicationContext());
        String artist=sharedpreferencesplugin.getSong();
        channel.invokeMethod("shareDataArtist", artist);

    }


    public void getTrackPlaylist(long playlist_id)
    {

        MusicFinder mf = new MusicFinder(activity.getContentResolver(),activity.getApplicationContext());
        List<String> data = mf.TrackPlaylist(playlist_id);
        channel.invokeMethod("TrackPlaylist", data);

    }

    ArrayList<HashMap> getStringSong()
    {
        sharedpreferencesplugin = new Storage(activity.getApplicationContext());
        //Log.d("shareData StringSong",list.toString());
        String string=sharedpreferencesplugin.getSong();
        Log.d("shareData StringSong",string);

        return null;
    }

    ArrayList<HashMap> getData() {
        MusicFinder mf = new MusicFinder(activity.getContentResolver(),activity.getApplicationContext());

        mf.prepare();
        List<MusicFinder.Song> allsongs = mf.getAllSongs();

        System.out.print(allsongs);
        ArrayList<HashMap> songsMap = new ArrayList<>();
        for (MusicFinder.Song s : allsongs) {
            songsMap.add(s.toMap());
        }


         list = mf.getStringSong();

        Log.d("shareData StringSong",list.toString());

        sharedpreferencesplugin=new Storage(activity.getApplicationContext());
        sharedpreferencesplugin.storeSong(list.toString());
        channel.invokeMethod("shareData", list);

        Log.d("MusicFinder","Music Plugin get data");
        return songsMap;
    }


    // ArrayList for Album Song DATA

    String getSongData(long id) {
        MusicFinder mf = new MusicFinder(activity.getContentResolver(),activity.getApplicationContext());
        String data=mf.getSongData(id);

        Log.d("MusicFinder URI_DATA",data);
        return data;
    }


    // ArrayList for PLaylist Song DATA

    String getPlaylist() {
        MusicFinder mf = new MusicFinder(activity.getContentResolver(),activity.getApplicationContext());
        List<String>  data=mf.Playlists();

         channel.invokeMethod("PLAYLIST",data);
        Log.d("MusicFinder URI_DATA",data.toString());
        return data.toString();
    }


    void createPlaylist(String name) {
        MusicFinder mf = new MusicFinder(activity.getContentResolver(),activity.getApplicationContext());
         mf.CreateNewPlaylist(name);
        Log.d("Music PLAYLIST_CREATED",name);
        //return data.toString();
    }

    void addMembers(ArrayList<String> members)
    {
        MusicFinder mf = new MusicFinder(activity.getContentResolver(),activity.getApplicationContext());

        String[] member = members.get(0).split("ELTID");
        long playlist_id=Integer.parseInt(member[1]);
        List<String> data = mf.TrackPlaylist(playlist_id);

        int n=data.size();

        int i;
        for(i=0;i<members.size();i++)
        {
             member = members.get(i).split("ELTID");
          mf.addTrackToPlaylist(member[0],Integer.parseInt(member[1]),n+i);
        }
         data = mf.TrackPlaylist(playlist_id);
        channel.invokeMethod("TrackPlaylist", data);

    }


    //Genres

    List<String> getGenres()
    {
        MusicFinder mf = new MusicFinder(activity.getContentResolver(),activity.getApplicationContext());

        List<String> data = mf.genres();
        Log.d("getGenres",data.toString());
       // channel.invokeMethod("getGenres", data);

        return data;
    }


    //Song by Genres

    List<String> getSongByGenres(int genre)
    {

        MusicFinder mf = new MusicFinder(activity.getContentResolver(),activity.getApplicationContext());
        List<String>  data=mf.genresForAudio(genre);

        //channel.invokeMethod("PLAYLIST",data);
        Log.d("MusicFinder URI_DATA",data.toString());

        return data;

    }



    // ArrayList for Album DATA

    ArrayList<HashMap> getAlbumData() {
        MusicFinder mf = new MusicFinder(activity.getContentResolver(),activity.getApplicationContext());
        mf.prepareAlbum();
        List<MusicFinder.Album> allAlbums = mf.getAllAlbums();
        System.out.print(allAlbums);
        ArrayList<HashMap> songsMap = new ArrayList<>();
        for (MusicFinder.Album s : allAlbums) {
            songsMap.add(s.toMap());
        }

        listAlbum=mf.getStringAlbum();

        sharedpreferencesplugin=new Storage(activity.getApplicationContext());
        sharedpreferencesplugin.storeAlbum(listAlbum.toString());
        channel.invokeMethod("shareAlbum", listAlbum.toString());

        Log.d("AlbumFinder",listAlbum.toString());
        Log.d("MusicFinder Album","Music Plugin get album");
        return songsMap;
    }



    // ArrayList for Album Song DATA

    ArrayList<HashMap> getAlbumSongData(int albumid) {
        MusicFinder mf = new MusicFinder(activity.getContentResolver(),activity.getApplicationContext());
        mf.getAlbumSongs(albumid);
        List<MusicFinder.Song> allAlbumSong = mf.getAllAlbumSong();
        System.out.print(allAlbumSong);
        ArrayList<HashMap> songsMap = new ArrayList<>();
        for (MusicFinder.Song s : allAlbumSong) {
            songsMap.add(s.toMap());
        }

        Log.d("MusicFinder","Music Plugin get album song");
        return songsMap;
    }

    // ArrayList for Album DATA

    ArrayList<HashMap> getArtistData() {
        MusicFinder mf = new MusicFinder(activity.getContentResolver(),activity.getApplicationContext());
        mf.prepareArtist();
        List<MusicFinder.Artist> allArtists = mf.getAllArtists();
        System.out.print(allArtists);
        ArrayList<HashMap> songsMap = new ArrayList<>();
        for (MusicFinder.Artist s : allArtists) {
            songsMap.add(s.toMap());
        }

        list=mf.getStringArtist();
        channel.invokeMethod("shareArtist", list);
        sharedpreferencesplugin=new Storage(activity.getApplicationContext());
        sharedpreferencesplugin.storeSong(list.toString());


        Log.d("ArtistFinder","Music Plugin get artist");
        return songsMap;
    }


    // ArrayList for Artist Song DATA

    ArrayList<HashMap> getArtistSongData(String artist) {
        MusicFinder mf = new MusicFinder(activity.getContentResolver(),activity.getApplicationContext());
        mf.getArtistSongs(artist);
         Log.d("getArtistSongs1",artist);
        List<MusicFinder.Song> allArtistSong = mf.getAllArtistSong();
        System.out.print(allArtistSong);
        ArrayList<HashMap> songsMap = new ArrayList<>();
        for (MusicFinder.Song s : allArtistSong) {
            songsMap.add(s.toMap());
        }

        Log.d("MusicFinder","Music Plugin get artist song");
        return songsMap;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermissions() {
        activity.requestPermissions(new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                REQUEST_CODE_STORAGE_PERMISSION);
    }

    private boolean shouldShowRequestPermissionRationale(Activity activity, String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            return activity.shouldShowRequestPermissionRationale(permission);
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.BASE_1_1)
    private int checkSelfPermission(Context context, String permission) {
        if (permission == null) {
            throw new IllegalArgumentException("permission is null");
        }
        return context.checkPermission(permission, Process.myPid(), Process.myUid());
    }

    @Override
    public boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        if (executeAfterPermissionGranted) {
                            pendingResult.success(getData());
                           // pendingResult = null;
                            //arguments = null;
                        }
                    } else {
                        setNoPermissionsError();
                    }
                }
            }
        }
        return false;
    }

    private void setNoPermissionsError() {
        pendingResult.error("permission", "you don't have the user permission to access the camera", null);
        pendingResult = null;
        arguments = null;
    }

    private void mute(Boolean muted) {
        if (MusicFinderPlugin.am == null)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            MusicFinderPlugin.am.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                    muted ? AudioManager.ADJUST_MUTE : AudioManager.ADJUST_UNMUTE, 0);
        } else {
            MusicFinderPlugin.am.setStreamMute(AudioManager.STREAM_MUSIC, muted);
        }
    }

    private void seek(double position) {
        mediaPlayer.seekTo((int) (position * 1000));
    }

    private void stop() {
        handler.removeCallbacks(sendData);
        //if (mediaPlayer != null) {

          if(MEDIAPLAYER)
          {
             /*
              if (mediaPlayer != null) {

                  mediaPlayer.stop();
                  mediaPlayer.release();
                  mediaPlayer = null;
              }*/
             //mediaPlayer.stop();
             //mediaPlayer.release();
             //mediaPlayer = null;
              MEDIAPLAYER=false;
          }
    }

    private void pause() {
        mediaPlayer.pause();
        handler.removeCallbacks(sendData);
    }

    //private void previous(String url) {

    private void previous(int newSong) {
        //mediaPlayer.release();
        MEDIAPLAYER=false;
        //handler.removeCallbacks(null);
        //mediaPlayer.stop();
        //mediaPlayer.release();

        play(newSong);
       // this.play(url);
    }

    //private void next(String url) {
    private void next(int newSong) {
        //mediaPlayer.stop();
       // stop();
       // handler.removeCallbacks(null);

        Log.d("NEXT MEDIALIST",String.valueOf(newSong));
        MEDIAPLAYER=false;
        //mediaPlayer.stop();

       // mediaPlayer.release();

        play(newSong);
    //    this.play(url);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
  //  private Boolean play(String url) {
    private Boolean play(int currentSong) {
        //if (mediaPlayer == null)
        if(!MEDIAPLAYER)
        {

            MEDIAPLAYER=true;
            /*if (mediaPlayer == null){
                mediaPlayer = new MediaPlayer();
                MusicFinderPlugin.playerId=mediaPlayer.getAudioSessionId();
                 //set Uri

             Uri trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,currentSong);
            }*/

            trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,currentSong);

            Log.d("MEDIALIST PLAY MUSIC 1:", String.valueOf(currentSong)+trackUri);

            mediaPlayer.reset();
            Log.d("PLAY MUSIC", String.valueOf(MusicFinderPlugin.playerId));
            //  mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {


                   Log.d("PLAY MUSIC MEDIALIST:", String.valueOf(trackUri));
                mediaPlayer.setDataSource(activity.getApplicationContext(),trackUri);

               // Log.d("PLAY MUSIC MEDIALIST:", getSongData(currentSong));
               channel.invokeMethod("uriData",getSongData(currentSong));

               // Log.d("PLAY MUSIC MEDIALIST:", url);
               // mediaPlayer.setDataSource(url);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("AUDIO", "invalid DataSource");
            }

            Log.d("PLAY MEDIALIST MUSIC 3:", String.valueOf(currentSong));
            //mediaPlayer.prepareAsync();
          } else {
            channel.invokeMethod("audio.onDuration", mediaPlayer.getDuration());

            mediaPlayer.start();
            channel.invokeMethod("audio.onStart", true);
        }

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                channel.invokeMethod("audio.onDuration", mediaPlayer.getDuration());

                mediaPlayer.start();
                channel.invokeMethod("audio.onStart", true);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //stop();
               //  mp.reset();
                //MEDIAPLAYER=false;

                channel.invokeMethod("audio.onComplete", true);
            }
        });

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                channel.invokeMethod("audio.onError", String.format("{\"what\":%d,\"extra\":%d}", what, extra));
                return true;
            }
        });

        handler.post(sendData);

        return true;
    }

    private final Runnable sendData = new Runnable() {
        public void run() {
            try {
                if (!mediaPlayer.isPlaying()) {
                    handler.removeCallbacks(sendData);
                }
                int time = mediaPlayer.getCurrentPosition();
                channel.invokeMethod("audio.onCurrentPosition", time);

                handler.postDelayed(this, 200);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

}

