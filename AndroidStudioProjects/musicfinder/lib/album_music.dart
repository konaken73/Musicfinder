
import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'storemedia.dart';



typedef void TimeChangeHandler(Duration duration);
typedef void ErrorHandler(String message);

class AlbumFinder {

  static const MethodChannel _channel = const MethodChannel('music_finder');

  TimeChangeHandler durationHandler;
  TimeChangeHandler positionHandler;
  VoidCallback startHandler;
  VoidCallback completionHandler;
  ErrorHandler errorHandler;

  int audioDuration=0;
  int currentPosition=0;

  String state ;


  AlbumFinder() {
    _channel.setMethodCallHandler(platformCallHandler);

  }


  Future<dynamic> getAlbums() => _channel.invokeMethod('shareAlbum1');


  void setDurationHandler(Duration duration) {

  }


  void setPositionHandler(Duration duration) {
    currentPosition = duration.inMilliseconds;
  }


  void setStartHandler(VoidCallback callback) {
    startHandler = callback;
  }

  void setCompletionHandler(VoidCallback callback) {

  }

  void setErrorHandler(ErrorHandler handler) {
    errorHandler = handler;
  }


  static Future<String> get platformVersion =>
      _channel.invokeMethod('getPlatformVersion');

  static Future<dynamic> allSongs() async {
    var completer = new Completer();

    Map params = <String, dynamic>{
      "handlePermissions": true,
      "executeAfterPermissionGranted": true,
    };
    List<dynamic> albums = await _channel.invokeMethod('getAlbums', params);

    var mySongs = albums.map((m) => new Album.fromMap(m)).toList();
    completer.complete(mySongs);
    return completer.future;
  }


  Future platformCallHandler(MethodCall call) async {

    print("AlbumPlateform");
    print(call.method);
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

        final duration = new Duration(milliseconds: call.arguments);
        if (positionHandler != null) {
          positionHandler(new Duration(milliseconds: call.arguments));
        }

        currentPosition=call.arguments;


        break;
      case "audio.onStart":
        if (startHandler != null) {
          startHandler();
        }
        break;
      case "audio.onComplete":
        if (completionHandler != null) {

        }

        break;
      case "audio.onError":
        if (errorHandler != null) {
          errorHandler(call.arguments);
        }
        break;
      case "shareDataAlbum":

        print("shareDataAlbum");
        String list=call.arguments.toString();
        setAlBums(list.split(', ')).then((bool committed){
        });
        setAlbumCount(list.split(', ').length).then((bool committed){});

        break;
      default:

    }
  }
}

class Album {

   int id;

   String albumName;

   String artist;

   int nbre_of_songs;

   String albumArt;


  Album(this.id,
   this.albumName ,
   this.artist,
   this.nbre_of_songs,
   this.albumArt);
  Album.fromMap(Map m) {
    id = m["id"];
    artist = m["artist"];
    albumName = m["albumname"];
    nbre_of_songs = m["nbreSongs"];
    albumArt = m["albumArt"];
  }
}