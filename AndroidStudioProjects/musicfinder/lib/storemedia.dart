import 'dart:async';


import 'package:shared_preferences/shared_preferences.dart';
//import 'music_player.dart';



Future<bool> setTrackIndex(int indexSong) async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  prefs.setInt("indexSong",indexSong);

  return prefs.commit();
}

Future<int> getTrackIndex() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();
  int TrackIndex = prefs.getInt("indexSong");
  if(TrackIndex==null)
    {
      TrackIndex=0;
    }

  return TrackIndex;
}


Future<int> getNextTrackIndex() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();
  int TrackIndex = prefs.getInt("indexSong");
  if(TrackIndex==null)
  {
    TrackIndex=0;
  }

  return TrackIndex;
}

Future<int> getPrevTrackIndex() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();
  int TrackIndex = prefs.getInt("indexSong");
  if(TrackIndex==null)
  {
    TrackIndex=0;
  }

  return TrackIndex;
}
Future<bool> setActiveAlbum(int index,String name,String art,int nbre) async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  print("ActiveAlbum  $index");
  print("ActiveAlbumName $name");
  print("ActiveAlbumNameNbre $nbre");
  prefs.setString("ActiveAlbumName", name);
  prefs.setString("ActiveAlbumArt", art);
  prefs.setInt("ActiveAlbum", index);
  prefs.setInt("ActiveAlbumNbre", nbre);
  return prefs.commit();
}


Future<int> getActiveAlbum() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  int index =prefs.getInt("ActiveAlbum");

  if(index==null)
    {
      index=0;
    }
  return prefs.getInt("ActiveAlbum");
}


Future<int> getActiveAlbumNbre() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  int index =prefs.getInt("ActiveAlbumNbre");

  if(index==null)
  {
    index=0;
  }
  return index;
}

Future<String> getActiveAlbumName() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();


  return prefs.getString("ActiveAlbumName");
}


Future<String> getActiveAlbumArt() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  return prefs.getString("ActiveAlbumArt");
}



Future<bool> setActiveArtist(int index,String name,int nbreAlbum,int nbreTracks) async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  prefs.setInt("ActiveArtist", index);
  prefs.setInt("ActiveArtistAlbum", nbreAlbum);
  prefs.setInt("ActiveArtistTrack", nbreTracks);
  prefs.setString("ActiveArtistName", name);
  print("Active name $name");
  return prefs.commit();
}


Future<int> getActiveArtist() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  int index=prefs.getInt("ActiveArtist");
  if(index==null)
  {
    index=0;
  }

  List<Map> list;

  return index;
}


Future<int> getActiveArtistAlbum() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  return prefs.getInt("ActiveArtistAlbum");
}




Future<int> getActiveArtistTrack() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  return prefs.getInt("ActiveArtistTrack");
}



Future<String> getActiveArtistName() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  return prefs.getString("ActiveArtistName");
}





Future<bool> setBackgroundColor(int color) async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  prefs.setInt("BackgroungColor", color);

  setBackgroundState(false);

  return prefs.commit();
}


Future<int> getBackgroundColor() async
{

  SharedPreferences prefs = await SharedPreferences.getInstance();

  int index = prefs.getInt("BackgroungColor");

  if(index==null){
    index=0;
  }

  return index;
}


Future<bool> setBackgroundImage(String image) async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  prefs.setString("BackgroungImage", image);
  setBackgroundState(true);
  return prefs.commit();
}


Future<String> getBackgroundImage() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  String image = prefs.getString("BackgroungImage");

  if(image==null)
    {
      image=" ";
    }

  return image;

}






Future<bool> setBackgroundState(bool state) async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  prefs.setBool("BackgroungState", state);
  return prefs.commit();
}


Future<bool> getBackgroundState() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();
  bool state = prefs.getBool("BackgroungState");

  if((state==null))
    {
      state=false;
    }

  return state;

}


Future<bool> setImage(String image) async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();
  prefs.setString("AllImage", image);
  //prefs.setInt("AllImageCount",list.length);
  return prefs.commit();
}


Future<List<String>> getAllImage() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  String images = prefs.getString("AllImage");
  List<String> list;

  if(images==null)
    {
      list=[""];
    }
    else{
    list=images.split(', ');
  }

  return list;

}


Future<bool> setPlayState(String playstate) async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  prefs.setString("PlayState", playstate);

  return prefs.commit();
}


Future<String> getPlayState() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  String playstate = prefs.getString("PlayState");

  if(playstate==null)
  {
    playstate="idle";
  }

  return playstate;

}



Future<bool> setSongs(List<String> Songs) async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  prefs.setStringList("AllSongs",Songs);
  prefs.setInt("SongCount",Songs.length);

  return prefs.commit();

}



//Future<List<Song>> getSongs() async
Future<List<String>> getSongs() async
//{
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  List<String> songs= prefs.getStringList("AllSongs");

   return songs;
}

Future<bool> setSeek(double seek) async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  prefs.setDouble("SeekPosition",seek);

  return prefs.commit();
}



