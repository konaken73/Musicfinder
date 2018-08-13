import 'dart:async';
import 'dart:ui';

import 'package:flutter/services.dart';
import 'package:musicfinder/storemedia.dart';


typedef void TimeChangeHandler(Duration duration);
typedef void ErrorHandler(String message);


class Musicfinder {
  static const MethodChannel _channel =
      const MethodChannel('musicfinder');
  TimeChangeHandler durationHandler;
  TimeChangeHandler positionHandler;
  VoidCallback startHandler;
  VoidCallback completionHandler;

  VoidCallback refresh;

  ErrorHandler errorHandler;

  List<String> songs;
  int audioDuration=0;
  int currentPosition=0;

  String state;



  get setPosition => null;

  Future<dynamic> play(int url, {bool isLocal: false}) =>
      _channel.invokeMethod('play', {"url": url, "isLocal": isLocal});

  Future<dynamic> prev(int url) => _channel.invokeMethod('previous',{"url": url});

  Future<dynamic> next(int url) => _channel.invokeMethod('next',{"url": url});

  Future<dynamic> pause() => _channel.invokeMethod('pause');

  Future<dynamic> stop() => _channel.invokeMethod('stop');

  Future<dynamic> mute(bool muted) => _channel.invokeMethod('mute', muted);

  Future<dynamic> position() => _channel.invokeMethod('position');

  Future<dynamic> getImages() => _channel.invokeMethod('ImageData1');

  Future<dynamic> seek(int seconds) =>
      _channel.invokeMethod('seek', seconds);

  Future<dynamic> createPlaylist(String url) => _channel.invokeMethod('createplaylist',{"playlist_name": url});

  Future<dynamic> Playlist() => _channel.invokeMethod('playlist');

  Future<dynamic> addMembers(List<String> members)=>_channel.invokeMethod('addTrackToplaylist',{"members":members});

  Future<dynamic> getTrackPlaylist(int playlist_id)=> _channel.invokeMethod('getTrackPlaylist',{"playlist_id":playlist_id});

  Future<dynamic> Genres() async {

    var completer=new Completer();

    var data=await _channel.invokeMethod('Genres');

    print("getGenres in Genres $data");
    setGenreslist(data.toString().split(', ')).then((bool committed){
      //refresh();
    });

  }
  Future<dynamic> getSongByGenres(int genre) async
  {
    var completer=new Completer();

    var data = await _channel.invokeMethod('getSongByGenres',{"genre":genre});

    print(data.toString());

    print("getSongByGenres in  Genres ${data.toString()}");
    setGenreslistSong(data.toString().split(', ')).then((bool committed)
    {
      // refresh();
    });
  }




  void setDurationHandler(TimeChangeHandler handler) {
    durationHandler = handler;
  }


  void setPositionHandler(TimeChangeHandler handler) {
    positionHandler = handler;
  }


  void setStartHandler(VoidCallback callback) {
    startHandler = callback;
  }


  void setRefresh(VoidCallback callback)
  {
    refresh = callback;
  }

  void setCompletionHandler(VoidCallback callback) {
    completionHandler = callback;
  }

  void setErrorHandler(ErrorHandler handler) {
    errorHandler = handler;
  }


  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    print('Musicfinder getPlatformVersion $version');
    return version;
  }

  static Future<dynamic> allSongs() async {
    var completer = new Completer();

    Map params = <String, dynamic>{
      "handlePermissions": true,
      "executeAfterPermissionGranted": true,
    };
    List<dynamic> songs = await _channel.invokeMethod('getSongs', params);
    var mySongs = songs.map((m) => new Song.fromMap(m)).toList();
    completer.complete(mySongs);
    return completer.future;
  }


  static Future<dynamic> AlbumSongs(int albumid) async {
    var completer = new Completer();

    List<dynamic> Albumsongs = await _channel.invokeMethod('getAlbumSong', {"albumid":albumid});
    var mySongs = Albumsongs.map((m) => new Song.fromMap(m)).toList();
    completer.complete(mySongs);
    return completer.future;
  }

  static Future<dynamic> ArtistSongs(String artist) async {
    var completer = new Completer();

    List<dynamic> Artistsongs = await _channel.invokeMethod('getArtistSong', {"artist":artist});
    var mySongs = Artistsongs.map((m) => new Song.fromMap(m)).toList();
    completer.complete(mySongs);
    return completer.future;
  }


  static Future<dynamic> Songs() async {
    var completer = new Completer();
    List<dynamic> songs = await _channel.invokeMethod('shareData');
    completer.complete(songs);
    return completer.future;
  }


  int index;


  Future platformCallHandler(MethodCall call) async {
    switch (call.method) {
      case "onFftVisualization":
        break;
      case "onWaveformVisualization":
        break;
      case "audio.onDuration":
        final duration = new Duration(milliseconds: call.arguments);
        if (durationHandler != null) {
          durationHandler(duration);
        }
        audioDuration=call.arguments;

        break;
      case "audio.onCurrentPosition":
        if (positionHandler != null) {
          positionHandler(new Duration(milliseconds: call.arguments));
        }

        break;
      case "audio.onStart":
        if (startHandler != null) {
          startHandler();
        }
        break;
      case "audio.onComplete":

        if (completionHandler != null) {
          completionHandler();
        }
        break;
      case "audio.onError":
        if (errorHandler != null) {
          errorHandler(call.arguments);
        }
        break;
      case "shareData":

        print("shareData Flutter");
        String list=call.arguments.toString();
        setSongs(list.split(', ')).then((bool committed){

        });
        //setSongCount(list.split(', ').length).then((bool committed){});
        break;
      case "ImageData":

        String list=call.arguments.toString();
        setImage(list).then((bool committed){});

        break;
      case "shareAlbum":

        String list=call.arguments.toString();
        setAlBums(list.split(', ')).then((bool committed){
        });
        //setAlbumCount(list.split(', ').length).then((bool committed){});
        print("shareAlbum");
        break;
      case "shareArtist":
        String list=call.arguments.toString();
        setArtist(list.split(', ')).then((bool committed){
        });

        //setArtistCount(list.split(', ').length).then((bool committed){});
        break;
      case "uriData":
        String data=call.arguments.toString();
        setUriData(data).then((bool committed){});
        break;
      case "PLAYLIST":
        String data=call.arguments.toString();
        print("True Playlist $data");
        setPlaylist(data.split(', ')).then((bool committed){
          refresh();
        });

        break;
      case "TrackPlaylist":
        String data=call.arguments.toString();
        print("True TrackPlaylist 1 $data");
        setTrackPlaylist(data).then((bool committed){
          refresh();
        });

        break;
      case "newCreated":
        String data=call.arguments.toString();
        print("newCreated $data");
        setTrackPlaylist(data).then((bool committed){});
        refresh();
        break;
      case "getGenres":
        List<String> data=call.arguments;
        print("getGenres $data");
        setGenreslist(data).then((bool committed){
          refresh();
        });

        break;
      default:

    }
  }
}


class Song
{
  int id;
  String artist;
  String title;
  String album;
  int albumId;
  int duration;

  String albumArt;


  Song(this.id, this.artist, this.title, this.album, this.albumId,
      this.duration,  this.albumArt);

  Song.fromMap(Map m) {
    id = m["id"];
    artist = m["artist"];
    title = m["title"];
    album = m["album"];
    albumId = m["albumId"];
    duration = m["duration"];
    albumArt = m["albumArt"];

  }
}

class Genre
{

  int id;
  String name;
  String Art;
  Genre(this.id,this.name,this.Art);
}

