package com.kencorp.music.musicfinder;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class MusicFinder {
    private static final String TAG = "music_finder";

    private ContentResolver mContentResolver;
    private List<Song> mSongs = new ArrayList<Song>();
    private List<Song> mAlbumSongs = new ArrayList<Song>();
    private List<Album> mAlbums = new ArrayList<Album>();
    private List<Song> mArtistSongs = new ArrayList<Song>();
    private List<Artist> mArtists = new ArrayList<Artist>();


   // private List<Playlist> mPlaylist = new ArrayList<Playlist>();

    private List<String> mGenre = new ArrayList<String>();
    private List<String> SongByGenre = new ArrayList<String>();

    private Random mRandom = new Random();

    private List<String> pattern = new ArrayList<String>();

    private List<String> artistList = new ArrayList<String>();
    private List<String> albumList = new ArrayList<String>();

    private List<String> playlist = new ArrayList<String>();
    private List<String> trackPlaylist = new ArrayList<String>();

    List<Integer> GenreId= new ArrayList<Integer>();

    Activity activity;
    Context context;

    Storage sharedpreferencesplugin;

    // Storage sharedPreferencesPlugin= new Storage();
    public MusicFinder(ContentResolver cr, Context context) {
        mContentResolver = cr;
        // new SharedPreferencesPlugin(activity.getApplicationContext()) ;
        sharedpreferencesplugin = new Storage(context);
    }

    public void prepare() {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri uriInternal=MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
        Cursor cur = mContentResolver.query(uri, null,
                MediaStore.Audio.Media.IS_MUSIC + " = 1", null, null);

        if (cur == null) {
            return;
        }
        if (!cur.moveToFirst()) {
            return;
        }

        int artistColumn = cur.getColumnIndex(MediaStore.Audio.Media.ARTIST);
        int titleColumn = cur.getColumnIndex(MediaStore.Audio.Media.TITLE);
        int albumColumn = cur.getColumnIndex(MediaStore.Audio.Media.ALBUM);
        int albumArtColumn = cur.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
        int durationColumn = cur.getColumnIndex(MediaStore.Audio.Media.DURATION);
        int idColumn = cur.getColumnIndex(MediaStore.Audio.Media._ID);

        do {

            Long currentId = cur.getLong(idColumn);
            String artistname = cur.getString(artistColumn);
            String title = cur.getString(titleColumn);

            String album = cur.getString(albumColumn);

            Long duration = cur.getLong(durationColumn);
            Long albumid = cur.getLong(albumArtColumn);

            mSongs.add(new Song(
                    currentId,
                    artistname,
                    title,
                    album,
                    duration,
                    albumid
            ));



            String path = sharedpreferencesplugin.getArt(String.valueOf(albumid));


            pattern.add(String.valueOf(currentId) + "ELTID" + artistname.replace(',','-') + "ELTID" + title.replace(',','-') + "ELTID" +
                    album.replace(',','-') + "ELTID" + String.valueOf(albumid) +
                    "ELTID" + String.valueOf(duration)+"ELTID" +path);

            // Log.d("shareData",pattern);


           /* pattern.add(String.valueOf(currentId) + "ELTID" + artistname + "ELTID" + title + "ELTID" +
                    album + "ELTID" + String.valueOf(duration) +
                    "ELTID" + String.valueOf(albumid));
            */
        } while (cur.moveToNext());


        cur = mContentResolver.query(uriInternal, null,
                MediaStore.Audio.Media.IS_MUSIC + " = 1", null, null);

        if (cur == null) {
            return;
        }
        if (!cur.moveToFirst()) {
            return;
        }


         artistColumn = cur.getColumnIndex(MediaStore.Audio.Media.ARTIST);
         titleColumn = cur.getColumnIndex(MediaStore.Audio.Media.TITLE);
         albumColumn = cur.getColumnIndex(MediaStore.Audio.Media.ALBUM);
         albumArtColumn = cur.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
         durationColumn = cur.getColumnIndex(MediaStore.Audio.Media.DURATION);
         idColumn = cur.getColumnIndex(MediaStore.Audio.Media._ID);

        do {

            Long currentId = cur.getLong(idColumn);
            String artistname = cur.getString(artistColumn);
            String title = cur.getString(titleColumn);

            String album = cur.getString(albumColumn);

            Long duration = cur.getLong(durationColumn);
            Long albumid = cur.getLong(albumArtColumn);

            mSongs.add(new Song(
                    currentId,
                    artistname,
                    title,
                    album,
                    duration,
                    albumid
            ));



            String path = sharedpreferencesplugin.getArt(String.valueOf(albumid));


            pattern.add(String.valueOf(currentId) + "ELTID" + artistname.replace(',','-') + "ELTID" + title.replace(',','-') + "ELTID" +
                    album.replace(',','-') + "ELTID" + String.valueOf(albumid) +
                    "ELTID" + String.valueOf(duration)+"ELTID" +path);

            // Log.d("shareData",pattern);


           /* pattern.add(String.valueOf(currentId) + "ELTID" + artistname + "ELTID" + title + "ELTID" +
                    album + "ELTID" + String.valueOf(duration) +
                    "ELTID" + String.valueOf(albumid));
            */
        } while (cur.moveToNext());


    }

    public ContentResolver getContentResolver() {
        return mContentResolver;
    }

    public Song getRandomSong() {
        if (mSongs.size() <= 0) return null;
        return mSongs.get(mRandom.nextInt(mSongs.size()));
    }

    public List<Song> getAllSongs() {
        return mSongs;
    }

    // get MUSIC DATA
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public String getSongData(long id) {

        String uriSong = "";
        //RETRIEVE Data Song INFO

        Log.d("GetSongData inside", String.valueOf(id));

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

/*
               String where = MediaStore.Audio.Media._ID;
               String whereVal[] = {String.valueOf(id)};
             String orderBy = MediaStore.Audio.Media._ID;
           Cursor cur = mContentResolver.query(uri, null,where ,whereVal ,orderBy,null);
*/
        Cursor cur = mContentResolver.query(uri, null,
                MediaStore.Audio.Media.IS_MUSIC + " = 1", null, null);

        if (cur == null) {
            return "";
        }
        if (!cur.moveToFirst()) {
            return "";
        }

        int data = cur.getColumnIndex(MediaStore.Audio.Media.DATA);
        int dataid = cur.getColumnIndex(MediaStore.Audio.Media._ID);


        do {
            Log.d("GetAlbumSong", cur.getString(data));
            if (cur.getLong(dataid) == id) {

                uriSong=cur.getString(data);
                      cur.moveToLast();
            }
        } while (cur.moveToNext());

       return uriSong;
    }



    public List<String> getStringSong() {
        return pattern;
    }

    //  ALBUM MUSIC
    public void prepareAlbum() {

        //RETRIEVE ALBUM INFO

        Uri songUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;

        Uri albumUri = MediaStore.Audio.Albums.INTERNAL_CONTENT_URI;


        final String[] columns = {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM, MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.ALBUM_ART, MediaStore.Audio.Albums.NUMBER_OF_SONGS};

        Cursor songCursor = mContentResolver.query(songUri, columns, null, null, null);

        //Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

        if (songCursor != null && songCursor.moveToFirst()) {
            int id = songCursor.getColumnIndex(MediaStore.Audio.Albums._ID);
            int albumName = songCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
            int artist = songCursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST);


            int albumArt = songCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
            int tracks = songCursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS);


            do {
                String currentArt = songCursor.getString(albumArt);
                // Bitmap art = BitmapFactory.decodeFile(currentArt);

                String currentArtist = songCursor.getString(artist);

                Long currentId = Long.parseLong(songCursor.getString(id));
                Log.d("AlbumID", String.valueOf(currentId));
                String currentAlbum = songCursor.getString(albumName);

                sharedpreferencesplugin.storeArt(String.valueOf(currentId), currentArt);

                sharedpreferencesplugin.storeArt(currentArtist, currentArt);

                int nbreSong = Integer.parseInt(songCursor.getString(tracks));
                mAlbums.add(new Album(currentId, currentAlbum, currentArtist, nbreSong, currentArt));

                albumList.add(String.valueOf(currentId) + "ELTID" + currentAlbum.replace(',', '-') + "ELTID" +
                        currentArtist.replace(',', '-') + "ELTID" + String.valueOf(nbreSong) + "ELTID" + currentArt);

            } while (songCursor.moveToNext());


        }



        final String[] columns1 = {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM, MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.ALBUM_ART, MediaStore.Audio.Albums.NUMBER_OF_SONGS};

         songCursor = mContentResolver.query(albumUri, columns, null, null, null);

        //Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

        if (songCursor != null && songCursor.moveToFirst()) {
            int id = songCursor.getColumnIndex(MediaStore.Audio.Albums._ID);
            int albumName = songCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
            int artist = songCursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST);


            int albumArt = songCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
            int tracks = songCursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS);


            do {
                String currentArt = songCursor.getString(albumArt);
                // Bitmap art = BitmapFactory.decodeFile(currentArt);

                String currentArtist = songCursor.getString(artist);

                Long currentId = Long.parseLong(songCursor.getString(id));
                Log.d("AlbumID", String.valueOf(currentId));
                String currentAlbum = songCursor.getString(albumName);

                sharedpreferencesplugin.storeArt(String.valueOf(currentId), currentArt);

                sharedpreferencesplugin.storeArt(currentArtist, currentArt);

                int nbreSong = Integer.parseInt(songCursor.getString(tracks));
                mAlbums.add(new Album(currentId, currentAlbum, currentArtist, nbreSong, currentArt));

                albumList.add(String.valueOf(currentId) + "ELTID" + currentAlbum.replace(',', '-') + "ELTID" +
                        currentArtist.replace(',', '-') + "ELTID" + String.valueOf(nbreSong) + "ELTID" + currentArt);

            } while (songCursor.moveToNext());


        }
    }

    // Get All Albums

    public List<Album> getAllAlbums() {

        Log.d("getAllAlbums", "List Album " + mAlbums);
        return mAlbums;
    }


    // get MUSIC of a particular Album
   // @TargetApi(Build.VERSION_CODES.JELLY_BEAN)

    public void getAlbumSongs(int albumid) {

        //RETRIEVE ALBUM INFO

        Log.d("GetAlbumSong inside", String.valueOf(albumid));

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;


//        String where = MediaStore.Audio.Media.ALBUM_ID;
        //      String whereVal[] = {String.valueOf(albumid)};
        //    String orderBy = MediaStore.Audio.Media.ALBUM_ID;
        //   Cursor cur = mContentResolver.query(uri, null,where ,whereVal ,orderBy,null);

        Cursor cur = mContentResolver.query(uri, null,
                MediaStore.Audio.Media.IS_MUSIC + " = 1", null, null);

        if (cur == null) {
            return;
        }
        if (!cur.moveToFirst()) {
            return;
        }



        int artistColumn = cur.getColumnIndex(MediaStore.Audio.Media.ARTIST);
        int titleColumn = cur.getColumnIndex(MediaStore.Audio.Media.TITLE);
        int albumColumn = cur.getColumnIndex(MediaStore.Audio.Media.ALBUM);
        int albumArtColumn = cur.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
        int durationColumn = cur.getColumnIndex(MediaStore.Audio.Media.DURATION);
        int idColumn = cur.getColumnIndex(MediaStore.Audio.Media._ID);

        do {
            Log.d("GetAlbumSong", cur.getString(artistColumn));
            if (cur.getLong(albumArtColumn) == albumid) {
                mAlbumSongs.add(new Song(
                        cur.getLong(idColumn),
                        cur.getString(artistColumn),
                        cur.getString(titleColumn),
                        cur.getString(albumColumn),
                        cur.getLong(durationColumn),
                        cur.getLong(albumArtColumn)));
                //      cur.moveToLast();
            }
        } while (cur.moveToNext());

    }


    // Get All AlbumSongs

    public List<Song> getAllAlbumSong() {

        Log.d("getAllAlbumSong", "List Song of Album " + mAlbumSongs);
        return mAlbumSongs;
    }

    public List<String> getStringAlbum()
    {
        return albumList;
    }

    public List<String> getStringArtist()
    {
        return artistList;
    }

    //Artist

    public void prepareArtist() {
        //RETRIEVE ARTIST INFO

        Uri artistUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;

        final String[] columns = {MediaStore.Audio.Artists._ID, MediaStore.Audio.Artists.NUMBER_OF_ALBUMS, MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
                MediaStore.Audio.Artists.Albums.ARTIST};

        Cursor songCursor = mContentResolver.query(artistUri, columns, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {
            int id = songCursor.getColumnIndex(MediaStore.Audio.Artists._ID);
            int album = songCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS);
            int tracks = songCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS);
            // int song = songCursor.getColumnIndex(MediaStore.Audio.Artists.Albums.NUMBER_OF_SONGS_FOR_ARTIST);
            int name = songCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST);

            // int albumArt= songCursor.getColumnIndex(MediaStore.Audio.Artists);


            do {

                Long currentId = Long.parseLong(songCursor.getString(id));

                String currentArtist = songCursor.getString(name);

                //   int nbreSong= Integer.parseInt(songCursor.getString(song));
                int nbreAlbum = Integer.parseInt(songCursor.getString(album));
                int nbreTracks = Integer.parseInt(songCursor.getString(tracks));

                //String art =" ";
                //String art = songCursor.getString(albumArt);

                String art = sharedpreferencesplugin.getArt(String.valueOf(currentArtist));

                //sharedPreferencesPlugin.getArt(String.valueOf(albumId));

                mArtists.add(new Artist(currentId, currentArtist, nbreAlbum, nbreTracks, art));

                artistList.add(String.valueOf(currentId)+"ELTID"+currentArtist.replace(',','-')+"ELTID"
                +String.valueOf(nbreAlbum)+"ELTID"+String.valueOf(nbreTracks)+"ELTID"+art);

            } while (songCursor.moveToNext());

        }
    }


    // Playlist
    public List<String> Playlists() {

        //RETRIEVE PLAYLIST INFO

        Uri playlistUri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;

        final String[] columns = {MediaStore.Audio.Playlists._ID,MediaStore.Audio.Playlists.NAME};

        Cursor playlistCursor = mContentResolver.query(playlistUri, columns, null, null, null);

        if (playlistCursor != null && playlistCursor.moveToFirst()) {

            // int albumArt= songCursor.getColumnIndex(MediaStore.Audio.Artists);
            int id = playlistCursor.getColumnIndex(MediaStore.Audio.Playlists._ID);
             int name = playlistCursor.getColumnIndex(MediaStore.Audio.Playlists.NAME);
            // int nbre = playlistCursor.getColumnIndex(MediaStore.Audio.Playlists.Members._COUNT);

            do {

                long playlist_id= Integer.parseInt(playlistCursor.getString(id));

                Uri memberUri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlist_id);

                Cursor memberCursor = mContentResolver.query(memberUri, null, null, null, null);

                String members = playlistCursor.getString(id)+"ELTID"+playlistCursor.getString(name)+"ELTID"+memberCursor.getCount()+" morceaux";

                playlist.add(members);
            } while (playlistCursor.moveToNext());

        }
        return  playlist;
    }

    //create a play list

    public void CreateNewPlaylist(String name) {
        //settings the new playlist
        ContentValues values = new ContentValues(1);
        values.put(MediaStore.Audio.Playlists.NAME, name);
        values.put(MediaStore.Audio.Playlists.DATE_MODIFIED, System.currentTimeMillis());
        //actually inserting the new playlist
        mContentResolver.insert(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, values);

        Log.d("CreateNewPlaylist**--* ", name);
    }

    public void addTrackToPlaylist(String audio_id, long playlist_id, int pos) {
        Uri newUri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlist_id);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, pos);
        values.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, audio_id);
        values.put(MediaStore.Audio.Playlists.Members.PLAYLIST_ID, playlist_id);
        mContentResolver.insert(newUri, values);
        Log.d("addTrackPlaylist**--** ", String.valueOf(playlist_id));
    }

    //get track from the  playlist id

    List<String> TrackPlaylist(long playlist_id)
    {
       // Uri playlistUri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;

       Uri newUri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlist_id);

       final String[] columns = {MediaStore.Audio.Playlists.Members.AUDIO_ID};

       Cursor songCursor = mContentResolver.query(newUri, columns, null, null, null);

       if (songCursor != null && songCursor.moveToFirst()) {

            // int albumArt= songCursor.getColumnIndex(MediaStore.Audio.Artists);
            int id = songCursor.getColumnIndex(MediaStore.Audio.Playlists.Members.AUDIO_ID);

            do {

                trackPlaylist.add(songCursor.getString(id));
               // playlist.add(songCursor.getString(id));
            } while (songCursor.moveToNext());

        }
        return trackPlaylist;
    }

    // Genres

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    List<String> genres()
    {

        final String[] genres = {MediaStore.Audio.Genres._ID,MediaStore.Audio.Genres.NAME};

        String path;



            Uri genreslistUri = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI;

            Cursor songCursor = mContentResolver.query(genreslistUri, genres, null, null,
                    null);


        if (songCursor != null && songCursor.moveToFirst()) {

            Log.d("Genres COUNT",String.valueOf(songCursor.getCount()));

            // int albumArt= songCursor.getColumnIndex(MediaStore.Audio.Artists);
                int name = songCursor.getColumnIndex(MediaStore.Audio.Genres.NAME);
                int id = songCursor.getColumnIndex(MediaStore.Audio.Genres._ID);
                do {

                    int genreId = Integer.parseInt(songCursor.getString(id));
                    Uri memberUri = MediaStore.Audio.Genres.Members.getContentUri("external",genreId);

                    Cursor memberCursor = mContentResolver.query(memberUri, null, null, null,
                            null);

                    if(memberCursor !=null && memberCursor.moveToFirst())
                    {

                        int albumid = memberCursor.getColumnIndex(MediaStore.Audio.Genres.Members.ALBUM_ID);
                        int ID = memberCursor.getColumnIndex(MediaStore.Audio.Genres.Members.GENRE_ID);
                        Log.d("",String.valueOf(memberCursor.getCount()));

                        //do {

                            Log.d("Genres Album_id",String.valueOf(memberCursor.getInt(albumid)));
                            path = sharedpreferencesplugin.getArt(String.valueOf(memberCursor.getInt(albumid)));

                         //   if(memberCursor.getString(ID)==songCursor.getString(id))
                           // {
                                mGenre.add(songCursor.getString(id)+"ELTID"+songCursor.getString(name)+"ELTID"+path);

                            //}

                        //}while (memberCursor.moveToNext());

                    }
                   // mGenre.add(songCursor.getString(id)+"ELTID"+songCursor.getString(name));

                    GenreId.add(Integer.parseInt(songCursor.getString(id)));
                } while (songCursor.moveToNext());
            }


        Log.d("Genres 1",mGenre.toString());
        Log.d("Genres 2",GenreId.toString());
           // int i=0;

            /*String genre;
            while(i<GenreId.size())
            {

               path= matchGenreAlbum(GenreId.get(i));

               genre=mGenre.get(i);
               genre.concat("ELTID"+path);
               mGenre.set(i,genre);
                i++;
            }
               */

            Log.d("Genres",mGenre.toString());
            return mGenre;

    }

    //match genres with album

    String matchGenreAlbum(int genreId)
    {

        String path=" ";
        Uri memberUri = MediaStore.Audio.Genres.Members.getContentUri("external",genreId);

        Cursor memberCursor = mContentResolver.query(memberUri, null, null, null,
                null);

        if(memberCursor !=null && memberCursor.moveToFirst())
        {


        int albumid = memberCursor.getColumnIndex(MediaStore.Audio.Genres.Members.ALBUM_ID);

        Log.d("Genres Album_id",String.valueOf(memberCursor.getCount()));

        do {

            Log.d("Genres Album_id",String.valueOf(memberCursor.getInt(albumid)));
            path = sharedpreferencesplugin.getArt(String.valueOf(memberCursor.getInt(albumid)));

        }while (memberCursor.moveToNext());

        }

        return path;
    }


    //song list by Genres

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    List<String> genresForAudio(int genreId)
    {

       // Log.d("genresForAudio 1",genre);

       // Uri medialistUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        //Log.d("genresForAudio 2",genre);

        //Cursor songCursor;
       // final String[] columns = {MediaStore.Audio.Media._ID};

        //Cursor cursor = mContentResolver.query(medialistUri, null, null, null, null);

        //if (cursor != null && cursor.moveToFirst()) {

         //   int idMedia = cursor.getColumnIndex(MediaStore.Audio.Media._ID);

            Log.d("genresForAudio 3",String.valueOf(genreId));
       // final String[] genres = {MediaStore.Audio.Genres.NAME};

        //Log.d("genresForAudio 4",genre);
       // Log.d("genresForAudio 4",String.valueOf(cursor.getCount()));
        //do
        //{

         //   int idaudio = Integer.parseInt(cursor.getString(idMedia));

            Uri genreslistUri = MediaStore.Audio.Genres.Members.getContentUri("external",genreId);

            Cursor songCursor = mContentResolver.query(genreslistUri, null, null, null,
                    null);

            if (songCursor != null && songCursor.moveToFirst()) {

                // int albumArt= songCursor.getColumnIndex(MediaStore.Audio.Artists);
                int id = songCursor.getColumnIndex(MediaStore.Audio.Genres.Members.AUDIO_ID);

                do {

                     //if(genre==songCursor.getString(name))
                    // {
                       //  Log.d("genresForAudio",String.valueOf(idaudio));

                         SongByGenre.add(songCursor.getString(id));

                     //}

                 } while (songCursor.moveToNext());
              }

         //  }while(cursor.moveToNext());
        //}
        return SongByGenre;
    }

    // Get All Artist

    public List<Artist> getAllArtists() {
        return mArtists;
    }

    // get MUSIC of a particular Artist
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void getArtistSongs(String artist) {

        //RETRIEVE Artist INFO

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor cur = mContentResolver.query(uri, null,
                MediaStore.Audio.Media.IS_MUSIC + " = 1", null, null);

        Log.d("getArtistSongs **---** ", artist);

        if (cur == null) {
            return;
        }
        if (!cur.moveToFirst()) {
            return;
        }


        int artistColumn = cur.getColumnIndex(MediaStore.Audio.Media.ARTIST);
        int titleColumn = cur.getColumnIndex(MediaStore.Audio.Media.TITLE);
        int albumColumn = cur.getColumnIndex(MediaStore.Audio.Media.ALBUM);
        int albumArtColumn = cur.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
        int durationColumn = cur.getColumnIndex(MediaStore.Audio.Media.DURATION);
        int idColumn = cur.getColumnIndex(MediaStore.Audio.Media._ID);


        do {
            Log.d("GetArtistSingleSong", cur.getString(artistColumn));
            if (cur.getString(artistColumn).equals(artist)) {
                mArtistSongs.add(new Song(
                        cur.getLong(idColumn),
                        cur.getString(artistColumn),
                        cur.getString(titleColumn),
                        cur.getString(albumColumn),
                        cur.getLong(durationColumn),
                        cur.getLong(albumArtColumn)));

                //         cur.moveToLast();
            }
        } while (cur.moveToNext());

    }

    // Get All Artist Songs

    public List<Song> getAllArtistSong() {

        Log.d("getAllArtistSong", "List Artist Song " + mArtistSongs);
        return mArtistSongs;
    }

    //Song
    public class Song {
        long id;
        String artist;
        String title;
        String album;
        long albumId;
        long duration;
        String uri;
        String albumArt;


        public Song(long id, String artist, String title, String album, long duration, long albumId) {
            this.id = id;
            this.artist = artist;
            this.title = title;
            this.album = album;
            this.duration = duration;
            this.albumId = albumId;
            //  this.uri = getURI();
            this.albumArt = getAlbumArt();

        }

        public long getId() {
            return id;
        }

        public String getArtist() {
            return artist;
        }

        public String getTitle() {
            return title;
        }

        public String getAlbum() {
            return album;
        }

        public long getDuration() {
            return duration;
        }

        public long getAlbumId() {
            return albumId;
        }

        public String getURI() {

            Uri mediaContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            String[] projection = new String[]{MediaStore.Audio.Media._ID, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID};
            String selection = MediaStore.Audio.Media._ID + "=?";
            String[] selectionArgs = new String[]{"" + id}; //This is the id you are looking for

            Cursor mediaCursor = getContentResolver().query(mediaContentUri, projection, selection, selectionArgs, null);

            if (mediaCursor.getCount() >= 0) {
                mediaCursor.moveToPosition(0);
                uri = mediaCursor.getString(mediaCursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                //Do something with the data
            }
            mediaCursor.close();
            return uri;
        }

        public String getAlbumArt() {


           String path = sharedpreferencesplugin.getArt(String.valueOf(albumId));

            // Log.d("PATH ART IMAGE",path);
            return path;
        }

        HashMap<String, Object> toMap() {
            HashMap<String, Object> songsMap = new HashMap<>();
            songsMap.put("id", id);
            songsMap.put("artist", artist);
            songsMap.put("title", title);
            songsMap.put("album", album);
            songsMap.put("albumId", albumId);
            songsMap.put("duration", duration);
            songsMap.put("albumArt", albumArt);

            return songsMap;
        }

    }

    //Album

    public class Album {

        private long id;

        private String albumName;

        private String artist;

        private int nbre_of_songs;

        private String albumImg;

        private String Year;

        public Album(long id, String albumName, String artistName, int nbre_of_songs, String albumImg) {
            this.id = id;
            this.albumName = albumName;
            this.artist = artistName;
            this.nbre_of_songs = nbre_of_songs;
            this.albumImg = albumImg;

        }


        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getAlbumName() {
            return albumName;
        }

        public void setAlbumName(String albumName) {
            this.albumName = albumName;
        }

        public String getArtistName() {
            return artist;
        }

        public void setArtistName(String artistName) {
            this.artist = artistName;
        }

        public int getNbre_of_songs() {
            return nbre_of_songs;
        }

        public void setNbre_of_songs(int nbre_of_songs) {
            this.nbre_of_songs = nbre_of_songs;
        }

        public String getAlbumImg() {
            return albumImg;
        }

        public void setAlbumImg(String albumImg) {
            this.albumImg = albumImg;
        }

        HashMap<String, Object> toMap() {
            HashMap<String, Object> albumsMap = new HashMap<>();
            albumsMap.put("id", id);
            albumsMap.put("artist", artist);
            albumsMap.put("albumname", albumName);
            albumsMap.put("nbreSongs", nbre_of_songs);
            albumsMap.put("albumArt", albumImg);

            return albumsMap;
        }
    }

    //ARTIST

    public class Artist {

        private long id;
        private String name;
        private int nbreAlbum;
        private int nbreTracks;
        private int nbreSong;
        private String art;

        public Artist(long id, String name, int nbreAlbum, int nbreTracks, String art) {
            this.id = id;
            this.name = name;
            this.nbreAlbum = nbreAlbum;
            this.nbreTracks = nbreTracks;
            this.art = art;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getNbreAlbum() {
            return nbreAlbum;
        }

        public void setNbreAlbum(int nbreAlbum) {
            this.nbreAlbum = nbreAlbum;
        }

        public int getNbreTracks() {
            return nbreTracks;
        }

        public void setNbreTracks(int nbreTracks) {
            this.nbreTracks = nbreTracks;
        }

        public int getNbreSong() {
            return nbreSong;
        }

        public void setNbreSong(int nbreSong) {
            this.nbreSong = nbreSong;
        }

        public String getArt() {
            return art;
        }

        public void setArt(String art) {
            this.art = art;
        }

        HashMap<String, Object> toMap() {
            HashMap<String, Object> artistMap = new HashMap<>();
            artistMap.put("id", id);
            artistMap.put("name", name);
            artistMap.put("nbreAlbum", nbreAlbum);
            artistMap.put("nbreTracks", nbreTracks);
            artistMap.put("albumArt", art);

            return artistMap;
        }
    }


}

/*
    // Playlist

 public class Playlist {


        private String name;
        private List<String> members;


        public Playlist() {

        }


    }



//GENRE

    public class Genre {


        public Genre( ) {

        }


    }



}
*/