Future<double> getSeek() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  double seek= prefs.getDouble("SeekPosition");

  return seek;
}



Future<bool> setAlBums(List<String> albums) async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  prefs.setStringList("AllAlBums", albums);
  prefs.setInt("AlbumCount",albums.length);

  return prefs.commit();
}


Future<List<String>> getAlbums() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  return prefs.getStringList("AllAlBums");
}


Future<bool> setArtist(List<String> artists) async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  prefs.setStringList("AllArtist", artists);
  prefs.setInt("ArtistCount",artists.length);


  return prefs.commit();
}


Future<List<String>> getArtists() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  return prefs.getStringList("AllArtist");
}



Future<bool> setGenres(List<String> genres) async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  prefs.setStringList("AllGenres", genres);

  return prefs.commit();
}


Future<List<String>> getGenres() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  return prefs.getStringList("AllGenres");
}



Future<bool> setSongsFavorite(List<String> Songs) async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  prefs.setStringList("AllSongsFavorite",Songs);

  prefs.setInt("FavoriteSongCount",Songs.length);

  return prefs.commit();
}



Future<List<String>> getSongsFavorite() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  List<String> songs= prefs.getStringList("AllSongsFavorite");

  if(songs==null)
    {
      songs=["ELTID"];
    }

  return songs;
}



Future<bool> setCurrentListSongs(List<String> Songs) async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  prefs.setStringList("CurrentListSongs",Songs);
  print("STORE setCurrentListSongs $Songs");
  return prefs.commit();
}


Future<List<String>> getCurrentListSongs() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  List<String> songs= prefs.getStringList("CurrentListSongs");

  if(songs==null)
  {
    songs=["ELTID"];
  }

  return songs;
}


Future<bool> setAlbumFavorite(List<String> Songs) async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  prefs.setStringList("AllAlbumFavorite",Songs);

  prefs.setInt("FavoriteAlbumCount",Songs.length);


  return prefs.commit();
}



Future<List<String>> getAlbumFavorite() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  List<String> songs= prefs.getStringList("AllAlbumFavorite");

  if(songs==null)
  {
    songs=["ELTID"];
  }

  return songs;
}


Future<List<String>> getFavorites() async
{
  List<String> all;
 // List<String> songs=await getSongsFavorite();
  List<String> albums=await getAlbumFavorite();
  List<String> artists=await getArtistFavorite();
  all=  albums+ artists;

  return all;
}




Future<bool> setArtistFavorite(List<String> Songs) async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  prefs.setStringList("AllArtistFavorite",Songs);

  prefs.setInt("FavoriteArtistCount",Songs.length);

  return prefs.commit();
}



Future<List<String>> getArtistFavorite() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  List<String> songs= prefs.getStringList("AllArtistFavorite");

  if(songs==null)
  {
    songs=["ELTID"];
  }

  return songs;
}


Future<bool> setRecentlyPlay(String song) async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();


  List<String> recent= await getRecentlyPlay();

  if(recent.length<3)
  {
    recent.add(song);
  }
  else{
       String element1=recent[1];
       String element2=recent[2];

       recent.insert(0, element1);
       recent.insert(1, element2);
       recent.insert(2, song);
  }
  prefs.setStringList("AllSongsRecently",recent);

  return prefs.commit();
}



Future<List<String>> getRecentlyPlay() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  List<String> songs= prefs.getStringList("AllSongsRecently");

  return songs;
}



Future<bool> setRecentAlbum(String album) async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();


  List<String> recent= await getRecentAlbum();

  if(recent.length<3)
  {
    recent.add(album);
  }
  else{
    String element1=recent[1];
    String element2=recent[2];

    recent.insert(0, element1);
    recent.insert(1, element2);
    recent.insert(2, album);
  }


  prefs.setStringList("AllAlbumRecently",recent);

  return prefs.commit();
}



Future<List<String>> getRecentAlbum() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  List<String> songs= prefs.getStringList("AllAlbumRecently");

  return songs;
}



List<String> song(String stringSong)
{
  List<String> song = stringSong.split("ELTID");

  return song;
}

List<String> album(String stringSong)
{
  List<String> song = stringSong.split("ELTID");

  return song;
}

List<String> artist(String stringSong)
{
  List<String> song = stringSong.split("ELTID");

  return song;
}




Future<bool> setNextTrack(int indexSong) async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  prefs.setInt("nextSong",indexSong);

  return prefs.commit();
}

Future<int> getNextTrack() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();
  int TrackIndex = prefs.getInt("nextSong");

  return TrackIndex;
}




Future<bool> setTrackId(int Id) async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  prefs.setInt("ListId",Id);

  return prefs.commit();
}


Future<int> getTrackId() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();
  int TrackIndex = prefs.getInt("ListId");

  
  return TrackIndex;
}


//Nbre de song
Future<bool> setSongCount(int count) async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  prefs.setInt("SongCount",count);

  return prefs.commit();
}


Future<int> getSongCount() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  int count=prefs.getInt("SongCount");

  if(count==null)
    {
      count=0;
    }
  return count;
}



//Nbre d'Album
Future<bool> setAlbumCount(int count) async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  prefs.setInt("AlbumCount",count);

  return prefs.commit();
}


Future<int> getAlbumCount() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  int count = prefs.getInt("AlbumCount");
  if(count==null)
    {
      count=0;
    }
  return count;
}


//Nbre d'Artiste
Future<bool> setArtistCount(int count) async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  prefs.setInt("ArtistCount",count);

  return prefs.commit();
}


Future<int> getArtistCount() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  int count = prefs.getInt("ArtistCount");
  if(count==null)
    {
      count=0;
    }

  return count;
}


//URI DATA FOR CURRENT SONG
Future<bool> setUriData(String uri) async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  prefs.setString("UriData",uri);

  return prefs.commit();
}


Future<String> getUriData() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  return prefs.getString("UriData");
}

//THIS FUNCTION CONTROL THE NUMBER OF TIME THE  APPLICATION IS LAUNCH

Future<bool> setAppLaunch(String launch) async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  prefs.setString("AppLaunch",launch);

  return prefs.commit();
}


Future<String> getAppLaunch() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  return prefs.getString("AppLaunch");
}

//Nbre de song
Future<bool> setFavoriteSongCount(int count) async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  prefs.setInt("FavoriteSongCount",count);

  return prefs.commit();
}


Future<int> getFavoriteSongCount() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  int count= prefs.getInt("FavoriteSongCount");
  if(count==null)
  {
    count=0;
  }

  return count;
}


//Nbre de FAVORI ALBUM
Future<bool> setFavoriteAlbumCount(int count) async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  prefs.setInt("FavoriteAlbumCount",count);

  return prefs.commit();
}


Future<int> getFavoriteAlbumCount() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  int count= prefs.getInt("FavoriteAlbumCount");
  if(count==null)
  {
    count=0;
  }

  return count;
}


//Nbre de FAVORI ARTIST
Future<bool> setFavoriteArtistCount(int count) async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  prefs.setInt("FavoriteArtistCount",count);

  return prefs.commit();
}


Future<int> getFavoriteArtistCount() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  int count=prefs.getInt("FavoriteArtistCount");

  if(count==null)
    {
      count=0;
    }

  return count;
}



//SHUFFLE STATE
Future<bool> setStateShuffle(bool state) async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  prefs.setBool("StateShuffle",state);

  return prefs.commit();
}


Future<bool> getStateShuffle() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  return prefs.getBool("StateShuffle");
}


//Nbre de Playlist
Future<bool> setPlaylist(List<String> playlist) async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  prefs.setStringList("Playlist",playlist);

  prefs.setInt("PlaylistCount",playlist.length);

  return prefs.commit();
}


Future<int> getPlaylistCount() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  int count=prefs.getInt("PlaylistCount");

  if(count==null)
  {
    count=0;
  }

  return count;
}


Future<List<String>> getPlaylist() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  List<String> playlist= prefs.getStringList("Playlist");

  return playlist;
}


//Nbre de Playlist
Future<bool> setTrackPlaylist(String playlist) async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  prefs.setString("TrackPlaylist",playlist);

  prefs.setInt("TrackPlaylistCount",playlist.length);

  return prefs.commit();
}


Future<int> getTrackPlaylistCount() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  int count=prefs.getInt("TrackPlaylistCount");

  if(count==null)
  {
    count=0;
  }

  return count;
}


Future<List<String>> getTrackPlaylist() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  String playlist= prefs.getString("TrackPlaylist");
  if(playlist==null)
    {
      return null;
    }
    else{
    return playlist.split(']')[0].split('[')[1].split(', ');
  }

}




//Nbre de Genres
Future<bool> setGenreslist(List<String> playlist) async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  prefs.setStringList("Genreslist",playlist);

  print("Genreslist ${playlist.length}");

  prefs.setInt("GenreslistCount",playlist.length);

  return prefs.commit();
}


Future<int> getGenreslistCount() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  int count=prefs.getInt("GenreslistCount");

  if(count==null)
  {
    count=0;
  }

  return count;
}


Future<List<String>> getGenreslist() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  List<String> playlist= prefs.getStringList("Genreslist");

  return playlist;
}



// song by Genres
Future<bool> setGenreslistSong(List<String> playlist) async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  prefs.setStringList("GenreslistSong",playlist);

  print("GenreslistSong in ${playlist.length} / $playlist");

  prefs.setInt("GenreslistSongCount",playlist.length);

  return prefs.commit();
}


Future<int> getGenreslistSongCount() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  int count=prefs.getInt("GenreslistSongCount");

  if(count==null)
  {
    count=0;
  }

  return count;
}


Future<List<String>> getGenreslistSong() async
{
  SharedPreferences prefs = await SharedPreferences.getInstance();

  List<String> playlist= prefs.getStringList("GenreslistSong");

  print("GenreslistSong in  get ${playlist.length} / $playlist");

  return playlist;
}
